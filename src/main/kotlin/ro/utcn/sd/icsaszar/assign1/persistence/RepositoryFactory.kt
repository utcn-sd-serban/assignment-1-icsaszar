package ro.utcn.sd.icsaszar.assign1.persistence

interface RepositoryFactory{

    fun createQuestionRespository(): QuestionRepository
}