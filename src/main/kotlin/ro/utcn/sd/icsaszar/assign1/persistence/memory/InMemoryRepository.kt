package ro.utcn.sd.icsaszar.assign1.persistence.memory

import ro.utcn.sd.icsaszar.assign1.model.GenericEntity
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.Vote
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Post
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicLong

class InMemoryRepository {
    private val questionData: ConcurrentMap<Long, Question> = ConcurrentHashMap()
    private val answerData: ConcurrentMap<Long, Answer> = ConcurrentHashMap()
    private val userData: ConcurrentMap<Long, User> = ConcurrentHashMap()
    private val tagData: ConcurrentMap<Long, Tag> = ConcurrentHashMap()
    private val voteData: CopyOnWriteArrayList<Vote> = CopyOnWriteArrayList()
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

    private fun <T : GenericEntity>findAllVotes(data: MutableMap<Long,T>): List<T> =
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
        findAllVotes(questionData)


    fun saveAnswer(answer: Answer): Answer {
        if(answer.id == null)
            answer.id = currentAnswerId.getAndIncrement()

        answerData[answer.id!!] = answer
        questionData[answer.answerTo!!.id!!]?.addAnswer(answer)

        userData[answer.author.id]!!.posts.add(answer)
        return answer
    }

    fun deleteAnswer(answer: Answer) {
        answerData.remove(answer.id!!)
    }

    fun findAnswerById(id: Long): Answer? =
        answerData[id]

    fun findAllAnswers(): List<Answer> =
        answerData.values.toList()

    fun saveUser(user: User): User =
        save(user, currentUserId, userData)

    fun deleteUser(user: User) =
        delete(user, userData)

    fun findUserById(id: Long): User? =
        findById(id, userData)

    fun findAllUsers(): List<User> =
        findAllVotes(userData)

    fun saveTag(tag: Tag): Tag =
        save(tag, currentTagId, tagData)

    fun deleteTag(tag: Tag) =
        delete(tag, tagData)

    fun findTagById(id: Long): Tag? =
        findById(id, tagData)

    fun findAllTags(): List<Tag> =
        findAllVotes(tagData)

    fun saveVote(vote: Vote): Vote{
        if(vote !in voteData){
            voteData.add(vote)
            questionData[vote.post.id!!]?.addVote(vote)
            answerData[vote.post.id!!]?.addVote(vote)
            userData[vote.user.id!!]?.addVote(vote)
        }else{
            voteData.remove(vote)
            voteData.add(vote)
        }
        return vote
    }

    fun deleteVote(vote: Vote){
        voteData.remove(vote)
    }

    fun findAllVotes(): List<Vote> =
        voteData.toList()

    fun findVotesByUserId(userId: Long): List<Vote> =
        voteData.filter { it.user.id!! == userId }

    fun findVotesByPostId(postId: Long): List<Vote> =
        voteData.filter { it.post.id!! == postId }

    fun getScoreForPost(postId: Long): Int =
        voteData.filter {it.post.id!! == postId} .sumBy { it.vote.toInt() }

    fun findVoteByIds(userId: Long, postId: Long): Vote? =
            voteData.find { (it.user.id!! == userId) and (it.post.id!! == postId) }
}