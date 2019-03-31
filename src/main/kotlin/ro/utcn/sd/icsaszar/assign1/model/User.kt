package ro.utcn.sd.icsaszar.assign1.model

import org.hibernate.annotations.Check
import org.hibernate.validator.constraints.Length
import ro.utcn.sd.icsaszar.assign1.model.post.Post
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Length(min = 3)
    @Column(unique = true)
    var userName: String = "",

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    var isBanned: Boolean = false,

    var isMod: Boolean = false,

    @OneToMany(mappedBy = "author", cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    var posts: MutableSet<Post> = HashSet(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var votes: MutableSet<Vote> = mutableSetOf()

) : GenericEntity {

    fun addPost(post: Post): User{
        posts.add(post)
        post.author = this
        return this
    }

    fun addPosts(posts: List<Post>): User{
        posts.forEach { addPost(it) }
        return this
    }

    fun addVote(vote: Vote): User{
        votes.add(vote)
        return this
    }

    override fun hashCode(): Int {
        return 31
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        return id?.equals(other.id) ?: false
    }
}
