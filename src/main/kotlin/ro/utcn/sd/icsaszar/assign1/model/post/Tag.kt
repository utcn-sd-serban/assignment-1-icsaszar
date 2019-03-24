package ro.utcn.sd.icsaszar.assign1.model.post

import ro.utcn.sd.icsaszar.assign1.model.GenericEntity
import javax.persistence.*

@Entity(name = "tags")
data class Tag(
        var tagName: String = "",

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override var id: Long? = null

): GenericEntity {
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
}