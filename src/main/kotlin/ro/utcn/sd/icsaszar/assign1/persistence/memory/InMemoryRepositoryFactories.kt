package ro.utcn.sd.icsaszar.assign1.persistence.memory

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.persistence.api.*


@Component
@ConditionalOnProperty(name = ["a1.repository-type"], havingValue = "MEMORY")
abstract class InMemoryQuestionRepositoryFactory : QuestionRepositoryFactory {
    abstract val repository: QuestionInMemoryRepository

    override fun createRepository(): QuestionRepository {
        return repository
    }
}

@Component
@ConditionalOnProperty(name = ["a1.repository-type"], havingValue = "MEMORY")
abstract class InMemoryAnswerRepositoryFactory : AnswerRepositoryFactory {
    abstract val repository: AnswerInMemoryRepository

    override fun createRepository(): AnswerRepository {
        return repository
    }
}

@Component
@ConditionalOnProperty(name = ["a1.repository-type"], havingValue = "MEMORY")
abstract class InMemoryTagRepositoryFactory : TagRepositoryFactory {
    abstract val repository: TagInMemoryRepository

    override fun createRepository(): TagRepository{
        return repository
    }
}

@Component
@ConditionalOnProperty(name = ["a1.repository-type"], havingValue = "MEMORY")
abstract class InMemoryUserRepositoryFactory : UserRepositoryFactory {
    abstract val repository: UserInMemoryRepository

    override fun createRepository(): UserRepository {
        return repository
    }
}