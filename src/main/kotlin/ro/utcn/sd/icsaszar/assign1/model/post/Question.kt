package ro.utcn.sd.icsaszar.assign1.model.post

import java.time.LocalDateTime
import javax.persistence.Entity

@Entity
//TODO: Discriminator field
class Question(
        authorId: Long,
        text: String,
        posted: LocalDateTime,
        var title: String) : Post(authorId, text, posted){

}