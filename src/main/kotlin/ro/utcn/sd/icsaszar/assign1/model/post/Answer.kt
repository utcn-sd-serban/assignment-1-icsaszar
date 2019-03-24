package ro.utcn.sd.icsaszar.assign1.model.post

import ro.utcn.sd.icsaszar.assign1.model.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
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

        id: Long? = null,

        @ManyToOne
        @JoinColumn(name = "question_id", nullable = false)
        @NotNull
        var answerTo: Question = Question()

) : Post(author, text, posted, id){

    constructor(data: RawAnswerData):this(
            text = data.text,
            posted = data.posted,
            id = data.id
    )

    constructor(data: RawAnswerData, author: User, answerTo: Question):this(
            text = data.text,
            posted = data.posted,
            id = data.id,
            author = author,
            answerTo = answerTo
    )

    fun display(): String{
        val formatter =
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
        val sb: StringBuilder = StringBuilder()
        sb.append(" ".repeat(5)).append(postText).append("\n")
            .append(" ".repeat(5)).append(author.userName.padEnd(30))
            .append(posted.format(formatter)).append("\n")
        return sb.toString()
    }
}