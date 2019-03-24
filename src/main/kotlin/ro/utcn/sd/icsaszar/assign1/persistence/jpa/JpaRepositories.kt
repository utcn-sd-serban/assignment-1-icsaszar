package ro.utcn.sd.icsaszar.assign1.persistence.jpa

import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.persistence.api.AnswerRepository
import ro.utcn.sd.icsaszar.assign1.persistence.api.QuestionRepository
import ro.utcn.sd.icsaszar.assign1.persistence.api.TagRepository
import ro.utcn.sd.icsaszar.assign1.persistence.api.UserRepository
import javax.persistence.EntityManager
import javax.persistence.NoResultException

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