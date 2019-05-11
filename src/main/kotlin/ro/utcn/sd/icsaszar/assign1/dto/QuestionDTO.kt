package ro.utcn.sd.icsaszar.assign1.dto

import ro.utcn.sd.icsaszar.assign1.model.post.Question

data class QuestionDTO(
        val id: Long,
        val title: String,
        val text: String,
        val score: Int?,
        val author: UserDTO,
        val answers: List<AnswerDTO>){

    companion object {
        fun fromQuestion(question: Question): QuestionDTO {
            return QuestionDTO(
                    question.id!!,
                    question.title,
                    question.postText,
                    question.score,
                    question.author.toDTO(),
                    question.answers.map { it.toDTO()}
            )
        }
    }

}

