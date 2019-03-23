package ro.utcn.sd.icsaszar.assign1.persistence.jdbc

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Post
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.LocalDateTime

class JdbcPostRepository(private val template: JdbcTemplate) {
    fun insert(entity: Post): Long {
        val sql: String = "insert into post (post_text, posted, author_id) values (?, ?, ?)"
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        template.update ( { conn ->
            val ps: PreparedStatement =
                    conn.prepareStatement(sql)
            ps.setString(1, entity.postText)
            ps.setTimestamp(2, Timestamp.valueOf(entity.posted))
            ps.setLong(3, entity.author.id!!)
            ps

        },
            keyHolder)
        return keyHolder.key!!.toLong()
    }

    fun update(id: Long, entity: Post) {
        val sql: String = "update post set post_text = ?, posted = ?, author_id = ?"
        template.update(sql, entity.postText, Timestamp.valueOf(entity.posted), entity.author.id!!)
    }

    fun delete(entity: Post) {
        val sql: String = "delete from post where id = ?"
        template.update(sql, entity.id!!)
    }
}