package ro.utcn.sd.icsaszar.assign1.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.persistence.api.RepositoryFactory

@Service
class UserService(private val repositoryFactory: RepositoryFactory){

    @Transactional
    fun findUserByName(userName: String): User?{
        return repositoryFactory.userRepository.findByUserName(userName)
    }


}