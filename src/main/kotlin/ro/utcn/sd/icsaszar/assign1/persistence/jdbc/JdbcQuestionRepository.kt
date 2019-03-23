package ro.utcn.sd.icsaszar.assign1.persistence.jdbc

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.persistence.api.GenericRepository

class JdbcQuestionRepository(private val template: JdbcTemplate) : GenericRepository<Question>{
    override fun save(entity: Question): Question {
        fun insert(entity: Question):Long{

            val insert: SimpleJdbcInsert = SimpleJdbcInsert(template)
            insert.apply {
                tableName = "question"
                usingGeneratedKeyColumns("id")
            }

            val map: MutableMap<String, Any> = HashMap()
            entity.let {
                map.apply {
                    put("post_text", it.postText)
                    put("posted",it.posted)
                    put("id_author",it.author.id!!)
                }
            }

            return insert.executeAndReturnKey(map).toLong()
        }

        return if (entity.id == null) {
            entity.id = insert(entity)
            entity
        } else {
            update(entity.id!!, entity)
        }

    }

    private fun update(id: Long, entity: Question): Question {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(entity: Question) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findById(id: Long): Question? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAll(): List<Question> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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