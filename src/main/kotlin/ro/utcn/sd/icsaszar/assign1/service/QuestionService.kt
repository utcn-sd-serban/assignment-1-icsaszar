package ro.utcn.sd.icsaszar.assign1.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ro.utcn.sd.icsaszar.assign1.event.NewQuestionEvent
import ro.utcn.sd.icsaszar.assign1.exception.PostNotFoundException
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.persistence.api.RepositoryFactory

@Service
class QuestionService(
        private val repositoryFactory: RepositoryFactory,
        private val eventPublisher: ApplicationEventPublisher
){

    @Transactional
    fun listAllQuestions(): List<Question> = repositoryFactory.questionRepository.findAll()

    @Transactional
    fun listAllQuestionsByPosted(): List<Question> = repositoryFactory.questionRepository.findAllByOrderByPostedDesc()

    @Transactional
    fun postQuestion(author: User, title: String, text: String, tags: Set<Tag>): Question{
        val question = Question(author, text, title = title)
        question.addTags(tags)
        val returnValue = repositoryFactory.questionRepository.save(question)
        eventPublisher.publishEvent(NewQuestionEvent(returnValue.toDTO()))
        return returnValue
    }

    @Transactional
    fun updateQuestion(question: Question): Question{
        return repositoryFactory.questionRepository.save(question)
    }

    @Transactional
    fun deleteQuestion(question: Question){
        return repositoryFactory.questionRepository.delete(question)
    }

    @Transactional
    fun findAllByTag(tag: Tag): List<Question> =
        repositoryFactory.questionRepository.findAllByTags(tag)

    @Transactional
    fun findAllByTitleContaining(text: String): List<Question> =
        repositoryFactory.questionRepository.findAllByTitleContainsIgnoreCase(text)

    @Transactional
    fun findById(id: Long): Question? =
        repositoryFactory.questionRepository.findById(id)

    @Transactional
    fun findAllByAuthorId(id: Long): List<Question> =
        repositoryFactory.questionRepository.findAllByAuthor_Id(id)
}