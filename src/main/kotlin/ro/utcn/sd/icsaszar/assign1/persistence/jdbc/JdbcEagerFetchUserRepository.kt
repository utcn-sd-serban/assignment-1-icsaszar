package ro.utcn.sd.icsaszar.assign1.persistence.jdbc

import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Post
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.persistence.api.UserRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcAnswerRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcQuestionRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcUserRepository

class JdbcEagerFetchUserRepository(
        private val userRepository: JdbcUserRepository,
        private val answerRepository: JdbcAnswerRepository,
        private val questionRepository: JdbcQuestionRepository
): UserRepository{
    override fun findByUserName(userName: String): User? {
        val user = userRepository.findByUserName(userName) ?: return null
        return assembleUser(user)
    }

    override fun save(entity: User): User =
        userRepository.save(entity)

    override fun delete(entity: User) =
        userRepository.delete(entity)

    override fun findById(id: Long): User? {
        val user = userRepository.findById(id) ?: return null
        return assembleUser(user)
    }

    override fun findAll(): List<User> {
        val users = userRepository.findAll()
        return users.mapNotNull { assembleUser(it) }
    }

    private fun assembleUser(user: User): User?{
        val questions = questionRepository.findAllByAuthor_Id(user.id!!).map { Question(it) }
        val answers = answerRepository.findAllByAuthor_Id(user.id!!).map { Answer(it) }
        user.addPosts(questions)
        user.addPosts(answers)
        return user
    }
}