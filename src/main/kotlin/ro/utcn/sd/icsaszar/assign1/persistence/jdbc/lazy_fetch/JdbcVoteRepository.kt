package ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import ro.utcn.sd.icsaszar.assign1.model.RawVoteData
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.Vote
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class JdbcVoteRepository(private val template: JdbcTemplate){

    private fun insert(entity: Vote){
        val sql: String = "insert into votes (user_id, post_id, vote) values (?,?,?)"
        template.update { conn ->
            val ps: PreparedStatement =
                    conn.prepareStatement(sql)
            ps.setLong(1, entity.user.id!!)
            ps.setLong(2,entity.post.id!!)
            ps.setShort(3,entity.vote)
            ps
        }
    }

    private fun update(entity: Vote) {
        val sql = "update votes set vote = ? where user_id = ? and post_id = ?"
        template.update(sql, entity.vote, entity.user.id!!, entity.post.id!!)
    }

    fun save(entity: Vote): Vote {
        return if(findById(entity.user.id!!, entity.post.id!!) == null){
            insert(entity)
            entity
        }
        else{
            update(entity)
            entity
        }
    }

    fun delete(entity: Vote) {
        val sql = "delete from votes where post_id = ? and user_id = ?"
        template.update(sql, entity.post.id!!, entity.user.id!!)
    }

    fun findAllByPost_Id(id: Long): List<RawVoteData> {
        val sql = "select * from votes where post_id = ?"
        return template.query(sql, VoteMapper(), id)
    }

    fun findById(userId: Long, postId: Long):RawVoteData?{
        val sql = "select * from votes where post_id = ? and user_id = ?"
        return template.query(sql, VoteMapper(), postId, userId).firstOrNull()
    }

    fun findAllByUser_Id(id: Long): List<RawVoteData> {
        val sql = "select * from votes where user_id = ?"
        return template.query(sql, VoteMapper(), id)
    }

    fun getScoreForPost(postId: Long): Int{
        val sql = "select coalesce(sum(vote),0) from votes where post_id = ?"
        return template.queryForObject(sql, Int::class.java, postId)
    }

    fun findAll(): List<RawVoteData>{
        val sql = "select * from votes"
        return template.query(sql, VoteMapper())
    }

    fun findByIds(postId: Long, userId: Long): RawVoteData?{
        val sql = "select * from votes where user_id = ? and post_id = ?"
        return template.query(sql, VoteMapper(), userId, postId).firstOrNull()
    }
}

class VoteMapper : RowMapper<RawVoteData>{
    override fun mapRow(rs: ResultSet, rowNum: Int): RawVoteData? {
        return RawVoteData(
                postId = rs.getLong("post_id"),
                userId = rs.getLong("user_id"),
                vote = rs.getShort("vote")
        )
    }
}