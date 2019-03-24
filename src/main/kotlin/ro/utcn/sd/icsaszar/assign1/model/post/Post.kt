package ro.utcn.sd.icsaszar.assign1.model.post

import ro.utcn.sd.icsaszar.assign1.model.GenericEntity
import ro.utcn.sd.icsaszar.assign1.model.User
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Post(
        @ManyToOne
        @JoinColumn(name = "author_id", nullable = false)
        @NotNull
        open var author: User = User(),

        @NotBlank
        open var postText: String = "",

        open var posted: LocalDateTime = LocalDateTime.now(),

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override var id: Long? = null
) : GenericEntity {

}