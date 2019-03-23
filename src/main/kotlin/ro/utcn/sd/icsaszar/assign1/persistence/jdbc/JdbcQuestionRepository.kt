package ro.utcn.sd.icsaszar.assign1.persistence.jdbc

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.persistence.api.GenericRepository
import ro.utcn.sd.icsaszar.assign1.persistence.api.QuestionRepository
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp

class QuestionMapper() : RowMapper<Question>{
    override fun mapRow(rs: ResultSet, rowNum: Int): Question? {
        return Question(
            text = rs.getString("text"),
            posted = rs.getTimestamp("posted").toLocalDateTime(),
            title = rs.getString("title")
        )
    }
}

class JdbcQuestionRepository (
        private val template: JdbcTemplate,
        private val postRepository: JdbcPostRepository
) : QuestionRepository{
    override fun findAllByTags(tag: Tag): List<Question> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAllByTitleContainsIgnoreCase(text: String): List<Question> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAllByOrderByPostedDesc(): List<Question> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun save(entity: Question): Question {
        return if (entity.id == null) {
            entity.id = postRepository.insert(entity)
            insert(entity)
            entity
        } else {
            postRepository.update(entity.id!!, entity)
            update(entity.id!!, entity)
            entity
        }
    }

    private fun update(id: Long, entity: Question) {
        val sql = "update question set title = ?"
        template.update(sql, entity.title)
    }

    override fun delete(entity: Question) {
        val sql = "delete from question where id = ?"
        template.update(sql, entity.id!!)
        postRepository.delete(entity)
    }


    override fun findById(id: Long): Question? {
        val sql = """
        select
           post.post_text, post.posted, question.title
        from
           post inner join question
           on post.id = question.id
        where
           post.id = ?
        """
        return template.query(sql, QuestionMapper(), id).firstOrNull()
    }

    override fun findAll(): List<Question> {
        val sql = """
        select
           post.post_text, post.posted, question.title
        from
           post inner join question
           on post.id = question.id
        where
           post.id = ?
        """
        return template.query(sql, QuestionMapper())
    }
}

/*
* @Override
	public List<Student> findAll() {
		return template.query("SELECT * FROM student", new StudentMapper());
	}

	@Override
	public Student save(Student student) {
		if (student.getId() == null) {
			student.setId(insert(student));
		} else {
			update(student);
		}
		return student;
	}

	@Override
	public void remove(Student student) {
		template.update("DELETE FROM student WHERE id = ?", student.getId());
	}

	@Override
	public Optional<Student> findById(int id) {
		List<Student> students = template.query("SELECT * FROM student WHERE id = ?", new StudentMapper(), id);
		return students.isEmpty() ? Optional.empty() : Optional.of(students.get(0));
	}

	private int insert(Student student) {
		// we use the SimpleJdbcInsert to easily retrieve the generated ID
		SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
		insert.setTableName("student");
		insert.usingGeneratedKeyColumns("id");
		Map<String, Object> map = new HashMap<>();
		map.put("first_name", student.getFirstName());
		map.put("last_name", student.getLastName());
		map.put("email_address", student.getEmailAddress());
		return insert.executeAndReturnKey(map).intValue();
	}

	private void update(Student student) {
		template.update("UPDATE student SET first_name = ?, last_name = ?, email_address = ? WHERE id = ?",
				student.getFirstName(), student.getLastName(), student.getEmailAddress(), student.getId());
	}
* */