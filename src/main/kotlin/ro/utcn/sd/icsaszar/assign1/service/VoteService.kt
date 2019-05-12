package ro.utcn.sd.icsaszar.assign1.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.Vote
import ro.utcn.sd.icsaszar.assign1.model.post.Post
import ro.utcn.sd.icsaszar.assign1.persistence.api.RepositoryFactory

@Service
class VoteService(private val repositoryFactory: RepositoryFactory){
    @Transactional
    fun upVote(post: Post, user: User): Boolean{
        if(post.author.id!! == user.id!!) return false
        with(repositoryFactory.voteRepository){
            val oldVote = findByIds(post.id!!, user.id!!)
            if(oldVote != null)
                if (oldVote.vote.toInt() == 1)
                    return false
            save(Vote(post, user, 1))
            return true
        }
    }

    @Transactional
    fun downVote(post: Post, user: User): Boolean{
        if(post.author.id!! == user.id!!) return false
        with(repositoryFactory.voteRepository){
            val oldVote = findByIds(post.id!!, user.id!!)
            if(oldVote != null)
                if (oldVote.vote.toInt() == -1)
                    return false
            save(Vote(post, user, -1))
            return true
        }
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