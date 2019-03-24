package ro.utcn.sd.icsaszar.assign1.persistence.jdbc

import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.RawAnswerData
import ro.utcn.sd.icsaszar.assign1.persistence.api.AnswerRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcAnswerRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcQuestionRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcUserRepository

class JdbcEagerFetchAnswerRepository(
        private val userRepository: JdbcUserRepository,
        private val answerRepository: JdbcAnswerRepository,
        private val questionRepository: JdbcQuestionRepository) : AnswerRepository{

    override fun findAllByAuthor_Id(id: Long): List<Answer> {
        val answers = answerRepository.findAllByAuthor_Id(id)
        return answers.mapNotNull { assembleAnswer(it) }
    }

    override fun findAllByAnswerTo_Id(questionId: Long): List<Answer> {
        val answers = answerRepository.findAllByAnswerTo_Id(questionId)
        return answers.mapNotNull { assembleAnswer(it) }
    }

    override fun save(entity: Answer): Answer =
        answerRepository.save(entity)

    override fun delete(entity: Answer) = answerRepository.delete(entity)

    override fun findById(id: Long): Answer? {
        val answer = answerRepository.findById(id) ?: return null
        return assembleAnswer(answer)
    }

    override fun findAll(): List<Answer> {
        val answers = answerRepository.findAll()
        return answers.mapNotNull { assembleAnswer(it) }
    }

    private fun assembleAnswer(answerData: RawAnswerData): Answer?{
        val user = userRepository.findById(answerData.authorId)
        val questionData = questionRepository.findById(answerData.questionId)
        return if(user != null)
            if(questionData != null){
                val question: Question = Question(questionData)
                val answer = Answer(answerData)
                answer.setQuestion(question)
                answer.setAuthor(user)
                answer
            }
            else null
        else
            null
    }
}