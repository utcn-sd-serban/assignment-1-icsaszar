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
import ro.utcn.sd.icsaszar.assign1.exception.PostNotFoundException
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.service.*

@RestController
class QuestionController(
        private val questionService: QuestionService,
        private val answerService: AnswerService,
        private val voteService: VoteService,
        private val userDetailsService: StackOverflowUserDetailsService,
        private val tagService: TagService,
        private val messagingTemplate: SimpMessagingTemplate
){

    @GetMapping("/questions")
    fun getAllQuestions(): List<QuestionDTO> {
        val questions = questionService.listAllQuestionsByPosted()
        return questions.map {  it.toDTO() }
    }

    @PostMapping("/questions")
    fun addQuestion(@RequestBody questionDTO: NewQuestionDTO): QuestionDTO{
        val currentUser = userDetailsService.loadCurrentUser()
        val tags: Set<Tag> = questionDTO.tags.mapNotNull { tagService.findTagByName(it.name)}.toSet()
        return questionService.postQuestion(currentUser, questionDTO.title, questionDTO.text, tags).toDTO()
    }

    @GetMapping("/questions/{id}")
    fun getQuestion(@PathVariable id: Long): QuestionDTO{
        val question = questionService.findById(id)
        return question?.toDTO() ?: throw PostNotFoundException(id)
    }

    @PostMapping("/questions/{postId}/answers")
    fun addAnswer(@PathVariable postId: Long, @RequestBody answerDTO: NewAnswerDTO): AnswerDTO{
        val currentUser = userDetailsService.loadCurrentUser()
        val question = questionService.findById(answerDTO.postId) ?: throw PostNotFoundException(answerDTO.postId)
        return answerService.submitAnswer(answerDTO.text, currentUser, question).toDTO()
    }

    @DeleteMapping("/questions/{postId}")
    fun deleteQuestion(@PathVariable postId: Long){
        val currentUser = userDetailsService.loadCurrentUser()
        val question = questionService.findById(postId) ?: throw PostNotFoundException(postId)
        if(question.author.id!! == currentUser.id)
            return questionService.deleteQuestion(question)
    }

    @DeleteMapping("questions/{postId}/answers/{answerId}")
    fun deleteAnswer(@PathVariable postId: Long, @PathVariable answerId: Long){
        val currentUser = userDetailsService.loadCurrentUser()
        val answer = answerService.findById(answerId) ?: throw PostNotFoundException(answerId)
        //FIXME Add a proper exception
        if(answer.answerTo!!.id!! != postId)
            throw Exception()
        if(answer.author.id!! == currentUser.id!!)
            return answerService.deleteAnswer(answer)
    }


    @EventListener(Event::class)
    fun handleEvent(event: Event){
        println("Sent event ${event.type}")
        messagingTemplate.convertAndSend("/topic/events", event)
    }

}