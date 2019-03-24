package ro.utcn.sd.icsaszar.assign1.persistence.jpa

import ro.utcn.sd.icsaszar.assign1.model.GenericEntity
import ro.utcn.sd.icsaszar.assign1.persistence.api.GenericRepository
import javax.persistence.EntityManager
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery

abstract class GenericJpaRepository<T : GenericEntity>(
        private val entityManager: EntityManager,
        private val entityClass: Class<T>): GenericRepository<T> {
    override fun save(entity: T): T {
        return if(entity.id == null){
            entityManager.persist(entity)
            entity
        }else{
            entityManager.merge(entity)
        }
    }

    override fun delete(entity: T) {
        entityManager.remove(entity)
    }

    override fun findAll(): List<T> {
        val builder: CriteriaBuilder = entityManager.criteriaBuilder
        val query: CriteriaQuery<T> = builder.createQuery(entityClass)
        query.select(query.from(entityClass))
        return entityManager.createQuery(query).resultList
    }

    override fun findById(id: Long): T? {
        return entityManager.find(entityClass, id)
    }
}