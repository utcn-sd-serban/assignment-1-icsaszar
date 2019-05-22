package ro.utcn.sd.icsaszar.assign1.controller

import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.*
import ro.utcn.sd.icsaszar.assign1.dto.AnswerDTO
import ro.utcn.sd.icsaszar.assign1.dto.NewAnswerDTO
import ro.utcn.sd.icsaszar.assign1.dto.NewQuestionDTO
import ro.utcn.sd.icsaszar.assign1.dto.QuestionDTO
import ro.utcn.sd.icsaszar.assign1.event.Event
import ro.utcn.sd.icsaszar.assign1.event.NewQuestionEvent
import ro.utcn.sd.icsaszar.assign1.exception.AuthorizationException
import ro.utcn.sd.icsaszar.assign1.exception.InvalidTagException
import ro.utcn.sd.icsaszar.assign1.exception.PostNotFoundException
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.service.*

@RestController
class QuestionController(
        private val questionService: QuestionService,
        private val answerService: AnswerService,
        private val userDetailsService: StackOverflowUserDetailsService,
        private val tagService: TagService,
        private val messagingTemplate: SimpMessagingTemplate
) {

    @GetMapping("/questions")
    fun getAllQuestions(
            @RequestParam(name = "title", required = false) title: String?,
            @RequestParam(name = "tag", required = false) tagName: String?
    ): List<QuestionDTO> {
        val result = if (title == null && tagName == null) {
            questionService.listAllQuestionsByPosted()
        } else if (title != null && tagName != null) {
            throw Exception("Only one parameter should be given!")
        } else if (title != null) {
            questionService.findAllByTitleContaining(title)
        } else if(tagName != null){
            val tag = tagService.findTagByName(tagName) ?: throw InvalidTagException(tagName)
            questionService.findAllByTag(tag)
        } else throw Exception("The type checker won't shut up about tagName being null")

        return result.map { it.toDTO() }
    }


    @PostMapping("/questions")
    fun addQuestion(@RequestBody questionDTO: NewQuestionDTO): QuestionDTO {
        val currentUser = userDetailsService.loadCurrentUser()
        val tags: Set<Tag> = questionDTO.tags.mapNotNull { tagService.findTagByName(it.name) }.toSet()
        return questionService.postQuestion(currentUser, questionDTO.title, questionDTO.text, tags).toDTO()
    }

    @PutMapping("/questions/{id}")
    fun editQuestion(@PathVariable id: Long, @RequestBody newText: String): QuestionDTO {
        val currentUser = userDetailsService.loadCurrentUser()
        val question = questionService.findById(id) ?: throw PostNotFoundException(id)
        if (question.author.id!! != currentUser.id!!)
            throw AuthorizationException.notOwnContent
        question.postText = newText
        return questionService.updateQuestion(question).toDTO()
    }

    @GetMapping("/questions/{id}")
    fun getQuestion(@PathVariable id: Long): QuestionDTO {
        val question = questionService.findById(id)
        return question?.toDTO() ?: throw PostNotFoundException(id)
    }

    @PostMapping("/questions/{postId}/answers")
    fun addAnswer(@PathVariable postId: Long, @RequestBody answerDTO: NewAnswerDTO): AnswerDTO {
        val currentUser = userDetailsService.loadCurrentUser()
        val question = questionService.findById(answerDTO.postId) ?: throw PostNotFoundException(answerDTO.postId)
        return answerService.submitAnswer(answerDTO.text, currentUser, question).toDTO()
    }

    @DeleteMapping("/questions/{postId}")
    fun deleteQuestion(@PathVariable postId: Long) {
        val currentUser = userDetailsService.loadCurrentUser()
        val question = questionService.findById(postId) ?: throw PostNotFoundException(postId)
        if (question.author.id!! == currentUser.id)
            return questionService.deleteQuestion(question)
        else throw AuthorizationException.notOwnContent
    }

    @DeleteMapping("questions/{postId}/answers/{answerId}")
    fun deleteAnswer(@PathVariable postId: Long, @PathVariable answerId: Long) {
        val currentUser = userDetailsService.loadCurrentUser()
        val answer = answerService.findById(answerId) ?: throw PostNotFoundException(answerId)
        if (answer.answerTo!!.id!! != postId)
            throw Exception()
        if (answer.author.id!! == currentUser.id!!)
            return answerService.deleteAnswer(answer)
        else throw AuthorizationException.notOwnContent
    }

    @PutMapping("/questions/{questionId}/answers/{answerId}")
    fun editAnswer(
            @PathVariable questionId: Long,
            @PathVariable answerId: Long,
            @RequestBody newText: String
    ): AnswerDTO {
        val currentUser = userDetailsService.loadCurrentUser()
        val answer = answerService.findById(answerId) ?: throw PostNotFoundException(answerId)
        if (answer.answerTo!!.id != questionId)
            throw PostNotFoundException(questionId)

        if (answer.author.id!! != currentUser.id!!)
            throw AuthorizationException.notOwnContent

        answer.postText = newText
        return answerService.updateAnswer(answer).toDTO()
    }


    @EventListener(Event::class)
    fun handleEvent(event: Event) {
        println("Sent event ${event.type}")
        messagingTemplate.convertAndSend("/topic/events", event)
    }
}