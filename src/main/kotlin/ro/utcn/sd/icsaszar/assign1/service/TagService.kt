package ro.utcn.sd.icsaszar.assign1.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.persistence.api.RepositoryFactory

@Service
class TagService(private val repositoryFactory: RepositoryFactory){

    @Transactional
    fun listAllTags(): List<Tag> = repositoryFactory.tagRepository.findAll()

    @Transactional
    fun findTagByName(name: String): Tag? = repositoryFactory.tagRepository.findByTagName(name.toLowerCase())

    @Transactional
    fun createTag(name: String): Tag = repositoryFactory.tagRepository.save(Tag(name.toLowerCase()))
}