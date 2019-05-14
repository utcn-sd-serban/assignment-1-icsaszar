package ro.utcn.sd.icsaszar.assign1.dto

import ro.utcn.sd.icsaszar.assign1.model.post.Answer

data class AnswerDTO (val id: Long,
                      val text: String,
                      val author: UserDTO,
                      val posted: String,
                      val score: Int?)

{
    companion object {
        fun fromAnswer(answer: Answer): AnswerDTO {
            return AnswerDTO(
                    answer.id!!,
                    answer.postText,
                    answer.author.toDTO(),
                    answer.posted.toString(),
                    answer.score)
        }
    }

}

data class NewAnswerDTO (
        val postId: Long,
        val text: String
)