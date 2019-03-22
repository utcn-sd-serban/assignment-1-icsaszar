package ro.utcn.sd.icsaszar.assign1.persistence.memory

import org.springframework.data.repository.CrudRepository
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.persistence.api.AnswerRepository
import ro.utcn.sd.icsaszar.assign1.persistence.api.QuestionRepository
import ro.utcn.sd.icsaszar.assign1.persistence.api.TagRepository
import ro.utcn.sd.icsaszar.assign1.persistence.api.UserRepository

class QuestionInMemoryRepository() : GenericInMemoryRepository<Question>(), QuestionRepository{

}

class AnswerInMemoryRepository() : GenericInMemoryRepository<Answer>(), AnswerRepository {

}

class TagInMemoryRepository() : GenericInMemoryRepository<Tag>(), TagRepository {

}

class UserInMemoryRepository() : GenericInMemoryRepository<User>(), UserRepository {
    override fun findByUserName(userName: String): User? {
        return data.values.find { it.userName == userName }
    }
}