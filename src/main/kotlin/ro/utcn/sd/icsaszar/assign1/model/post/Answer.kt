package ro.utcn.sd.icsaszar.assign1.model.post

import ro.utcn.sd.icsaszar.assign1.model.User
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
//TODO: Discriminator field
class Answer(
        author: User = User(),

        text: String = "",

        posted: LocalDateTime = LocalDateTime.now()
) : Post(author, text, posted){

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    var answerTo: Question = Question()
}