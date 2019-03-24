package ro.utcn.sd.icsaszar.assign1.model

import org.hibernate.annotations.Check
import org.hibernate.validator.constraints.Length
import ro.utcn.sd.icsaszar.assign1.model.post.Post
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Length(min = 3)
    var userName: String = "",

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    @OneToMany(mappedBy = "author")
    var posts: MutableList<Post> = mutableListOf()
) : GenericEntity {
}
