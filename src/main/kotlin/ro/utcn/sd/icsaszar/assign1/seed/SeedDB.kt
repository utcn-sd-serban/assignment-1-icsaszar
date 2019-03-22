package ro.utcn.sd.icsaszar.assign1.seed

import org.springframework.boot.CommandLineRunner
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.persistence.api.RepositoryFactory


@Component
// The Order ensures that this command line runner is ran first (before the ConsoleController)
@Order(Ordered.HIGHEST_PRECEDENCE)
class StudentSeed(private val factory: RepositoryFactory) : CommandLineRunner {


    @Transactional
    @Throws(Exception::class)
    override fun run(vararg args: String) {
        println("Seeding users")
        val userRepository = factory.userRepository
        if (userRepository.findAll().isEmpty()) {
            userRepository.save(User("User1"))
            userRepository.save(User("User2"))
            userRepository.save(User("User3"))
        }
        println("Seeding tags")
        println("Seeding questions")
        println("Seeding answers")
    }
}