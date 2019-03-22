package ro.utcn.sd.icsaszar.assign1.model.post

import ro.utcn.sd.icsaszar.assign1.model.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity
//TODO: Discriminator field
class Question(
        author: User = User(),
        text: String = "",
        posted: LocalDateTime = LocalDateTime.now(),

        @Column
        var title: String = "") : Post(author, text, posted){

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "question_tag",
            joinColumns = [JoinColumn(name = "question_id")],
            inverseJoinColumns = [JoinColumn(name = "tag_id")])
    var tags: MutableSet<Tag> = mutableSetOf()

    @OneToMany(mappedBy = "answerTo", fetch = FetchType.EAGER)
    var answers: MutableList<Answer> = mutableListOf()

    fun display(): String {
        val sb: StringBuilder = StringBuilder()
        sb.append(title).append("\n")
            .append(text).append("\n")
            .append(author.userName.padEnd(30))
            .append(posted).append("\n")
        return sb.toString()
    }
}