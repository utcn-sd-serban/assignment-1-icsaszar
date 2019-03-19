package ro.utcn.sd.icsaszar.assign1.persistence.memory

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import ro.utcn.sd.icsaszar.assign1.model.GenericEntity
import ro.utcn.sd.icsaszar.assign1.persistence.api.GenericRepository
import ro.utcn.sd.icsaszar.assign1.persistence.api.GenericRepositoryFactory


@Component
@ConditionalOnProperty(name = ["a1.repository-type"], havingValue = "MEMORY")
abstract class InMemoryRepositoryFactory<T : GenericEntity> : GenericRepositoryFactory<T> {
    abstract val repository: GenericInMemoryRepository<T>

    override fun createRepository(): GenericRepository<T> {
        return repository
    }
}