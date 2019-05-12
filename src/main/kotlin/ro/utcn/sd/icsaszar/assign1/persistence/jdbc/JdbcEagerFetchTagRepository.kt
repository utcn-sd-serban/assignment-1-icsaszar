package ro.utcn.sd.icsaszar.assign1.persistence.jdbc

import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.persistence.api.TagRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcAnswerRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcPostRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcQuestionRepository
import ro.utcn.sd.icsaszar.assign1.persistence.jdbc.lazy_fetch.JdbcTagRepository

class JdbcEagerFetchTagRepository(
        private val tagRepository: JdbcTagRepository,
        private val questionRepository: JdbcQuestionRepository
) : TagRepository{
    override fun findByTagName(name: String): Tag? {
        val tag = tagRepository.findByTagName(name) ?: return null
        return assembleTag(tag)
    }

    override fun findAllByQuestions_Id(id: Long): List<Tag> {
        val tags = tagRepository.findAllByQuestions_Id(id)
        return tags.map { assembleTag(it) }
    }

    override fun save(entity: Tag): Tag =
        tagRepository.save(entity)

    override fun delete(entity: Tag) =
        tagRepository.delete(entity)

    override fun deleteAll() = tagRepository.deleteAll()

    override fun findById(id: Long): Tag? {
        val tag = tagRepository.findById(id) ?: return null
        return assembleTag(tag)
    }

    override fun findAll(): List<Tag> {
        val tags = tagRepository.findAll()
        return tags.map {assembleTag(it)}
    }

    private fun assembleTag(tag: Tag): Tag {
        questionRepository.findAllByTags(tag).forEach {
            val q = Question(it)
            tag.addQuestion(q)
        }
        return tag
    }
}