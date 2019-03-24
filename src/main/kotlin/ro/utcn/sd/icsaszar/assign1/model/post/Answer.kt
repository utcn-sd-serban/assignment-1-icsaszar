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
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", nullable = false)
    var answerTo: Question? = null

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

    fun display(): String{
        val formatter =
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
        val sb: StringBuilder = StringBuilder()
        sb.append(" ".repeat(5)).append(id.toString()).append("\n")
            .append(" ".repeat(5)).append(postText).append("\n")
            .append(" ".repeat(5)).append(author.userName.padEnd(30))
            .append(posted.format(formatter)).append("\n")
        return sb.toString()
    }

    @PreRemove
    fun removeAnswerTo(){
        answerTo?.removeAnswer(this)
    }
}