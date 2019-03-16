package ro.utcn.sd.icsaszar.assign1.persistence.memory

import ro.utcn.sd.icsaszar.assign1.model.Question
import ro.utcn.sd.icsaszar.assign1.persistence.QuestionRepository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.atomic.AtomicInteger

class InMemoryQuestionRepository(private val data: ConcurrentMap<Int, Question> = ConcurrentHashMap()) : QuestionRepository {
    private val currentId: AtomicInteger = AtomicInteger(1)
    override fun save(question: Question): Question {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        if(question.id != 0)
            data[question.id] = question
        else{
            question.id = currentId.getAndIncrement()
            data[question.id] = question
        }

    }

    override fun findById(id: Int): Question? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(question: Question) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listAll(): List<Question> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
