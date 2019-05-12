package ro.utcn.sd.icsaszar.assign1.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ro.utcn.sd.icsaszar.assign1.dto.UserDetailsDTO
import ro.utcn.sd.icsaszar.assign1.dto.VoteDTO
import ro.utcn.sd.icsaszar.assign1.model.post.Post
import ro.utcn.sd.icsaszar.assign1.service.*

@RestController
class UserController(
        private val userService: UserService,
        private val userDetailsService: StackOverflowUserDetailsService,
        private val voteService: VoteService,
        private val answerService: AnswerService,
        private val questionService: QuestionService
) {
    @GetMapping("/account")
    fun getCurrentUserPosts(): List<Post>{
        val currentUser = userDetailsService.loadCurrentUser()
        val userAnswers = answerService.findAllByAuthorId(currentUser.id!!)
        val userQuestions = questionService.findAllByAuthorId(currentUser.id!!)
        return userAnswers + userQuestions
    }

    @GetMapping("/account/details")
    fun getUserDetails(): UserDetailsDTO{
        val currentUser = userDetailsService.loadCurrentUser()
        val votes = voteService.getVoteByUser(currentUser.id!!).map { it.toDTO() }
        return UserDetailsDTO(currentUser.id!!, votes)
    }
}