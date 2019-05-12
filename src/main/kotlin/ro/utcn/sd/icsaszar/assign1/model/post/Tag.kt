package ro.utcn.sd.icsaszar.assign1.model.post

import ro.utcn.sd.icsaszar.assign1.dto.ConvertibleToDTO
import ro.utcn.sd.icsaszar.assign1.dto.TagDTO
import ro.utcn.sd.icsaszar.assign1.model.GenericEntity
import javax.persistence.*

@Entity(name = "tags")
data class Tag(
        var tagName: String = "",

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override var id: Long? = null

): GenericEntity, ConvertibleToDTO<TagDTO> {
        @ManyToMany(mappedBy = "tags", cascade = [CascadeType.MERGE], fetch = FetchType.EAGER)
        var questions: MutableList<Question> = mutableListOf()

    fun addQuestion(question: Question): Tag{
        question.addTag(this)
        questions.add(question)
        return this
    }

    fun addQuestions(questions: List<Question>): Tag{
        questions.forEach { addQuestion(it) }
        return this
    }

    override fun hashCode(): Int {
        return 31
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Tag) return false

        return id?.equals(other.id) ?: false
    }

    override fun toDTO(): TagDTO {
        return TagDTO.fromTag(this)
    }
}