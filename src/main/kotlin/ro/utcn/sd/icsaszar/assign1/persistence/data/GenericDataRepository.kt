package ro.utcn.sd.icsaszar.assign1.persistence.data

import org.springframework.data.repository.Repository
import ro.utcn.sd.icsaszar.assign1.model.GenericEntity
import ro.utcn.sd.icsaszar.assign1.persistence.api.GenericRepository

interface GenericDataRepository<T : GenericEntity> : Repository<T, Long>, GenericRepository<T>{

}