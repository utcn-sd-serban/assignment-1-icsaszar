package ro.utcn.sd.icsaszar.assign1.persistence.data

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import ro.utcn.sd.icsaszar.assign1.persistence.api.*

@Component
@ConditionalOnProperty(name = ["a1.repository-type"], havingValue = "DATA")
class DataRepositoryFactory(
    questionDataRepository: QuestionDataRepository,
    answerDataRepository: AnswerDataRepository,
    userDataRepository: UserDataRepository,
    tagDataRepository: TagDataRepository,
    voteDataRepository: VoteDataRepository
) : RepositoryFactory{
    override val questionRepository: QuestionRepository = questionDataRepository
    override val answerRepository: AnswerRepository = answerDataRepository
    override val userRepository: UserRepository = userDataRepository
    override val tagRepository: TagRepository = tagDataRepository
    override val voteRepository: VoteRepository = voteDataRepository
}