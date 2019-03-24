package ro.utcn.sd.icsaszar.assign1.model.post

import org.hibernate.validator.constraints.Length
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
        val id: Long
)

@Entity
class Question(
        author: User = User(),
        text: String = "",
        posted: LocalDateTime = LocalDateTime.now(),
        id: Long? = null,

        @Column
        @Length(min = 3)
        var title: String = "",

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "question_tag",
                joinColumns = [JoinColumn(name = "question_id")],
                inverseJoinColumns = [JoinColumn(name = "tag_id")])
        var tags: MutableSet<Tag> = mutableSetOf(),

        @OneToMany(mappedBy = "answerTo", fetch = FetchType.EAGER)
        var answers: MutableList<Answer> = mutableListOf()


        ) : Post(author, text, posted, id){

    constructor(data: RawQuestionData):this(
            text = data.text,
            posted = data.posted,
            id = data.id,
            title = data.title
    )

    fun addAnswer(answer: Answer){
        answers.add(answer)
    }

    fun display(): String {
        val formatter =
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
        val sb: StringBuilder = StringBuilder()
        sb.append(title).append("\n")
            .append(postText).append("\n")
            .append(author.userName.padEnd(30))
            .append(posted.format(formatter)).append("\n")
        return sb.toString()
    }
}