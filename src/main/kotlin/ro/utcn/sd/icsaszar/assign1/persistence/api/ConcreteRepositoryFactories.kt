package ro.utcn.sd.icsaszar.assign1.persistence.api

import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag

interface QuestionRepositoryFactory {
    fun createRepository() : QuestionRepository
}

interface AnswerRepositoryFactory{
    fun createRepository() : AnswerRepository
}

interface UserRepositoryFactory{
    fun createRepository() : UserRepository
}

interface TagRepositoryFactory{
    fun createRepository() : TagRepository
}