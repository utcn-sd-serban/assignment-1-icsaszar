package ro.utcn.sd.icsaszar.assign1.controller

import org.springframework.web.bind.annotation.*
import ro.utcn.sd.icsaszar.assign1.dto.AnswerDTO
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
        val currentUser = userDetailsService.loadCurrentUser()
        val tags: Set<Tag> = questionDTO.tags.mapNotNull { tagService.findTagByName(it.name)}.toSet()
        return questionService.postQuestion(currentUser, questionDTO.title, questionDTO.text, tags).toDTO()
    }

    @GetMapping("/posts/{id}")
    fun getPost(@PathVariable id: Long): QuestionDTO{
        val question = questionService.findById(id)
        return question?.toDTO() ?: throw PostNotFoundException(id)
    }

    @PostMapping("/posts/{postId}")
    fun addAnswer(@PathVariable postId: Int, @RequestBody answerDTO: AnswerDTO): QuestionDTO{
        TODO()
    }
}