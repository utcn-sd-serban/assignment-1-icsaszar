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

class QuestionJpaRepository(entityManager: EntityManager):
        GenericJpaRepository<Question>(entityManager, Question::class.java), QuestionRepository{
    override fun findAllByTags(tag: Tag): List<Question> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAllByTitleContainsIgnoreCase(text: String): List<Question> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAllByOrderByPostedDesc(): List<Question> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAllByAuthor_Id(id: Long): List<Question> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class AnswerJpaRepository(entityManager: EntityManager):
        GenericJpaRepository<Answer>(entityManager, Answer::class.java), AnswerRepository{

    override fun findAllByAnswerTo_Id(questionId: Long): List<Answer> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAllByAuthor_Id(id: Long): List<Answer> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class UserJpaRepository(entityManager: EntityManager):
        GenericJpaRepository<User>(entityManager, User::class.java), UserRepository{
    override fun findByUserName(userName: String): User? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class TagJpaRepository(entityManager: EntityManager):
        GenericJpaRepository<Tag>(entityManager, Tag::class.java), TagRepository{
    override fun findByTagName(name: String): Tag? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAllByQuestions_Id(id: Long): MutableList<Tag> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}