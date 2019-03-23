package ro.utcn.sd.icsaszar.assign1.persistence.api

import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.persistence.api.GenericRepository

interface QuestionRepository : GenericRepository<Question> {
    fun findAllByTags(tag: Tag): List<Question>
    fun findAllByTitleContainsIgnoreCase(text: String): List<Question>
    fun findAllByOrderByPostedDesc(): List<Question>
}

interface AnswerRepository : GenericRepository<Answer>{

}

interface UserRepository : GenericRepository<User>{
    fun findByUserName(userName: String) : User?
}

interface TagRepository : GenericRepository<Tag>{
    fun findByName(name: String) : Tag?
}

