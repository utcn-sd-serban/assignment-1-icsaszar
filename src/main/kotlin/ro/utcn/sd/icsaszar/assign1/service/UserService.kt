package ro.utcn.sd.icsaszar.assign1.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ro.utcn.sd.icsaszar.assign1.persistence.api.RepositoryFactory

@Service
class UserService(val repositoryFactory: RepositoryFactory){

    @Transactional
    fun checkUserLogin(userName: String): Boolean{
        return repositoryFactory.userRepository.findByUserName(userName) != null
    }
}