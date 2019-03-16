package ro.utcn.sd.icsaszar.assign1.persistence

import ro.utcn.sd.icsaszar.assign1.model.Question

interface QuestionRepository{
    fun save(question: Question): Question
    fun findById(id: Int): Question?
    fun remove(question: Question)
    fun listAll(): List<Question>
}