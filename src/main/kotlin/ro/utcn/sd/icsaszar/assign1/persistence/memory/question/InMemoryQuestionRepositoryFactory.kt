package ro.utcn.sd.icsaszar.assign1.persistence.memory.question

import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.persistence.memory.GenericInMemoryRepository
import ro.utcn.sd.icsaszar.assign1.persistence.memory.InMemoryRepositoryFactory

class InMemoryQuestionRepositoryFactory : InMemoryRepositoryFactory<Question>(){
    override val repository: GenericInMemoryRepository<Question> = InMemoryQuestionRepository()
}