package ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.RawQuestionData
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import java.sql.PreparedStatement
import java.sql.ResultSet


class JdbcQuestionRepository (
        private val template: JdbcTemplate,
        private val postRepository: JdbcPostRepository
){

    fun findAllByAuthor_Id(id: Long): List<RawQuestionData> {
        val sql = """
        select
          post.id as id,
          post.post_text as post_text,
          post.posted as posted,
          post.author_id as author_id,
          question.title as title
        from
          post
            inner join question on post.id = question.id
        where
          post.author_id = ?
        """
        return template.query(sql, RawQuestionMapper(), id)
    }

    fun findAllByTags(tag: Tag): List<RawQuestionData> {
        val sql = """
        select
          post.id as id,
          post.post_text as post_text,
          post.posted as posted,
          post.author_id as author_id,
          question.title as title
        from
          post
            inner join question on post.id = question.id
            inner join question_tag on question.id = question_tag.question_id
        where
          question_tag.tag_id = ?
        """
        return template.query(sql, RawQuestionMapper(), tag.id!!)
    }

    fun findAllByTitleContainsIgnoreCase(text: String): List<RawQuestionData> {
        val sql = """
        select
          post.id as id,
          post.post_text as post_text,
          post.posted as posted,
          post.author_id as author_id,
          question.title as title
        from
          post
            inner join question on post.id = question.id
        where
          lower(question.title) like ?
        """
        val param = "%${text.toLowerCase()}%"
        return template.query(sql, RawQuestionMapper(), param)
    }

    fun findAllByOrderByPostedDesc(): List<RawQuestionData> {
        val sql = """
        select
          post.id as id,
          post.post_text as post_text,
          post.posted as posted,
          post.author_id as author_id,
          question.title as title
        from
          post
            inner join question on post.id = question.id
        order by post.posted desc
        """
        return template.query(sql, RawQuestionMapper())
    }

    private fun insert(entity: Question){
        val sql = "insert into question (id, title) values (?, ?)"
        template.update { conn ->
                val ps: PreparedStatement =
                        conn.prepareStatement(sql)
                ps.setLong(1, entity.id!!)
                ps.setString(2, entity.title)
                ps
        }
    }

    private fun insertTag(questionId: Long, tagId: Long){
        val sql = "insert into question_tag (question_id, tag_id) values (?, ?)"
        template.update { conn ->
            val ps: PreparedStatement =
                    conn.prepareStatement(sql)
            ps.setLong(1, questionId)
            ps.setLong(2, tagId)
            ps
        }
    }

    fun save(entity: Question): Question {
        return if (entity.id == null) {
            entity.id = postRepository.insert(entity)
            insert(entity)
            entity.tags.forEach { insertTag(entity.id!!, it.id!!)}
            entity
        } else {
            postRepository.update(entity.id!!, entity)
            update(entity.id!!, entity)
            entity
        }
    }

    private fun update(id: Long, entity: Question) {
        val sql = "update question set title = ? where question.id = ?"
        template.update(sql, entity.title, id)
    }

    fun delete(entity: Question) {
        val sql = "delete from question where id = ?"
        template.update(sql, entity.id!!)
        postRepository.delete(entity)
    }


    fun findById(id: Long): RawQuestionData? {
        val sql = """
        select
           post.id as id,
           post.post_text as post_text,
           post.posted as posted,
           post.author_id as author_id,
           question.title as title
        from
           post inner join question
           on post.id = question.id
        where
           post.id = ?
        """
        return template.query(sql, RawQuestionMapper(), id).firstOrNull()
    }

    fun findAll(): List<RawQuestionData> {
        val sql = """
        select
           post.id as id,
           post.post_text as post_text,
           post.posted as posted,
           post.author_id as author_id,
           question.title as title
        from
           post inner join question
           on post.id = question.id
        """
        return template.query(sql, RawQuestionMapper())
    }
}

class RawQuestionMapper : RowMapper<RawQuestionData>{
    override fun mapRow(rs: ResultSet, rowNum: Int): RawQuestionData? {
        return RawQuestionData(
                authorId = rs.getLong("author_id"),
                id = rs.getLong("id"),
                text = rs.getString("post_text"),
                posted = rs.getTimestamp("posted").toLocalDateTime(),
                title = rs.getString("title")
        )
    }
}