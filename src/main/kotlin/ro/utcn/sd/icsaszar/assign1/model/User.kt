package ro.utcn.sd.icsaszar.assign1.model

import javax.persistence.*

@Entity
data class User(
        @Column
        var name: String
) : GenericEntity{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override var id: Long? = null
}