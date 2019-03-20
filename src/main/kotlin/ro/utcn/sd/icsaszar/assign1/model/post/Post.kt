package ro.utcn.sd.icsaszar.assign1.model.post

import ro.utcn.sd.icsaszar.assign1.model.GenericEntity
import ro.utcn.sd.icsaszar.assign1.model.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Post(
        @ManyToOne
        @JoinColumn(name = "author_id", nullable = false)
        open var author: User = User(),

        open var text: String = "",

        open var posted: LocalDateTime = LocalDateTime.now()
) : GenericEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null

}