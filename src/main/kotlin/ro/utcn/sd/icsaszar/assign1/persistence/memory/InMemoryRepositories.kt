package ro.utcn.sd.icsaszar.assign1.persistence.memory

import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.persistence.api.AnswerRepository
import ro.utcn.sd.icsaszar.assign1.persistence.api.QuestionRepository
import ro.utcn.sd.icsaszar.assign1.persistence.api.TagRepository
import ro.utcn.sd.icsaszar.assign1.persistence.api.UserRepository

class QuestionInMemoryRepository(private val mainRepository: InMemoryRepository) : QuestionRepository{

    override fun findAllByTitleContainsIgnoreCase(text: String): List<Question> = findAll().filter { it.title.contains(text,true) }

    override fun findAllByOrderByPostedDesc(): List<Question> = findAll().sortedByDescending { it.posted }

    override fun findAllByTags(tag: Tag): List<Question> = findAll().filter { it.tags.contains(tag) }

    override fun save(entity: Question): Question = mainRepository.saveQuestion(entity)

    override fun delete(entity: Question) = mainRepository.deleteQuestion(entity)

    override fun findById(id: Long): Question? = mainRepository.findQuestionById(id)

    override fun findAll(): List<Question> = mainRepository.findAllQuestions()
}

class AnswerInMemoryRepository(private val mainRepository: InMemoryRepository) :  AnswerRepository {

    override fun save(entity: Answer): Answer = mainRepository.saveAnswer(entity)

    override fun delete(entity: Answer) = mainRepository.deleteAnswer(entity)

    override fun findById(id: Long): Answer? = mainRepository.findAnswerById(id)

    override fun findAll(): List<Answer> = mainRepository.findAllAnswers()
}

class TagInMemoryRepository(private val mainRepository: InMemoryRepository) : TagRepository {

    override fun findByName(name: String): Tag? = findAll().find { it.name == name }

    override fun save(entity: Tag): Tag = mainRepository.saveTag(entity)

    override fun delete(entity: Tag) = mainRepository.deleteTag(entity)

    override fun findById(id: Long): Tag? = mainRepository.findTagById(id)

    override fun findAll(): List<Tag> = mainRepository.findAllTags()
}

class UserInMemoryRepository(private val mainRepository: InMemoryRepository) : UserRepository {

    override fun findByUserName(userName: String): User? =
        findAll().find { it.userName == userName }

    override fun save(entity: User): User = mainRepository.saveUser(entity)

    override fun delete(entity: User) = mainRepository.deleteUser(entity)

    override fun findById(id: Long): User? = mainRepository.findUserById(id)

    override fun findAll(): List<User> = mainRepository.findAllUsers()
}