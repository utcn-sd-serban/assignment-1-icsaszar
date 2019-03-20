package ro.utcn.sd.icsaszar.assign1.model.post

import ro.utcn.sd.icsaszar.assign1.model.GenericEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
//TODO: Discriminator field
abstract class Post(
        @Column
        open var authorId: Long,

        @Column
        open var text: String,

        @Column
        open var posted: LocalDateTime
) : GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = 0
}