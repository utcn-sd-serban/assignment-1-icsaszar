package ro.utcn.sd.icsaszar.assign1.persistence.jdbc

import ro.utcn.sd.icsaszar.assign1.model.Vote
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.RawAnswerData
import ro.utcn.sd.icsaszar.assign1.persistence.api.AnswerRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcAnswerRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcQuestionRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcUserRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcVoteRepository

class JdbcEagerFetchAnswerRepository(
        private val voteRepository: JdbcVoteRepository,
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

    override fun deleteAll() = answerRepository.deleteAll()

    override fun findById(id: Long): Answer? {
        val answer = answerRepository.findById(id) ?: return null
        return assembleAnswer(answer)
    }

    override fun findAll(): List<Answer> {
        val answers = answerRepository.findAll()
        return answers.mapNotNull { assembleAnswer(it) }
    }

    override fun findAllByPostIdOrderByScoreDesc(postId: Long): List<Answer> {
        val answers = answerRepository.findAllByPostIdOrderByScoreDesc(postId)
        return answers.mapNotNull { assembleAnswer(it) }
    }

    private fun assembleAnswer(answerData: RawAnswerData): Answer?{
        val user = userRepository.findById(answerData.authorId) ?: return null
        val questionData = questionRepository.findById(answerData.questionId) ?: return null
        val voteData = voteRepository.findAllByPost_Id(answerData.id)
        val question: Question = Question(questionData)
        val answer = Answer(answerData)
        voteData.forEach { answer.addVote(Vote(answer, user, it.vote)) }
        answer.setQuestion(question)
        answer.setAuthor(user)
        return answer
    }
}