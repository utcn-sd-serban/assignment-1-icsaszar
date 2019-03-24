package ro.utcn.sd.icsaszar.assign1.persistence.jpa

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import ro.utcn.sd.icsaszar.assign1.persistence.api.*
import javax.persistence.EntityManager

@Component
@ConditionalOnProperty(name = ["a1.repository-type"], havingValue = "JPA")
class JpaRepositoryFactory(entityManager: EntityManager): RepositoryFactory{
    override val questionRepository: QuestionRepository = QuestionJpaRepository(entityManager)
    override val answerRepository: AnswerRepository = AnswerJpaRepository(entityManager)
    override val userRepository: UserRepository = UserJpaRepository(entityManager)
    override val tagRepository: TagRepository = TagJpaRepository(entityManager)
}