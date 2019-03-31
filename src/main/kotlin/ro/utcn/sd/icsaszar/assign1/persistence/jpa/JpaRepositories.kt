package ro.utcn.sd.icsaszar.assign1.persistence.jpa

import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.Vote
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.persistence.api.*
import javax.persistence.EntityManager
import javax.persistence.NoResultException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery

class QuestionJpaRepository(entityManager: EntityManager):
        GenericJpaRepository<Question>(entityManager, Question::class.java), QuestionRepository{
    override fun findAllByTags(tag: Tag): List<Question> =
        entityManager.createQuery("select q from Question q join q.tags t where t.id = ?1", entityClass)
                .setParameter(1,tag.id)
                .resultList

    override fun findAllByTitleContainsIgnoreCase(text: String): List<Question> =
        entityManager.createQuery("select q from Question q where lower(q.title) like ?1", entityClass)
                .setParameter(1, "%${text.toLowerCase()}%")
                .resultList


    override fun findAllByOrderByPostedDesc(): List<Question> =
        entityManager.createQuery("select q from Question q order by q.posted desc", entityClass)
                .resultList

    override fun findAllByAuthor_Id(id: Long): List<Question> =
        entityManager.createQuery("select q from Question q where q.author.id = ?1", entityClass)
                .setParameter(1, id)
                .resultList
}

class AnswerJpaRepository(entityManager: EntityManager):
        GenericJpaRepository<Answer>(entityManager, Answer::class.java), AnswerRepository{

    override fun findAllByAnswerTo_Id(questionId: Long): List<Answer> =
            entityManager.createQuery("select a from Answer a where a.answerTo.id = ?1", entityClass)
                    .setParameter(1, questionId)
                    .resultList

    override fun findAllByAuthor_Id(id: Long): List<Answer> =
            entityManager.createQuery("select a from Answer a where a.author.id = ?1", entityClass)
                    .setParameter(1, id)
                    .resultList
}

class UserJpaRepository(entityManager: EntityManager):
        GenericJpaRepository<User>(entityManager, User::class.java), UserRepository{
    override fun findByUserName(userName: String): User? =
            try{
                entityManager.createQuery("select u from User u where u.userName = ?1", entityClass)
                        .setParameter(1, userName)
                        .singleResult
            }catch (e: NoResultException){
                null
            }
}

class TagJpaRepository(entityManager: EntityManager):
        GenericJpaRepository<Tag>(entityManager, Tag::class.java), TagRepository {
    override fun findByTagName(name: String): Tag? =
            try{
                entityManager.createQuery("select t from tags t where t.tagName = ?1", entityClass)
                        .setParameter(1, name)
                        .singleResult
            }catch (e: NoResultException){
                null
            }

    override fun findAllByQuestions_Id(id: Long): List<Tag> =
            entityManager.createQuery("select t from tags t join t.questions q where q.id = ?1", entityClass)
                    .setParameter(1, id)
                    .resultList
}

class VoteJpaRepository(private val entityManager: EntityManager): VoteRepository {
    private fun find(vote: Vote): Vote? =
        try{
            entityManager.createQuery("select v from Vote v where v.user.id = ?1 and v.post.id = ?2", Vote::class.java)
                    .setParameter(1, vote.user.id!!)
                    .setParameter(2, vote.post.id)
                    .singleResult
        }
        catch (e: NoResultException){
            null
        }

    override fun save(vote: Vote): Vote =
        if(find(vote) == null){
            entityManager.persist(vote)
            vote
        }else{
            entityManager.merge(vote)
        }

    override fun delete(vote: Vote) {
        if(entityManager.contains(vote))
            entityManager.remove(vote)
        else{
            val toDelete = entityManager.merge(vote)
            entityManager.remove(toDelete)
        }
    }

    override fun findAllByPost_Id(postId: Long): List<Vote> =
        entityManager.createQuery("select v from Vote v where v.post.id = ?1", Vote::class.java)
                .setParameter(1, postId)
                .resultList


    override fun findAllByUser_Id(userId: Long): List<Vote> =
        entityManager.createQuery("select v from Vote v where v.user.id = ?1", Vote::class.java)
                .setParameter(1, userId)
                .resultList

    override fun getScoreForPost(postId: Long): Int =
        entityManager.createQuery("select sum(v.vote) form Vote v where v.post.id = ?1", Int::class.java)
                .setParameter(1, postId)
                .singleResult

    override fun findAll(): List<Vote> {
        val builder: CriteriaBuilder = entityManager.criteriaBuilder
        val query: CriteriaQuery<Vote> = builder.createQuery(Vote::class.java)
        query.select(query.from(Vote::class.java))
        return entityManager.createQuery(query).resultList
    }

    override fun findByIds(postId: Long, userId: Long): Vote? =
            try {
                entityManager.createQuery("select v from Vote v where v.user.id = ?1 and v.post.id = ?2", Vote::class.java)
                        .setParameter(1, userId)
                        .setParameter(2, postId)
                        .singleResult
            }catch (e: NoResultException){
                null
            }
}