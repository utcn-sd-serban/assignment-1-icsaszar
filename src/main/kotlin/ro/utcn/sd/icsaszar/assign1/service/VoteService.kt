package ro.utcn.sd.icsaszar.assign1.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ro.utcn.sd.icsaszar.assign1.event.NewQuestionEvent
import ro.utcn.sd.icsaszar.assign1.event.NewVoteEvent
import ro.utcn.sd.icsaszar.assign1.exception.VotingException
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.Vote
import ro.utcn.sd.icsaszar.assign1.model.post.Post
import ro.utcn.sd.icsaszar.assign1.persistence.api.RepositoryFactory

@Service
class VoteService(
        private val repositoryFactory: RepositoryFactory,
        private val eventPublisher: ApplicationEventPublisher){
    @Transactional
    fun upVote(post: Post, user: User): Vote{
        if(post.author.id!! == user.id!!)
            throw VotingException.votingOnOwnPost
        with(repositoryFactory.voteRepository){
            return save(Vote(post, user, 1))
        }
    }

    @Transactional
    fun downVote(post: Post, user: User): Vote{
        if(post.author.id!! == user.id!!)
            throw VotingException.votingOnOwnPost
        with(repositoryFactory.voteRepository){
            return save(Vote(post, user, -1))
        }
    }

    @Transactional
    fun vote(post: Post, direction: Short, user: User): Vote {

        val result = when {
            direction.compareTo(1) == 0 -> upVote(post, user)
            direction.compareTo(-1) == 0 -> downVote(post, user)
            else -> throw Exception("Invalid Parameter")
        }
        eventPublisher.publishEvent(NewVoteEvent(result.toDTO()))
        return result
    }

    @Transactional
    fun removeVote(post: Post, user: User): Boolean {
        with(repositoryFactory.voteRepository){
            val foundVote = findByIds(post.id!!, user.id!!) ?: return false
            delete(foundVote)
            return true
        }
    }

    @Transactional
    fun getPostScore(post: Post): Int{
        with(repositoryFactory.voteRepository){
            return getScoreForPost(post.id!!)
        }
    }

    @Transactional
    fun getVoteByUser(userId: Long): List<Vote>{
        with(repositoryFactory.voteRepository){
            return findAllByUser_Id(userId)
        }
    }
}