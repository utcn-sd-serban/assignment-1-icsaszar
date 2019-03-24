package ro.utcn.sd.icsaszar.assign1.persistence.jdbc

import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.RawQuestionData
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.persistence.api.QuestionRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcAnswerRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcQuestionRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcTagRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcUserRepository

class JdbcEagerFetchQuestionRepository(
        private val questionRepository: JdbcQuestionRepository,
        private val userRepository: JdbcUserRepository,
        private val answerRepository: JdbcAnswerRepository,
        private val tagRepository: JdbcTagRepository

): QuestionRepository{

    override fun findAllByAuthor_Id(id: Long): List<Question> {
        val questions = questionRepository.findAllByAuthor_Id(id)
        return questions.mapNotNull { assembleQuestion(it) }
    }

    override fun findAllByTags(tag: Tag): List<Question> {
        val questions = questionRepository.findAllByTags(tag)
        return questions.mapNotNull { assembleQuestion(it) }
    }

    override fun findAllByTitleContainsIgnoreCase(text: String): List<Question> {
        val questions = questionRepository.findAllByTitleContainsIgnoreCase(text)
        return questions.mapNotNull { assembleQuestion(it) }
    }

    override fun findAllByOrderByPostedDesc(): List<Question> {
        val questions = questionRepository.findAllByOrderByPostedDesc()
        return questions.mapNotNull { assembleQuestion(it) }
    }

    override fun save(entity: Question): Question =
        questionRepository.save(entity)

    override fun delete(entity: Question) =
        questionRepository.delete(entity)

    override fun findById(id: Long): Question? {
        val questionData = questionRepository.findById(id) ?: return null
        return assembleQuestion(questionData)
    }

    override fun findAll(): List<Question> {
        val questions = questionRepository.findAll()
        return questions.mapNotNull { assembleQuestion(it) }
    }

    private fun assembleQuestion(questionData: RawQuestionData): Question?{
        val user = userRepository.findById(questionData.authorId)
        return if (user != null){
            val question = Question(questionData)
            val answers = answerRepository.findAllByAnswerTo_Id(questionData.id).map { Answer(it, user, question) }
            question.answers = answers.toMutableList()
            question.author = user
            question.tags = tagRepository.findAllByQuestions_Id(questionData.id).toMutableSet()
            question
        } else
            null
    }
}