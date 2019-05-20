package ro.utcn.sd.icsaszar.assign1.seed

import org.springframework.boot.CommandLineRunner
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.Vote
import ro.utcn.sd.icsaszar.assign1.model.post.Answer
import ro.utcn.sd.icsaszar.assign1.model.post.Question
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.persistence.api.RepositoryFactory


@Component
// The Order ensures that this command line runner is ran first (before the ConsoleController)
@Order(Ordered.HIGHEST_PRECEDENCE)
class StudentSeed(
        private val factory: RepositoryFactory,
        private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {


    @Transactional
    @Throws(Exception::class)
    override fun run(vararg args: String) {
        clear()
        println("Seeding users")
        //http://www.optipess.com/2018/10/15/titsmcgee4782/

        val users = mutableListOf(
            User("TitsMcGee4782", passwordEncoder.encode("p@assW0rd")),
            User("User2", passwordEncoder.encode("dhas9d8hdq2de")),
            User("User3", passwordEncoder.encode("lkmdoiasjd09"))
        )
        val userRepository = factory.userRepository
        userRepository.apply {
            if (findAll().isEmpty()) {
                users.replaceAll { save(it) }
            }
        }

        println("Seeding tags")
        val tagRepository = factory.tagRepository
        val tags = listOf(Tag("java"), Tag("kotlin"), Tag("spring-boot"), Tag("general"))
        tagRepository.apply {
            if(findAll().isEmpty()){
                tags.forEach { save(it) }
            }
        }

        println("Seeding questions")
        val questionRepository = factory.questionRepository
        val questions = mutableListOf<Question>()
        questions += Question(users[0], "^Title", title = "How do i even").addTag(tags[3])
        questions += Question(users[0], "Ubuntu is so bad like you can't even install an exe, like no wonder nobody uses it", title = "Why can'i i install an exe on ubuntu")
        questions += Question(users[1], "Pls help the deadline is tomorrow", title = "Help my program keeps crashing").addTag(tags[3])
        questions += Question(users[2], "My hands hurt from so much typing",title = "Why is java so verbose?").addTag(tags[0])
        questionRepository.apply {
            if(findAll().isEmpty()) {
                questions.replaceAll {save(it)}
            }
        }

        println("Seeding answers")
        val answerRepository = factory.answerRepository
        val answers = mutableListOf<Answer>()
        answers += Answer(users[2], "Yes").setQuestion(questions[0])
        answers += Answer(users[1], "No").setQuestion(questions[0])
        answers += Answer(users[2], "Dude...").setQuestion(questions[1])
        answers += Answer(users[0], "I have the same problem").setQuestion(questions[2])
        answers += Answer(users[0], "Never mind i figured it out").setQuestion(questions[0])
        answerRepository.apply {
            if(findAll().isEmpty()){
                answers.replaceAll { save(it) }
            }
        }

        println("Seeding votes")
        val voteRepository = factory.voteRepository
        val votes = mutableListOf<Vote>()
        votes += Vote(questions[0], users[1], -1)
        votes += Vote(questions[0], users[2], -1)
        votes += Vote(questions[2], users[1], 1)
        votes += Vote(questions[3], users[1], -1)

        votes += Vote(answers[4], users[1], -1)
        votes += Vote(answers[4], users[2], -1)
        votes += Vote(answers[0], users[1], 1)
        votes += Vote(answers[1], users[2], 1)
        votes += Vote(answers[1], users[0], 1)
        votes += Vote(answers[3], users[2], 1)
        voteRepository.apply {
            if(findAll().isEmpty()){
                votes.forEach {
                    save(it)
                }
            }
        }
    }

    fun clear(){
        factory.apply {
            answerRepository.deleteAll()
            questionRepository.deleteAll()
            tagRepository.deleteAll()
            userRepository.deleteAll()
            voteRepository.deleteAll()
        }

    }
}