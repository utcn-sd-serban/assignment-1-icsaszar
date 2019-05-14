package ro.utcn.sd.icsaszar.assign1.controller

import org.springframework.web.bind.annotation.*
import ro.utcn.sd.icsaszar.assign1.dto.AnswerDTO
import ro.utcn.sd.icsaszar.assign1.dto.NewAnswerDTO
import ro.utcn.sd.icsaszar.assign1.dto.NewQuestionDTO
import ro.utcn.sd.icsaszar.assign1.dto.QuestionDTO
import ro.utcn.sd.icsaszar.assign1.exception.PostNotFoundException
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.service.*

@RestController
class PostController(
  private val questionService: QuestionService,
  private val answerService: AnswerService,
  private val voteService: VoteService,
  private val userDetailsService: StackOverflowUserDetailsService,
  private val tagService: TagService
){

    @GetMapping("/posts")
    fun getAllPosts(): List<QuestionDTO> {
        val questions = questionService.listAllQuestionsByPosted()
        return questions.map {  it.toDTO() }
    }

    @PostMapping("/posts")
    fun addPost(@RequestBody questionDTO: NewQuestionDTO): QuestionDTO{
        //FIXME: How we should we go about RequestBodies?
        // One DTO per request type is a lot of classes
        val currentUser = userDetailsService.loadCurrentUser()
        val tags: Set<Tag> = questionDTO.tags.mapNotNull { tagService.findTagByName(it.name)}.toSet()
        return questionService.postQuestion(currentUser, questionDTO.title, questionDTO.text, tags).toDTO()
    }

    @GetMapping("/posts/{id}")
    fun getPost(@PathVariable id: Long): QuestionDTO{
        val question = questionService.findById(id)
        return question?.toDTO() ?: throw PostNotFoundException(id)
    }

    @PostMapping("/posts/{postId}/answers")
    fun addAnswer(@PathVariable postId: Long, @RequestBody answerDTO: NewAnswerDTO): AnswerDTO{
        val currentUser = userDetailsService.loadCurrentUser()
        val question = questionService.findById(answerDTO.postId) ?: throw PostNotFoundException(answerDTO.postId)
        return answerService.submitAnswer(answerDTO.text, currentUser, question).toDTO()
    }

    @DeleteMapping("/posts/{postId}")
    fun deletePost(@PathVariable postId: Long){
        //FIXME Check if the user is deleting his own question in an efficient way
        val currentUser = userDetailsService.loadCurrentUser()
        val question = questionService.findById(postId) ?: throw PostNotFoundException(postId)
        if(question.author.id!! == currentUser.id)
            return questionService.deleteQuestion(question)
        //FIXME Respond with unauthorized
    }

    @DeleteMapping("posts/{postId}/answers/{answerId}")
    fun deleteAnswer(@PathVariable postId: Long, @PathVariable answerId: Long){
        val currentUser = userDetailsService.loadCurrentUser()
        val answer = answerService.findById(answerId) ?: throw PostNotFoundException(answerId)
        //FIXME Add a proper exception
        if(answer.answerTo!!.id!! != postId)
            throw Exception()
        if(answer.author.id!! == currentUser.id!!)
            return answerService.deleteAnswer(answer)
    }
}