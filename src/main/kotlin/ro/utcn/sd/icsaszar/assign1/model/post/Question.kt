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

        @ManyToMany
        @JoinTable(name = "question_tag",
                joinColumns = [JoinColumn(name = "question_id")],
                inverseJoinColumns = [JoinColumn(name = "tag_id")])
        var tags: MutableSet<Tag> = mutableSetOf()

        @OneToMany(mappedBy = "answerTo")
        var answers: MutableList<Answer> = mutableListOf()
}