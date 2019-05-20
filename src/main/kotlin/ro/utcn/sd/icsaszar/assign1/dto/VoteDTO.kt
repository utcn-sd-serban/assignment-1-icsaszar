package ro.utcn.sd.icsaszar.assign1.dto

import ro.utcn.sd.icsaszar.assign1.model.Vote

data class VoteDTO(val postId: Long, val direction: String) {

    companion object {
        fun fromVote(vote: Vote): VoteDTO{
            val dir = if (vote.vote.compareTo(1) == 0) "up" else "down"
            return VoteDTO(vote.post.id!!, dir)
        }
    }

}
