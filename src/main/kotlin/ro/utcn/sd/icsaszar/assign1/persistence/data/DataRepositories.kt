package ro.utcn.sd.icsaszar.assign1.persistence.data

import org.springframework.data.repository.CrudRepository
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.persistence.api.AnswerRepository
import ro.utcn.sd.icsaszar.assign1.persistence.api.QuestionRepository
import ro.utcn.sd.icsaszar.assign1.persistence.api.TagRepository
import ro.utcn.sd.icsaszar.assign1.persistence.api.UserRepository

interface QuestionDataRepository : CrudRepository<Question, Long>, QuestionRepository {

}

interface AnswerDataRepository : CrudRepository<Answer, Long>, AnswerRepository {

}

interface TagDataRepository : CrudRepository<Tag, Long>, TagRepository {

}

interface UserDataRepository : CrudRepository<User, Long>, UserRepository {

}