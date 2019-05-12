package ro.utcn.sd.icsaszar.assign1.persistence.jdbc

import ro.utcn.sd.icsaszar.assign1.model.RawVoteData
import ro.utcn.sd.icsaszar.assign1.model.Vote
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Post
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.persistence.api.VoteRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcAnswerRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcQuestionRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcUserRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcVoteRepository

class JdbcEagerFetchVoteRepository(
        private val voteRepository: JdbcVoteRepository,
        private val answerRepository: JdbcAnswerRepository,
        private val questionRepository: JdbcQuestionRepository,
        private val userRepository: JdbcUserRepository
) :VoteRepository{
    override fun save(vote: Vote): Vote =
        voteRepository.save(vote)

    override fun delete(vote: Vote) =
        voteRepository.delete(vote)

    override fun deleteAll() = voteRepository.deleteAll()

    override fun findAllByPost_Id(postId: Long): List<Vote> =
        voteRepository.findAllByPost_Id(postId).mapNotNull {assembleVote(it)}

    override fun findAllByUser_Id(userId: Long): List<Vote> =
        voteRepository.findAllByUser_Id(userId).mapNotNull {assembleVote(it)}

    override fun getScoreForPost(postId: Long): Int =
        voteRepository.getScoreForPost(postId)

    override fun findAll(): List<Vote> =
       voteRepository.findAll().mapNotNull { assembleVote(it) }

    override fun findByIds(postId: Long, userId: Long): Vote?{
        val voteData = voteRepository.findByIds(postId, userId) ?: return null
        return assembleVote(voteData)
    }

    private fun assembleVote(voteData: RawVoteData): Vote?{
        val user = userRepository.findById(voteData.userId) ?: return null
        val questionData = questionRepository.findById(voteData.postId)
        val answerData = answerRepository.findById(voteData.postId)
        if((questionData == null) and (answerData == null))
            return null
        if((questionData != null) and (answerData != null))
            return null
        val post: Post = if(questionData != null) Question(questionData) else Answer(answerData!!)
        return Vote(post, user, voteData.vote)
    }
}