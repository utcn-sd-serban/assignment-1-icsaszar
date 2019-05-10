package ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.persistence.api.UserRepository
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class JdbcUserRepository(private val template: JdbcTemplate){

    fun findByUserName(userName: String): User? {
        val sql = "select * from users where users.user_name = ?"
        return template.query(sql, UserMapper(), userName).firstOrNull()
    }

    private fun insert(entity: User): Long{
        val sql: String = "insert into users (user_name, is_mod, is_banned) values (?,?,?)"
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        template.update ( { conn ->
            val ps: PreparedStatement =
                    conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, entity.userName)
            ps.setBoolean(2, entity.isMod)
            ps.setBoolean(3, entity.isBanned)
            ps

        },
                keyHolder)
        return keyHolder.key!!.toLong()
    }

    private fun update(id: Long, entity: User) {
        val sql = "update users set user_name = ? where users.id = ?"
        template.update(sql, entity.userName, id)
    }

    fun save(entity: User): User {
        return if (entity.id == null) {
            entity.id = insert(entity)
            entity
        } else {
            update(entity.id!!, entity)
            entity
        }
    }

    fun delete(entity: User) {
        val sql = "delete from users where id = ?"
        template.update(sql, entity.id!!)
    }

    fun findById(id: Long): User? {
        val sql = "select * from users where id = ?"
        return template.query(sql, UserMapper(), id).firstOrNull()
    }

    fun findAll(): List<User> {
        val sql = "select * from users"
        return template.query(sql, UserMapper())
    }
}

class UserMapper : RowMapper<User> {
    override fun mapRow(rs: ResultSet, rowNum: Int): User? {
        return User(
                rs.getString("user_name"),
                rs.getLong("id"),
                isMod =  rs.getBoolean("is_mod"),
                isBanned = rs.getBoolean("is_banned")
                )
    }
}
