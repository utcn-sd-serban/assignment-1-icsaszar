package ro.utcn.sd.icsaszar.assign1.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.persistence.api.RepositoryFactory

@Service
class QuestionService(private val repositoryFactory: RepositoryFactory){

    @Transactional
    fun listAllQuestions(): List<Question> = repositoryFactory.questionRepository.findAll()

    @Transactional
    fun listAllQuestionsByPosted(): List<Question> = repositoryFactory.questionRepository.findAllByOrderByPostedDesc()

    @Transactional
    fun postQuestion(author: User, title: String, text: String, tags: Set<Tag>): Question =
        repositoryFactory.questionRepository.save(Question(author, text, title = title, tags = tags.toMutableSet()))

    @Transactional
    fun findAllByTag(tag: Tag): List<Question> =
        repositoryFactory.questionRepository.findAllByTags(tag)

    @Transactional
    fun findAllByTitleContaining(text: String): List<Question> =
        repositoryFactory.questionRepository.findAllByTitleContainsIgnoreCase(text)
}