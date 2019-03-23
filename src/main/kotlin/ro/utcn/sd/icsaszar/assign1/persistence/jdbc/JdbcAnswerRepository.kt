package ro.utcn.sd.icsaszar.assign1.persistence.jdbc

import org.springframework.jdbc.core.JdbcTemplate
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.persistence.api.AnswerRepository

class JdbcAnswerRepository(private val template: JdbcTemplate,
                           private val postRepository: JdbcPostRepository): AnswerRepository{

    override fun save(entity: Answer): Answer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(entity: Answer) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findById(id: Long): Answer? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAll(): List<Answer> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}