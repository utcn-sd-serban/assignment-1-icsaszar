package ro.utcn.sd.icsaszar.assign1.persistence.api

import ro.utcn.sd.icsaszar.assign1.model.GenericEntity

interface GenericRepositoryFactory<T : GenericEntity> {
    fun createRepository() : GenericRepository<T>
}