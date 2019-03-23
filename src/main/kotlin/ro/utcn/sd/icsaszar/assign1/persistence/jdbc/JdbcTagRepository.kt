package ro.utcn.sd.icsaszar.assign1.persistence.jdbc

import org.springframework.jdbc.core.JdbcTemplate
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.persistence.api.TagRepository

class JdbcTagRepository(private val template: JdbcTemplate) : TagRepository{
    override fun findByName(name: String): Tag? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun save(entity: Tag): Tag {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(entity: Tag) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findById(id: Long): Tag? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAll(): List<Tag> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}