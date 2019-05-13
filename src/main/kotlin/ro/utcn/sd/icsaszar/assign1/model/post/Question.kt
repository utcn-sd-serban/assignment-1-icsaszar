package ro.utcn.sd.icsaszar.assign1.model.post

import org.hibernate.validator.constraints.Length
import ro.utcn.sd.icsaszar.assign1.dto.ConvertibleToDTO
import ro.utcn.sd.icsaszar.assign1.dto.QuestionDTO
import ro.utcn.sd.icsaszar.assign1.dto.TagDTO
import ro.utcn.sd.icsaszar.assign1.model.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.persistence.*

data class RawQuestionData(
        val authorId: Long,
        val text: String,
        val posted: LocalDateTime,
        val title: String,
        val score: Int,
        val id: Long
)

@Entity
class Question(
        author: User = User(),
        text: String = "",
        posted: LocalDateTime = LocalDateTime.now(),

        id: Long? = null,
        score: Int? = null,
        @Length(min = 3)
        var title: String = ""
        ) : Post(author, text, posted, id, score), ConvertibleToDTO<QuestionDTO>{

    init {
        author.addPost(this)
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE])
    @JoinTable(name = "question_tag",
            joinColumns = [JoinColumn(name = "question_id")],
            inverseJoinColumns = [JoinColumn(name = "tag_id")])
    var tags: MutableSet<Tag> = mutableSetOf()

    @OneToMany(
            mappedBy = "answerTo",
            fetch = FetchType.EAGER,
            cascade = [CascadeType.REMOVE, CascadeType.MERGE],
            orphanRemoval = true
    )
    var answers: MutableSet<Answer> = mutableSetOf()

    constructor(data: RawQuestionData): this(
            text = data.text,
            posted = data.posted,
            id = data.id,
            title = data.title,
            score = data.score
    )

    fun addAnswer(answer: Answer): Question{
        answer.answerTo = this
        answers.add(answer)
        return this
    }

    fun addTag(tag: Tag): Question{
        tag.questions.add(this)
        tags.add(tag)
        return this
    }

    fun addTags(tags: Set<Tag>): Question{
        tags.forEach {addTag(it)}
        return this
    }

    fun removeAnswer(answer: Answer): Boolean{
        return if( answers.remove(answer) ){
            answer.removeAnswerTo()
            true
        } else false
    }

    fun display(): String {
        val formatter =
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
        val sb: StringBuilder = StringBuilder()
        sb.append(id.toString().padEnd(5))
            .append(title).append("\n")
            .append(postText).append("\n")
            .append(author.userName.padEnd(30))
            .append(posted.format(formatter)).append("\n")
        if(score != null)
            sb.append("Score: $score").append("\n")
        return sb.toString()
    }

    override fun hashCode(): Int {
        return 31
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Question) return false

        return id?.equals(other.id) ?: false
    }

    override fun toDTO(): QuestionDTO {
        return QuestionDTO.fromQuestion(this)
    }
}