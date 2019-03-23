package ro.utcn.sd.icsaszar.assign1.persistence.memory

import ro.utcn.sd.icsaszar.assign1.model.GenericEntity
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import java.util.concurrent.atomic.AtomicLong

class InMemoryRepository {
    private val questionData: MutableMap<Long, Question> = mutableMapOf()
    private val userData: MutableMap<Long, User> = mutableMapOf()
    private val tagData: MutableMap<Long, Tag> = mutableMapOf()
    private val currentQuestionId: AtomicLong = AtomicLong(0)
    private val currentUserId: AtomicLong = AtomicLong(0)
    private val currentAnswerId: AtomicLong = AtomicLong(0)
    private val currentTagId: AtomicLong = AtomicLong(0)

    private fun <T : GenericEntity>save(entity: T, ac: AtomicLong, data: MutableMap<Long,T>): T{
        if(entity.id == null){
            entity.id = ac.getAndIncrement()
        }

        data[entity.id!!] = entity
        return entity
    }

    private fun <T : GenericEntity>delete(entity: T, data: MutableMap<Long,T>){
        data.remove(entity.id!!)
    }

    private fun <T : GenericEntity>findById(id: Long, data: MutableMap<Long,T>): T? =
        data[id]

    private fun <T : GenericEntity>findAll(data: MutableMap<Long,T>): List<T> =
        data.values.toList()


    fun saveQuestion(question: Question): Question{
        val savedQ = save(question, currentQuestionId, questionData)

        for (tag in tagData.values){
            if(tag in question.tags)
                tag.questions.add(question)
        }

        userData[question.author.id]!!.posts.add(question)

        return savedQ
    }

    fun deleteQuestion(question: Question) =
        delete(question, questionData)

    fun findQuestionById(id: Long): Question? =
        findById(id, questionData)

    fun findAllQuestions(): List<Question> =
        findAll(questionData)

    fun saveAnswer(answer: Answer): Answer {
        if(answer.id == null)
            answer.id = currentAnswerId.getAndIncrement()

        questionData[answer.answerTo.id]!!.answers.add(answer)

        userData[answer.author.id]!!.posts.add(answer)
        return answer
    }

    fun deleteAnswer(answer: Answer) {
        questionData[answer.answerTo.id]!!.answers.remove(answer)
    }

    fun findAnswerById(id: Long): Answer? =
        questionData.values.flatMap { q -> q.answers }.first { it.id == id }

    fun findAllAnswers(): List<Answer> =
        questionData.values.flatMap { q -> q.answers }

    fun saveUser(user: User): User =
        save(user, currentUserId, userData)

    fun deleteUser(user: User) =
        delete(user, userData)

    fun findUserById(id: Long): User? =
        findById(id, userData)

    fun findAllUsers(): List<User> =
        findAll(userData)

    fun saveTag(tag: Tag): Tag =
        save(tag, currentTagId, tagData)

    fun deleteTag(tag: Tag) =
        delete(tag, tagData)

    fun findTagById(id: Long): Tag? =
        findById(id, tagData)

    fun findAllTags(): List<Tag> =
        findAll(tagData)
}