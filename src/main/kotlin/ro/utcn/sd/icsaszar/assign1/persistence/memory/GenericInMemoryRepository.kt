package ro.utcn.sd.icsaszar.assign1.persistence.memory

import ro.utcn.sd.icsaszar.assign1.model.GenericEntity
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.persistence.api.GenericRepository
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong


abstract class GenericInMemoryRepository<T : GenericEntity> : GenericRepository<T>{
    private val data: MutableMap<Long, T> = HashMap()
    private val currentId: AtomicLong = AtomicLong(0)

    override fun save(entity: T): T {
        if(entity.id == null){
            entity.id = currentId.getAndIncrement()
        }

        data[entity.id!!] = entity

        return entity
    }

    override fun update(id: Long, entity: T): T {
        data[id] = entity
        return entity
    }

    override fun delete(entity: T) {
        data.remove(entity.id)
    }

    override fun findById(id: Long): T? {
        return data[id]
    }

    override fun findAll(): List<T> {
        return data.values.toList()
    }
}