package ro.utcn.sd.icsaszar.assign1.model.post

import ro.utcn.sd.icsaszar.assign1.model.GenericEntity
import javax.persistence.*

@Entity
data class Tag(
        var name: String = ""
): GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null

    @ManyToMany(mappedBy = "tags")
    var questions: MutableList<Question> = mutableListOf()
}