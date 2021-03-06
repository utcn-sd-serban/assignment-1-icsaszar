package ro.utcn.sd.icsaszar.assign1.persistence.jdbc

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import ro.utcn.sd.icsaszar.assign1.persistence.api.*
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.*

@Component
@ConditionalOnProperty(name = ["a1.repository-type"], havingValue = "JDBC")
class JdbcRepositoryFactory(template: JdbcTemplate) : RepositoryFactory{
    private val postRepository: JdbcPostRepository = JdbcPostRepository(template)

    private val lazyQuestionRepository: JdbcQuestionRepository = JdbcQuestionRepository(template, postRepository)
    private val lazyAnswerRepository: JdbcAnswerRepository = JdbcAnswerRepository(template, postRepository)
    private val lazyUserRepository: JdbcUserRepository = JdbcUserRepository(template)
    private val lazyTagRepository: JdbcTagRepository = JdbcTagRepository(template)
    private val lazyVoteRepository: JdbcVoteRepository = JdbcVoteRepository(template)

    override val answerRepository: AnswerRepository = JdbcEagerFetchAnswerRepository(lazyVoteRepository, lazyUserRepository, lazyAnswerRepository, lazyQuestionRepository)
    override val questionRepository: QuestionRepository = JdbcEagerFetchQuestionRepository(lazyVoteRepository, lazyQuestionRepository, lazyUserRepository, lazyAnswerRepository, lazyTagRepository)
    override val userRepository: UserRepository = JdbcEagerFetchUserRepository(lazyVoteRepository, lazyUserRepository, lazyAnswerRepository, lazyQuestionRepository)
    override val tagRepository: TagRepository = JdbcEagerFetchTagRepository(lazyTagRepository, lazyQuestionRepository)
    override val voteRepository: VoteRepository = JdbcEagerFetchVoteRepository(lazyVoteRepository, lazyAnswerRepository, lazyQuestionRepository, lazyUserRepository)
}