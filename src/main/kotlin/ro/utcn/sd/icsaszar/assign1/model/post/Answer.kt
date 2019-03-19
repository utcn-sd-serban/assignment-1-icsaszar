package ro.utcn.sd.icsaszar.assign1.model.post

import java.time.LocalDateTime
import javax.persistence.Entity

@Entity
class Answer(
        authorId: Long,

        text: String,

        posted: LocalDateTime
) : Post(authorId, text, posted){

}