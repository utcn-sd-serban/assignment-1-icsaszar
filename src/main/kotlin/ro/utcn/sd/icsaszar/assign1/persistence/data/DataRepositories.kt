package ro.utcn.sd.icsaszar.assign1.persistence.data

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.Repository
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.Vote
import ro.utcn.sd.icsaszar.assign1.model.VoteId
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.persistence.api.*

interface QuestionDataRepository : CrudRepository<Question, Long>, QuestionRepository {

}
interface AnswerDataRepository : CrudRepository<Answer, Long>, AnswerRepository {
    @Query(
            """select
            post.id as id,
            post.post_text as post_text,
            post.posted as posted,
            post.author_id as author_id,
            answer.question_id as question_id
        from
            post inner join answer
                            on post.id = answer.id
                 left join votes
                            on post.id = votes.post_id
        where
                answer.question_id  = ?
        group by
            post.id, answer.question_id
        order by
            coalesce(sum(votes.vote),0) desc""", nativeQuery = true)
    override fun findAllByPostIdOrderByScoreDesc(postId: Long): List<Answer>
}

interface TagDataRepository : CrudRepository<Tag, Long>, TagRepository {

}

interface UserDataRepository : CrudRepository<User, Long>, UserRepository {

}

interface VoteDataRepository: VoteDataRepositoryBase, VoteDataRepositoryCustom, VoteRepository{

}

interface VoteDataRepositoryBase: Repository<Vote, VoteId>{
    @Query("select sum(v.vote) from Vote v where v.post.id = ?1")
    fun getScoreForPost(postId: Long): Int

    fun save(vote: Vote): Vote

    fun delete(vote: Vote)

    fun findAllByPost_Id(postId: Long): List<Vote>

    fun findAllByUser_Id(userId: Long): List<Vote>

    fun findAll(): List<Vote>

    fun findByUser_IdAndPost_Id(postId: Long, userId: Long): Vote?
}

interface VoteDataRepositoryCustom{

}

class VoteDataRepositoryImpl(
        @Autowired
        private val voteDataRepositoryBase: VoteDataRepositoryBase){

    fun findByIds(postId: Long, userId: Long): Vote? = voteDataRepositoryBase.findByUser_IdAndPost_Id(postId, userId)
}