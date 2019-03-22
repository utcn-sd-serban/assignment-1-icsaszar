package ro.utcn.sd.icsaszar.assign1.model

import ro.utcn.sd.icsaszar.assign1.model.post.Post
import javax.persistence.*

@Entity
data class User(
        var userName: String = ""
) : GenericEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null

    @OneToMany(mappedBy = "author")
    var posts: MutableList<Post> = mutableListOf()
}
