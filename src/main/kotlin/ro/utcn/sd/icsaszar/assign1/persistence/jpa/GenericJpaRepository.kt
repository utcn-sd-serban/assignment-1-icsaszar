package ro.utcn.sd.icsaszar.assign1.persistence.jpa

import ro.utcn.sd.icsaszar.assign1.model.GenericEntity
import ro.utcn.sd.icsaszar.assign1.persistence.api.GenericRepository
import javax.persistence.EntityManager

abstract class GenericJpaRepository<T : GenericEntity>(private val entityManager: EntityManager): GenericRepository<T> {
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

    //    public List<Student> findAll() {
//        // the criteria builder is used to create a type-safe query; an alternative would have been
//        // to write a JPQL query instead ("SELECT s FROM Student s") or to use named queries
//        // https://docs.jboss.org/hibernate/entitymanager/3.5/reference/en/html/querycriteria.html
//        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Student> query = builder.createQuery(Student.class);
//        query.select(query.from(Student.class));
//        return entityManager.createQuery(query).getResultList();
//    }
//
//
//    @Override
//    public void remove(Student student) {
//        entityManager.remove(student);
//    }
//
//    @Override
//    public Optional<Student> findById(int id) {
//        return Optional.ofNullable(entityManager.find(Student.class, id));
//    }
}