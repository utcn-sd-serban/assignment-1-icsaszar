package ro.utcn.sd.icsaszar.assign1.model

import ro.utcn.sd.icsaszar.assign1.model.post.Post
import java.io.Serializable
import javax.persistence.*

@Embeddable
data class VoteId(
        var postId: Long,
        var userId: Long
): Serializable

data class Vote(
    @EmbeddedId
    var id: VoteId,

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("postId")
    var post: Post,

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    var user: User,

    var vote: Short
){

}