package ro.utcn.sd.icsaszar.assign1.model.post

import ro.utcn.sd.icsaszar.assign1.model.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.persistence.*
import javax.validation.constraints.NotNull

data class RawAnswerData(
        val authorId: Long,
        val text: String,
        val posted: LocalDateTime,
        val id: Long,
        val questionId: Long
)

@Entity
class Answer(
        author: User = User(),

        text: String = "",

        posted: LocalDateTime = LocalDateTime.now(),

        id: Long? = null

) : Post(author, text, posted, id){
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", nullable = false)
    var answerTo: Question? = null

    init {
        author.addPost(this)
    }

    constructor(data: RawAnswerData):this(
            text = data.text,
            posted = data.posted,
            id = data.id
    )

    fun setQuestion(question: Question): Answer{
        question.addAnswer(this)
        answerTo = question
        return this
    }

    fun display(score: Int? = null): String{
        val formatter =
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
        val sb: StringBuilder = StringBuilder()
        sb.append(" ".repeat(5)).append(id.toString()).append("\n")
            .append(" ".repeat(5)).append(postText).append("\n")
            .append(" ".repeat(5)).append(author.userName.padEnd(30))
            .append(posted.format(formatter)).append("\n")
        if(score != null)
            sb.append(" ".repeat(5)).append("Score: $score").append("\n")
        return sb.toString()
    }

    fun removeAnswerTo(){
        answerTo?.removeAnswer(this)
    }

    override fun hashCode(): Int {
        return 31
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Answer) return false

        return id?.equals(other.id) ?: false
    }


}