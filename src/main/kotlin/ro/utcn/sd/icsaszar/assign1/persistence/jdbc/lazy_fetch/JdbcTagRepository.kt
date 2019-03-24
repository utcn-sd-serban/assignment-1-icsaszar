package ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.persistence.api.TagRepository
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class JdbcTagRepository(private val template: JdbcTemplate) : TagRepository{

    override fun findAllByQuestions_Id(id: Long): MutableList<Tag> {
        val sql = """
        select *
        from
          tags join question_tag
           on tags.id = question_tag.tag_id
        where question_tag.question_id = ?
        """
        return template.query(sql, TagMapper(), id)
    }

    override fun findByTagName(name: String): Tag? {
        val sql = "select * from tags where tag_name = ?"
        return template.query(sql, TagMapper(), name).firstOrNull()
    }

    private fun insert(entity: Tag): Long{
        val sql: String = "insert into tags (tag_name) values (?)"
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        template.update ( { conn ->
            val ps: PreparedStatement =
                    conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, entity.tagName)
            ps

        },
        keyHolder)
        return keyHolder.key!!.toLong()
    }

    private fun update(id: Long, entity: Tag) {
        val sql = "update tags set tag_name = ? where tags.id = ?"
        template.update(sql, entity.tagName, id)
    }

    override fun save(entity: Tag): Tag {
        return if (entity.id == null) {
            entity.id = insert(entity)
            entity
        } else {
            update(entity.id!!, entity)
            entity
        }
    }

    override fun delete(entity: Tag) {
        val sql = "delete from tags where id = ?"
        template.update(sql, entity.id!!)
    }

    override fun findById(id: Long): Tag? {
        val sql = "select * from tags where id = ?"
        return template.query(sql, TagMapper(), id).firstOrNull()
    }

    override fun findAll(): List<Tag> {
        val sql = "select * from tags"
        return template.query(sql, TagMapper())
    }
}

class TagMapper : RowMapper<Tag>{
    override fun mapRow(rs: ResultSet, rowNum: Int): Tag? {
        return Tag(
            id = rs.getLong("id"),
            tagName = rs.getString("tag_name")
        )
    }
}