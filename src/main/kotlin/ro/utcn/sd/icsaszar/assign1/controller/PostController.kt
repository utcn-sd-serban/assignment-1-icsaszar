package ro.utcn.sd.icsaszar.assign1.controller

import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.*
import ro.utcn.sd.icsaszar.assign1.dto.*
import ro.utcn.sd.icsaszar.assign1.exception.PostNotFoundException
import ro.utcn.sd.icsaszar.assign1.exception.VotingException
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.service.*
import java.lang.Exception

@RestController
class PostController(
  private val questionService: QuestionService,
  private val answerService: AnswerService,
  private val voteService: VoteService,
  private val userDetailsService: StackOverflowUserDetailsService,
  private val tagService: TagService,
  private val messagingTemplate: SimpMessagingTemplate
){

    @GetMapping("/posts/{id}")
    fun getPost(@PathVariable id: Long): QuestionDTO{
        val question = questionService.findById(id)
        return question?.toDTO() ?: throw PostNotFoundException(id)
    }

    @DeleteMapping("/posts/{postId}")
    fun deletePost(@PathVariable postId: Long){
        val currentUser = userDetailsService.loadCurrentUser()
        val question = questionService.findById(postId)
        val answer = answerService.findById(postId)
        if((answer == null) && (question == null))
            throw PostNotFoundException(postId)
        if(question != null){
            if(question.author.id!! == currentUser.id)
                return questionService.deleteQuestion(question)
        }else if(answer != null) {
            if(answer.author.id!! == currentUser.id)
                return answerService.deleteAnswer(answer)
        }
    }

    @PostMapping("/posts/{postId}/votes")
    fun voteOnPost(@PathVariable postId: Long, @RequestBody vote: VoteDTO): VoteDTO{
        val currentUser = userDetailsService.loadCurrentUser()
        val question = questionService.findById(postId)
        val answer = answerService.findById(postId)
        val dir: Short = when {
            vote.direction == "up" -> 1
            vote.direction == "down" -> -1
            else -> throw Exception()
        }
        if((answer == null) && (question == null))
            throw PostNotFoundException(postId)
        return when {
            question != null -> voteService.vote(question, dir, currentUser).toDTO()
            answer != null -> voteService.vote(answer, dir, currentUser).toDTO()
            else -> throw Exception()
        }
    }
}