package ro.utcn.sd.icsaszar.assign1

import org.junit.Assert.*
import ro.utcn.sd.icsaszar.assign1.persistence.memory.InMemoryRepositoryFactory
import ro.utcn.sd.icsaszar.assign1.persistence.api.RepositoryFactory

import org.junit.Test
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.service.QuestionService

class QuestionServiceTests{
    val users = listOf(User("User1"), User("User2"), User("User3"))
    val tags = listOf(Tag("java"), Tag("kotlin"), Tag("spring-boot"), Tag("general"))
    val questions = mutableListOf<Question>()
    val answers = mutableListOf<Answer>()

    private fun createMockedFactory(): RepositoryFactory {
        val factory = InMemoryRepositoryFactory()

        val userRepository = factory.userRepository
        userRepository.apply {
            if (findAll().isEmpty()) {
                users.forEach { save(it) }
            }
        }

        val tagRepository = factory.tagRepository
        tagRepository.apply {
            if(findAll().isEmpty()){
                tags.forEach { save(it) }
            }
        }

        val questionRepository = factory.questionRepository
        questions += Question(users[0], "^Title", title = "How do i even").addTag(tags[3])
        questions += Question(users[0], "Ubuntu is so bad like you can't even install an exe, like no wonder nobody uses it", title = "Why can'i i install an exe on ubuntu")
        questions += Question(users[1], "Pls help the deadline is tomorrow", title = "Help my program keeps crashing").addTag(tags[3])
        questions += Question(users[2], "My hands hurt from so much typing",title = "Why is java so verbose?").addTag(tags[0])
        questionRepository.apply {
            if(findAll().isEmpty()) {
                questions.forEach {save(it)}
            }
        }

        val answerRepository = factory.answerRepository
        answers += Answer(users[2], "Yes").setQuestion(questions[0])
        answers += Answer(users[1], "No").setQuestion(questions[0])
        answers += Answer(users[2], "Dude...").setQuestion(questions[1])
        answers += Answer(users[0], "I have the same problem").setQuestion(questions[2])
        answers += Answer(users[0], "Never mind i figured it out").setQuestion(questions[0])
        answerRepository.apply {
            if (findAll().isEmpty()) {
                answers.forEach {
                    save(it)
                }
            }
        }
        return factory
    }

    @Test
    fun testAllQuestionsInserted(){
        val factory = createMockedFactory()
        val questionService = QuestionService(factory)

        questions.forEach { assertNotNull(questionService.findById(it.id!!)) }
    }

    @Test
    fun testSearchByTitle(){
        val factory = createMockedFactory()
        val questionService = QuestionService(factory)

        val actual = questionService.findAllByTitleContaining("help")

        assertEquals(1, actual.size)
        assertEquals(questions[2], actual[0])
    }

    @Test
    fun testPostQuestion(){
        val factory = createMockedFactory()
        val questionService = QuestionService(factory)

        val oldNrQuestions = questionService.listAllQuestions().size
        val newQuestion = questionService.postQuestion(users[0], "Test title", "Test text", setOf())

        assertNotNull(newQuestion.id)

        val newNrQuestions = questionService.listAllQuestions().size
        assertEquals(oldNrQuestions+1, newNrQuestions)

        val insertedQuestion = questionService.findById(newQuestion.id!!)
        assertEquals(newQuestion, insertedQuestion)
    }
}