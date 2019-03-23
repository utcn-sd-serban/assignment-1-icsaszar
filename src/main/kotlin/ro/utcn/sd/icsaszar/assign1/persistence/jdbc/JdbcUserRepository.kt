package ro.utcn.sd.icsaszar.assign1.persistence.jdbc

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.persistence.api.GenericRepository
import ro.utcn.sd.icsaszar.assign1.persistence.api.UserRepository
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp

class JdbcUserRepository(private val template: JdbcTemplate) : UserRepository{

    override fun findByUserName(userName: String): User? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun insert(entity: User): Long{
        val sql: String = "insert into users (user_name) values (?)"
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        template.update ( { conn ->
            val ps: PreparedStatement =
                    conn.prepareStatement(sql)
            ps.setString(1, entity.userName)
            ps

        },
                keyHolder)
        return keyHolder.key!!.toLong()
    }

    private fun update(id: Long, entity: User) {
        val sql = "update users set user_name = ?"
        template.update(sql, entity.userName)
    }

    override fun save(entity: User): User {
        return if (entity.id == null) {
            entity.id = insert(entity)
            entity
        } else {
            update(entity.id!!, entity)
            entity
        }
    }

    override fun delete(entity: User) {
        val sql = "delete from user where id = ?"
        template.update(sql, entity.id!!)
    }

    override fun findById(id: Long): User? {
        val sql = "select * from user where id = ?"
        return template.query(sql, UserMapper(), id).firstOrNull()
    }

    override fun findAll(): List<User> {
        val sql = "select * from user"
        return template.query(sql, UserMapper())
    }
}

class UserMapper : RowMapper<User> {
    override fun mapRow(rs: ResultSet, rowNum: Int): User? {
        return User(rs.getString("user_name"))
    }
}
