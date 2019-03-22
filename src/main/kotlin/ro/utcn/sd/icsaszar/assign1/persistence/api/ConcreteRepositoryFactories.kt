package ro.utcn.sd.icsaszar.assign1.persistence.api

import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag

interface RepositoryFactory {
    val questionRepository : QuestionRepository
    val answerRepository: AnswerRepository
    val userRepository: UserRepository
    val tagRepository: TagRepository
}