package ro.utcn.sd.icsaszar.assign1.dto

import ro.utcn.sd.icsaszar.assign1.model.User

data class UserDTO (
        val id: Long,
        val username: String,
        val votes: List<VoteDTO>
        ){

    companion object {
        fun fromUser(user: User): UserDTO {
            return UserDTO(
                    user.id!!,
                    user.userName,
                    user.votes.map {it.toDTO()}
            )
        }
    }

}

data class UserDetailsDTO (
        val id: Long,
        val votes: List<VoteDTO>
)