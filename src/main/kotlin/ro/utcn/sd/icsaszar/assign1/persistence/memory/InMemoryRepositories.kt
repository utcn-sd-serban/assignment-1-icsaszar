package ro.utcn.sd.icsaszar.assign1.persistence.memory

import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.Vote
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.persistence.api.*

class QuestionInMemoryRepository(private val mainRepository: InMemoryRepository) : QuestionRepository{

    override fun findAllByAuthor_Id(id: Long): List<Question> =
        findAll().filter { it.author.id!! == id }

    override fun findAllByTitleContainsIgnoreCase(text: String): List<Question> =
        findAll().filter { it.title.contains(text,true) }

    override fun findAllByOrderByPostedDesc(): List<Question> =
        findAll().sortedByDescending { it.posted }

    override fun findAllByTags(tag: Tag): List<Question> =
        findAll().filter { it.tags.contains(tag) }

    override fun save(entity: Question): Question = mainRepository.saveQuestion(entity)

    override fun delete(entity: Question) = mainRepository.deleteQuestion(entity)

    override fun findById(id: Long): Question? = mainRepository.findQuestionById(id)

    override fun findAll(): List<Question> = mainRepository.findAllQuestions()
}

class AnswerInMemoryRepository(private val mainRepository: InMemoryRepository) :  AnswerRepository {

    override fun findAllByAuthor_Id(id: Long): List<Answer> =
        findAll().filter { it.author.id!! == id }

    override fun findAllByAnswerTo_Id(questionId: Long): List<Answer> =
        mainRepository.findAllAnswers().filter { it.answerTo!!.id == questionId }

    override fun save(entity: Answer): Answer = mainRepository.saveAnswer(entity)

    override fun delete(entity: Answer) = mainRepository.deleteAnswer(entity)

    override fun findById(id: Long): Answer? = mainRepository.findAnswerById(id)

    override fun findAll(): List<Answer> = mainRepository.findAllAnswers()
}

class TagInMemoryRepository(private val mainRepository: InMemoryRepository) : TagRepository {

    override fun findAllByQuestions_Id(id: Long): List<Tag> =
        mainRepository.findAllTags().filter { tag -> id in tag.questions.map { it.id }}


    override fun findByTagName(name: String): Tag? = findAll().find { it.tagName == name }

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

class VoteInMemoryRepository(private val mainRepository: InMemoryRepository): VoteRepository{
    override fun save(vote: Vote): Vote =
        mainRepository.saveVote(vote)

    override fun delete(vote: Vote) =
        mainRepository.deleteVote(vote)

    override fun findAllByPost_Id(postId: Long): List<Vote> =
        mainRepository.findVotesByPostId(postId)

    override fun findAllByUser_Id(userId: Long): List<Vote> =
        mainRepository.findVotesByUserId(userId)

    override fun getScoreForPost(postId: Long): Int =
        mainRepository.getScoreForPost(postId)
}