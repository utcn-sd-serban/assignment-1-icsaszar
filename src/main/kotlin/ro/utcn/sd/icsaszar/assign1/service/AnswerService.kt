package ro.utcn.sd.icsaszar.assign1.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ro.utcn.sd.icsaszar.assign1.event.NewAnswerEvent
import ro.utcn.sd.icsaszar.assign1.event.NewAnswerPayload
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.persistence.api.RepositoryFactory

@Service
class AnswerService(
        private val repositoryFactory: RepositoryFactory,
        private val eventPublisher: ApplicationEventPublisher){

    @Transactional
    fun submitAnswer(text: String, author: User, question: Question): Answer {
        val answer = Answer(author, text)
        answer.setQuestion(question)
        val result = repositoryFactory.answerRepository.save(answer)
        eventPublisher.publishEvent(NewAnswerEvent(NewAnswerPayload(question.id!!, result.toDTO())))
        return result
    }

    @Transactional
    fun findAllByPostIdOrderByScoreDesc(postId: Long): List<Answer> =
        repositoryFactory.answerRepository.findAllByPostIdOrderByScoreDesc(postId)

    @Transactional
    fun findAllByAuthorId(id: Long): List<Answer> =
        repositoryFactory.answerRepository.findAllByAuthor_Id(id)

    @Transactional
    fun findById(id: Long): Answer? =
        repositoryFactory.answerRepository.findById(id)

    @Transactional
    fun deleteAnswer(answer: Answer) {
        answer.removeAnswerTo()
        repositoryFactory.answerRepository.delete(answer)
    }

    @Transactional
    fun updateAnswer(answer: Answer) =
        repositoryFactory.answerRepository.save(answer)
}