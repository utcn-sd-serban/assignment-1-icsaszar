package ro.utcn.sd.icsaszar.assign1.model.post

import ro.utcn.sd.icsaszar.assign1.model.GenericEntity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

data class Tag(
        var name: String
): GenericEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override var id: Long? = 0
}