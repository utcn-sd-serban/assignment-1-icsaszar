package ro.utcn.sd.icsaszar.assign1.persistence.memory

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.persistence.api.*

@Component
@ConditionalOnProperty(name = ["a1.repository-type"], havingValue = "MEMORY")
class InMemoryRepositoryFactory : RepositoryFactory {
    private val mainRepository: InMemoryRepository = InMemoryRepository()
    override val questionRepository: QuestionRepository = QuestionInMemoryRepository(mainRepository)
    override val answerRepository: AnswerRepository = AnswerInMemoryRepository(mainRepository)
    override val userRepository: UserRepository = UserInMemoryRepository(mainRepository)
    override val tagRepository: TagRepository = TagInMemoryRepository(mainRepository)
    override val voteRepository: VoteRepository = VoteInMemoryRepository(mainRepository)
}