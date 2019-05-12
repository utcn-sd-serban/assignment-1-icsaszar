package ro.utcn.sd.icsaszar.assign1.dto

import ro.utcn.sd.icsaszar.assign1.model.post.Question

data class NewQuestionDTO(
        val title: String,
        val text: String,
        val tags: List<TagDTO>
)

data class QuestionDTO(
        val id: Long,
        val title: String,
        val text: String,
        val score: Int?,
        val author: UserDTO,
        val posted: String,
        val answers: List<AnswerDTO>,
        val tags: List<TagDTO>){

    companion object {
        fun fromQuestion(question: Question): QuestionDTO {
            return QuestionDTO(
                    question.id!!,
                    question.title,
                    question.postText,
                    question.score,
                    question.author.toDTO(),
                    question.posted.toString(),
                    question.answers.map { it.toDTO()},
                    question.tags.map {it.toDTO()}
            )
        }
    }

}

