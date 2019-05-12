package ro.utcn.sd.icsaszar.assign1.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ro.utcn.sd.icsaszar.assign1.dto.TagDTO
import ro.utcn.sd.icsaszar.assign1.service.TagService

@RestController
class TagController(
        private val tagService: TagService
) {
    @GetMapping("/tags")
    fun getAllTags(): List<TagDTO>{
        return tagService.listAllTags().map { it.toDTO() }
    }

    @PostMapping("/tags")   
    fun createNewTag(@RequestBody newTag: String): TagDTO{
        return tagService.createTag(newTag).toDTO()
    }
}