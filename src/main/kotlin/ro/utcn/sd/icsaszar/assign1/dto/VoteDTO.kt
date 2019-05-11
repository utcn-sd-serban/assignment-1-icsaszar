package ro.utcn.sd.icsaszar.assign1.dto

import ro.utcn.sd.icsaszar.assign1.model.Vote

data class VoteDTO(val postId: Long, val direction: Short) {

    companion object {
        fun fromVote(vote: Vote): VoteDTO{
            return VoteDTO(vote.post.id!!, vote.vote)
        }
    }

}
