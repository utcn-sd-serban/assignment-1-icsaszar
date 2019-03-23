package ro.utcn.sd.icsaszar.assign1.persistence.jdbc

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import ro.utcn.sd.icsaszar.assign1.persistence.api.*

@Component
@ConditionalOnProperty(name = ["a1.repository-type"], havingValue = "JDBC")
class JdbcRepositoryFactory(private val template: JdbcTemplate) : RepositoryFactory{
    private val postRepository: JdbcPostRepository = JdbcPostRepository(template)
    override val questionRepository: QuestionRepository = JdbcQuestionRepository(template, postRepository)
    override val answerRepository: AnswerRepository = JdbcAnswerRepository(template, postRepository)
    override val userRepository: UserRepository = JdbcUserRepository(template)
    override val tagRepository: TagRepository = JdbcTagRepository(template)
}