package ro.utcn.sd.icsaszar.assign1.persistence.api

import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.persistence.api.GenericRepository

interface QuestionRepository : GenericRepository<Question> {

}

interface AnswerRepository : GenericRepository<Answer>{

}

interface UserRepository : GenericRepository<User>{

}

interface TagRepository : GenericRepository<Tag>{

}

