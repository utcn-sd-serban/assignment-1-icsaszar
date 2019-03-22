package ro.utcn.sd.icsaszar.assign1.persistence.memory

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.persistence.api.*

@Component
@ConditionalOnProperty(name = ["a1.repository-type"], havingValue = "MEMORY")
class InMemoryRepositoryFactory : RepositoryFactory {

    override val questionRepository: QuestionRepository = QuestionInMemoryRepository()
    override val answerRepository: AnswerRepository = AnswerInMemoryRepository()
    override val userRepository: UserRepository = UserInMemoryRepository()
    override val tagRepository: TagRepository = TagInMemoryRepository()
}