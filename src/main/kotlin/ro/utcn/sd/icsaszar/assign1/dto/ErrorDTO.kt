package ro.utcn.sd.icsaszar.assign1.dto

import ro.utcn.sd.icsaszar.assign1.exception.PostNotFoundException
import ro.utcn.sd.icsaszar.assign1.exception.UserNotFoundException
import ro.utcn.sd.icsaszar.assign1.exception.VotingException

data class ErrorDTO(val message: String){
    companion object {
        fun fromException(ex: UserNotFoundException) = ErrorDTO(ex.message!!)
        fun fromException(ex: PostNotFoundException) = ErrorDTO(ex.message!!)
        fun fromException(ex: VotingException) = ErrorDTO(ex.message!!)
    }
}