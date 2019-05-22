package ro.utcn.sd.icsaszar.assign1.event

import ro.utcn.sd.icsaszar.assign1.dto.AnswerDTO
import ro.utcn.sd.icsaszar.assign1.dto.QuestionDTO
import ro.utcn.sd.icsaszar.assign1.dto.VoteDTO

enum class EventType(val type: String){
    NEW_QUESTION("NEW_QUESTION"),
    NEW_ANSWER("NEW_ANSWER"),
    NEW_VOTE("NEW_VOTE")
}

sealed class Event(val type: EventType)

data class NewQuestionEvent(
        val payload: QuestionDTO
): Event(EventType.NEW_QUESTION)

data class NewVoteEvent(
        val payload: VoteDTO
): Event(EventType.NEW_VOTE)

data class NewAnswerPayload(
        val questionId: Long,
        val answer: AnswerDTO
)

data class NewAnswerEvent(
        val payload: NewAnswerPayload
): Event(EventType.NEW_ANSWER)