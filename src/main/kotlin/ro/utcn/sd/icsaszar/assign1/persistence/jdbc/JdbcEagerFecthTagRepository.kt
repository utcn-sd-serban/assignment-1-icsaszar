package ro.utcn.sd.icsaszar.assign1.persistence.jdbc

import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.persistence.api.TagRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcAnswerRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcPostRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcQuestionRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcTagRepository

class JdbcEagerFecthTagRepository(
        private val tagRepository: JdbcTagRepository,
        private val questionRepository: JdbcQuestionRepository
) : TagRepository{
    override fun findByTagName(name: String): Tag? {
        val tag = tagRepository.findByTagName(name) ?: return null
        tag.questions = questionRepository.findAllByTags(tag).map {Question(it)}.toMutableList()
        return tag
    }

    override fun findAllByQuestions_Id(id: Long): List<Tag> {
        val tags = tagRepository.findAllByQuestions_Id(id)
        return tags.map {tag ->
            tag.questions = questionRepository.findAllByTags(tag).map { Question(it) }.toMutableList()
            tag
        }
    }

    override fun save(entity: Tag): Tag =
        tagRepository.save(entity)

    override fun delete(entity: Tag) =
        tagRepository.delete(entity)

    override fun findById(id: Long): Tag? =
        tagRepository.findById(id)

    override fun findAll(): List<Tag> =
        tagRepository.findAll()

}