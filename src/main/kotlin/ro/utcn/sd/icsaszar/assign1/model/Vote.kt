package ro.utcn.sd.icsaszar.assign1.model

import ro.utcn.sd.icsaszar.assign1.model.post.Post
import java.io.Serializable
import javax.persistence.*

data class RawVoteData(var postId: Long, var userId: Long, var vote: Short)

data class Vote(
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    var post: Post,

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    var user: User,

    var vote: Short
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vote) return false

        if (post != other.post) return false
        if (user != other.user) return false
        if (vote != other.vote) return false

        return true
    }

    override fun hashCode(): Int {
        var result = post.hashCode()
        result = 31 * result + user.hashCode()
        result = 31 * result + vote
        return result
    }
}