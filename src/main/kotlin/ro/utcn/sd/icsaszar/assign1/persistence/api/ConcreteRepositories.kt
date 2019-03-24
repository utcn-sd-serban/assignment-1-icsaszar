package ro.utcn.sd.icsaszar.assign1.persistence.api

import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag

interface QuestionRepository : GenericRepository<Question> {
    fun findAllByTags(tag: Tag): List<Question>
    fun findAllByTitleContainsIgnoreCase(text: String): List<Question>
    fun findAllByOrderByPostedDesc(): List<Question>
    fun findAllByAuthor_Id(id: Long): List<Question>
}

interface AnswerRepository : GenericRepository<Answer>{
    fun findAllByAnswerTo_Id(questionId: Long): List<Answer>
    fun findAllByAuthor_Id(id: Long): List<Answer>
}

interface UserRepository : GenericRepository<User>{
    fun findByUserName(userName: String) : User?
}

interface TagRepository : GenericRepository<Tag>{
    fun findByTagName(name: String) : Tag?
    fun findAllByQuestions_Id(id: Long): List<Tag>
}

