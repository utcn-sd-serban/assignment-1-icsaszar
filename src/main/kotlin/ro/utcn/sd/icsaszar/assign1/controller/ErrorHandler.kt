package ro.utcn.sd.icsaszar.assign1.controller

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ro.utcn.sd.icsaszar.assign1.dto.ErrorDTO
import ro.utcn.sd.icsaszar.assign1.exception.PostNotFoundException
import ro.utcn.sd.icsaszar.assign1.exception.UserNotFoundException
import ro.utcn.sd.icsaszar.assign1.exception.VotingException

@Component
@RestControllerAdvice
class ErrorHandler{

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PostNotFoundException::class)
    fun handlePostNotFoundException(ex: PostNotFoundException): ErrorDTO{
        return ErrorDTO.fromException(ex)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(ex: PostNotFoundException): ErrorDTO{
        return ErrorDTO.fromException(ex)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(VotingException::class)
    fun handleVotingException(ex: VotingException): ErrorDTO{
        return ErrorDTO.Companion.fromException(ex)
    }
}