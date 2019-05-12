package ro.utcn.sd.icsaszar.assign1.controller

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Controller
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Post
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.service.*

@Controller
class CommandLineController(
       private val userService: UserService,
       private val questionService: QuestionService,
       private val answerService: AnswerService,
       private val tagService: TagService,
       private val voteService: VoteService
) : CommandLineRunner{
    private var currentUser: User? = null


    override fun run(vararg args: String?) {

        mainLoop@ while(true)
        {
            while(currentUser == null){
                handleLogin()
            }

            readLoop@ while(true)
            {
                println("Type help to view a list of available commands")
                print(">")
                val cmd = readLine()!!

                when (cmd) {
                    "post" -> handlePost()
                    "list" -> handleList()
                    "tags" -> handleTags()
                    "filter" -> handleFilter()
                    "view" -> handleView()
                    "account" -> handleAccount()
                    "logout" -> {
                        println("Logging out")
                        currentUser = null
                        break@readLoop
                    }
                    "help" -> handleHelp()
                    else -> println("Invalid command")
                }
            }

        }

    }

    private fun handleAccount() {
       main_loop@ do {
            println("[account] Type q to view a list of your questions")
            println("[account] Type a to view a list of your answers")
            println("[account] Type manage to edit or delete an answer")
            println("[account] Type a to done to exit")
            var cmd = readLine()!!

            when(cmd){
                "q" -> {
                    val questions = questionService.findAllByAuthorId(currentUser!!.id!!)
                    questions.forEach {
                        println(it.display())
                    }
                }
                "a" -> {
                    val answers = answerService.findAllByAuthorId(currentUser!!.id!!)
                    answers.forEach {
                        println(it.answerTo!!.display())
                        println(it.display())
                    }
                }
                "manage" -> {
                    println("[account] [manage] Type the id of the answer you would like to manage")
                    cmd = readLine()!!
                    val id = cmd.toLongOrNull()
                    if(id == null){
                        println("[account] [manage] Please type a number")
                        continue@main_loop
                    }
                    val answers = answerService.findAllByAuthorId(currentUser!!.id!!)
                    val answer = answerService.findById(id)

                    if(answer == null){
                        println("[account] [manage] Answer with id: $id not found")
                        continue@main_loop
                    }else if(answer !in answers){
                        println("[account] [manage] Answer with id: $id not found")
                        continue@main_loop
                    } else {
                        println(answer.answerTo!!.display())
                        println(answer.display())
                        println("[account] [manage] Type delete to delete this answer")
                        println("[account] [manage] Type edit to edit this answer")
                        cmd = readLine()!!
                        when (cmd){
                            "edit" -> {
                                println("[account] [manage] [edit] Type new answer text")
                                val newText = readLine()!!
                                answer.postText = newText
                                answerService.updateAnswer(answer)
                                println("[account] [manage] [edit] Answer updated")
                            }
                            "delete" -> {
                                answerService.deleteAnswer(answer)
                                println("[account] [manage] [delete] Answer deleted")
                            }
                            else -> {
                                println("[account] [manage] Invalid command")
                            }
                        }
                    }
                }
                "done" -> {}
                else -> {
                    println("[account] Invalid command")
                }
            }
        }while(cmd != "done")
    }

    private fun handleAnswer() {

    }

    private fun handleView() {
        println("[view] Type the id of a question to view it")
        print("[view] Id: ")
        var cmd = readLine()!!
        val questionId = cmd.toLong()
        var question = questionService.findById(questionId)
        if(question == null){
            println("[view] No such question")
            return
        }
        val posts = mutableListOf<Post>(question)

        println(question.display())
        answerService.findAllByPostIdOrderByScoreDesc(question.id!!)
                .forEach {
                    //I know it.display(voteService.getPostScore(it)) is horrible,
                    //especially since the scores are already calculated to do the sorting
                    //but getting it right with hibernate (using ResultSetMapping or @Formula fields)
                    //is something don't have enough time to attempt
                    println(it.display(voteService.getPostScore(it)))
                    posts.add(it)
                }

        println("[view] Type answer to submit an answer to this question")
        println("[view] Type vote to vote on a displayed post")
        println("[view] Type done to exit")

        cmd = readLine()!!

        while(cmd != "done"){
            when (cmd){
                "answer" ->{
                    print("[view] [answer] Type answer text:")
                    cmd = readLine()!!
                    answerService.submitAnswer(cmd, currentUser!!, question)
                    println("[view] [answer] Answer submitted")
                }
                "vote" ->{
                    println("[view] [vote] Type the id you want to vote up or down")
                    print("[view] [vote] Id:")
                    cmd = readLine()!!
                    val id = cmd.toLong()
                    val targetPost = posts.find { it.id!! == id }
                    if(targetPost != null){
                        if(targetPost.author.id!! != currentUser!!.id!!){
                            println("[view] [vote] Type up or down to upvote or downvote")
                            print("[view] [vote] Direction:")
                            cmd = readLine()!!
                            when(cmd){
                                "up" -> {
                                    if(voteService.upVote(targetPost,currentUser!!))
                                        println("[view] [vote] Vote recorded")
                                    else
                                        println("[view] [vote] You already upvoted this post")
                                }
                                "down" -> {
                                    if(voteService.downVote(targetPost, currentUser!!))
                                        println("[view] [vote] Vote recorded")
                                    else
                                        println("[view] [vote] You already downvoted this post")
                                }
                                else -> println("[view] [vote] Invalid command")
                            }
                        }else{
                            println("[view] [vote] Can't vote on your own post!")
                        }

                    }else{
                        println("[view] [vote] Invalid post id!")
                    }
                }
                "refresh" -> {

                }
                else ->{
                    println("[view] [answer] Invalid command")
                }
            }

            println("[view] Type answer to submit an answer to this question")
            println("[view] Type vote to vote on a displayed post")
            println("[view] Type done to exit")

            cmd = readLine()!!
        }
    }

    private fun handleTags() {
        println("Available tags:")
        tagService.listAllTags().forEach { println(it.tagName) }
    }

    private fun handleFilter() {
        println("Type title <postText> to search by title")
        println("Type tag <tag-name> to filter by tag")
        print("[filter] >")
        var cmd = readLine()!!
        if(cmd.startsWith("title ")){
            val text = cmd.removePrefix("title ")
            if(text.isBlank()){
                println("Please enter a title")
                return
            }
            val filteredQuestions = questionService.findAllByTitleContaining(text)
            if(filteredQuestions.isEmpty())
                println("No results")
            else
                filteredQuestions.forEach {
                    println(it.display())
                }
        } else if(cmd.startsWith("tag")){
            val tagName = cmd.removePrefix("tag ")
            if(tagName.isBlank()){
                println("Please enter a tag")
                return
            }
            val tag = tagService.findTagByName(tagName)
            if(tag != null){
                val filteredQuestions = questionService.findAllByTag(tag)
                if(filteredQuestions.isEmpty())
                    println("No results")
                else
                    filteredQuestions.forEach {
                        println(it.display())
                    }
            }else{
                println("No such tag")
            }
        } else {
          println("Invalid command")
        }
    }

    private fun handlePost() {
        println("[post] Type your post title")
        print("[post] Title: ")
        val title = readLine()!!
        println("[post] Type your post text")
        print("[post] Text: ")
        val text = readLine()!!
        println("[post] Type add <tag-name> to add a tag")
        println("[post] Type list to view a list of available tags")
        println("[post] Type view to view a the tags added")
        println("[post] Type done to continue")
        print("[post] Tags: ")
        val tags = mutableSetOf<Tag>()
        var cmd = readLine()!!
        while(cmd != "done"){
            if(cmd == "list") {
                println("[post] Available tags:")
                tagService.listAllTags().forEach { println(it.tagName) }
            }else if (cmd == "view"){
                println("[post] Current tags:")
                tags.forEach { println(it.tagName) }
            } else if(cmd.startsWith("add ")){
                val tagName = cmd.removePrefix("add ")
                if(tagName.isBlank()){
                    println("[post] Please enter a tag name")
                    cmd = readLine()!!
                    continue
                }

                var tag = tagService.findTagByName(tagName)
                if(tag != null){
                    if(tags.add(tag))
                        println("[post] Tag $tagName added")
                    else
                        println("[post] Tag $tagName has already been added")
                } else {
                    println("[post] Tag $tagName does not exist, would you like to add it?")
                    print("[post] Create tag $tagName: ")
                    cmd = readLine()!!
                    while((cmd != "yes") and (cmd != "no")){
                        println("Please type yes or no")
                        print("[post] Create tag $tagName: ")
                        cmd = readLine()!!
                    }

                    if(cmd == "yes"){
                        tag = tagService.createTag(tagName)
                        println("[post] Tag $tagName created")
                        tags.add(tag)
                    }
                }
            }
            else{
                println("[post] Invalid command")
            }
            println("[post] Type add <tag-name> to add a tag")
            println("[post] Type list to view a list of available tags")
            println("[post] Type view to view a the tags added")
            println("[post] Type done to continue")
            print("[post] Tags: ")
            cmd = readLine()!!
        }
        println("[post] Your question has been posted!")
        questionService.postQuestion(currentUser!!, title, text, tags)

    }

    private fun handleList() {
        questionService.listAllQuestionsByPosted().forEach {q ->
            //I know q.display(voteService.getPostScore(q)) is horrible,
            //but getting it right with hibernate (using ResultSetMapping)
            //is something don't have enough time to attempt
            println(q.display())
            println("".padEnd(100, '-'))
        }
    }

    private fun handleHelp(){
        val l = listOf("logout", "post", "list", "answer", "help", "filter", "view", "account")
        println("The available commands are:")
        l.forEach { println(it) }
    }

    private fun handleLogin(){
        println("Type your username to login")
        print("[login] Username: ")
        val userName = readLine()!!
        currentUser = userService.findUserByName(userName)
        if (currentUser != null){
            println("[login] Welcome $userName!")
        } else{
            println("[login] Invalid username")
        }
    }
}