package ro.utcn.sd.icsaszar.assign1.persistence.api

import ro.utcn.sd.icsaszar.assign1.model.GenericEntity

interface GenericRepository<T : GenericEntity> {

    fun save(entity: T): T
    fun delete(entity: T)
    fun findById(id: Long): T?
    fun findAll(): List<T>
    fun deleteAll()
}