package ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.RawAnswerData
import java.sql.PreparedStatement
import java.sql.ResultSet

class JdbcAnswerRepository(private val template: JdbcTemplate,
                           private val postRepository: JdbcPostRepository){

    private fun insert(entity: Answer){
        val sql = "insert into answer (id, question_id) values (?,?)"
        template.update { conn ->
            val ps: PreparedStatement =
                    conn.prepareStatement(sql)
            ps.setLong(1, entity.id!!)
            ps.setLong(2, entity.answerTo!!.id!!)
            ps
        }
    }

    fun save(entity: Answer): Answer {
        return if (entity.id == null) {
            entity.id = postRepository.insert(entity)
            insert(entity)
            entity
        } else {
            postRepository.update(entity.id!!, entity)
            entity
        }
    }

    fun delete(entity: Answer) {
        val sql = "delete from answer where id = ?"
        template.update(sql, entity.id!!)
        postRepository.delete(entity)
    }


    fun findById(id: Long): RawAnswerData? {
        val sql = """
        select
          post.id as id,
          post.post_text as post_text,
          post.posted as posted,
          post.author_id as author_id,
          answer.question_id as question_id
        from
           post inner join answer
           on post.id = answer.id
        where
           post.id = ?
        """
        return template.query(sql, RawAnswerMapper(), id).firstOrNull()
    }

    fun findAll(): List<RawAnswerData> {
        val sql = """
        select
          post.id as id,
          post.post_text as post_text,
          post.posted as posted,
          post.author_id as author_id,
          answer.question_id as question_id
        from
           post inner join answer
           on post.id = answer.id
        """
        return template.query(sql, RawAnswerMapper())
    }

    fun findAllByAnswerTo_Id(questionId: Long): List<RawAnswerData> {
        val sql = """
        select
          post.id as id,
          post.post_text as post_text,
          post.posted as posted,
          post.author_id as author_id,
          answer.question_id as question_id
        from
           post inner join answer
           on post.id = answer.id
        where
           answer.question_id = ?
        """
        return template.query(sql, RawAnswerMapper(), questionId)
    }

    fun findAllByAuthor_Id(id: Long): List<RawAnswerData> {
        val sql = """
        select
          post.id as id,
          post.post_text as post_text,
          post.posted as posted,
          post.author_id as author_id,
          answer.question_id as question_id
        from
          post inner join answer
           on post.id = answer.id
        where
          post.author_id = ?
        """
        return template.query(sql, RawAnswerMapper(), id)
    }
}

class RawAnswerMapper : RowMapper<RawAnswerData>{
    override fun mapRow(rs: ResultSet, rowNum: Int): RawAnswerData? {
        return RawAnswerData(
                authorId = rs.getLong("author_id"),
                id = rs.getLong("id"),
                text = rs.getString("post_text"),
                posted = rs.getTimestamp("posted").toLocalDateTime(),
                questionId = rs.getLong("question_id")
        )
    }
}