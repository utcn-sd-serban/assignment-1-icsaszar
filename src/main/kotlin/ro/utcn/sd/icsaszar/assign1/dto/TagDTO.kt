package ro.utcn.sd.icsaszar.assign1.dto

import ro.utcn.sd.icsaszar.assign1.model.post.Tag

data class TagDTO (
        val name: String,
        val id: Long
){
    companion object {
        fun fromTag(tag: Tag): TagDTO{
            return TagDTO(tag.tagName, tag.id!!)
        }
    }
}