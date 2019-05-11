package ro.utcn.sd.icsaszar.assign1.model

import ro.utcn.sd.icsaszar.assign1.dto.ConvertibleToDTO
import ro.utcn.sd.icsaszar.assign1.dto.VoteDTO
import ro.utcn.sd.icsaszar.assign1.model.post.Post
import java.io.Serializable
import javax.persistence.*

data class RawVoteData(var postId: Long, var userId: Long, var vote: Short)

@Entity
@Table(name = "votes")
@IdClass(VoteId::class)
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
): Serializable, ConvertibleToDTO<VoteDTO> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vote) return false

        if (post != other.post) return false
        if (user != other.user) return false

        return true
    }

    override fun hashCode(): Int {
        return 31
    }

    override fun toDTO(): VoteDTO {
        return VoteDTO.fromVote(this)
    }
}

data class VoteId(var post: Post? = null, var user: User? = null): Serializable
