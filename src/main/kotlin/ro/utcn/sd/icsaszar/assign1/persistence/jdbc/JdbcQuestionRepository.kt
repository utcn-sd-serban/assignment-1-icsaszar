package ro.utcn.sd.icsaszar.assign1.persistence.jdbc

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import ro.utcn.sd.icsaszar.assign1.model.Question
import ro.utcn.sd.icsaszar.assign1.persistence.QuestionRepository
import java.sql.ResultSet


class JdbcQuestionRepository(private val jdbcTemplate: JdbcTemplate): QuestionRepository{
    override fun save(question: Question): Question {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findById(id: Int): Question? {
        val questions: List<Question> = jdbcTemplate.query(
                "SELECT * FROM question WHERE id = ?",
                { resultSet: ResultSet, _ ->
                    Question(resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getString("text") )},
                arrayOf(id))
        return questions.firstOrNull()
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(question: Question) {
        return jdbcTemplate.query("DELETE FROM question WHERE id = ?", arrayOf(question.id), {_ -> Unit})
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listAll(): List<Question> {
        return jdbcTemplate.query("SELECT * FROM question")
            {resultSet: ResultSet, _ ->
                    Question(resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getString("text") )}
    }

    fun update(question: Question): Int{
        val simpleInsert : SimpleJdbcInsert = SimpleJdbcInsert(jdbcTemplate)
        simpleInsert.tableName = "question"
        simpleInsert.setGeneratedKeyName("id")

        val data: MutableMap<String, Any> = HashMap()

        data["title"] = question.title
        data["text"] = question.text

        return simpleInsert.executeAndReturnKey(data).toInt()

    }

    fun insert(question: Question): Int{

    }
}