package ro.utcn.sd.icsaszar.assign1.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.persistence.api.RepositoryFactory

@Service
class QuestionService(val repositoryFactory: RepositoryFactory){

    @Transactional
    fun listAllQuestions(): List<Question> = repositoryFactory.questionRepository.findAll()

}