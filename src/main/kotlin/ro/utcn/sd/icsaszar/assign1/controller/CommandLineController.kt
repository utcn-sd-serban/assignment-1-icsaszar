package ro.utcn.sd.icsaszar.assign1.controller

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Controller
import ro.utcn.sd.icsaszar.assign1.model.User
import ro.utcn.sd.icsaszar.assign1.model.post.Tag
import ro.utcn.sd.icsaszar.assign1.service.QuestionService
import ro.utcn.sd.icsaszar.assign1.service.TagService
import ro.utcn.sd.icsaszar.assign1.service.UserService

@Controller
class CommandLineController(
       private val userService: UserService,
       private val questionService: QuestionService,
       private val tagService: TagService
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
                    "answer" -> {}
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
        println("Type your post title")
        print("[post] Title: ")
        val title = readLine()!!
        println("Type your post postText")
        print("[post] Text: ")
        val text = readLine()!!
        println("Type add <tag-name> to add a tag")
        println("Type list to view a list of available tags")
        println("Type view to view a the tags added")
        println("Type done to continue")
        print("[post] Tags: ")
        val tags = mutableSetOf<Tag>()
        var cmd = readLine()!!
        while(cmd != "done"){
            if(cmd == "list") {
                println("Available tags:")
                tagService.listAllTags().forEach { println(it.tagName) }
            }else if (cmd == "view"){
                println("Current tags:")
                tags.forEach { println(it.tagName) }
            } else if(cmd.startsWith("add ")){
                val tagName = cmd.removePrefix("add ")
                if(tagName.isBlank()){
                    println("Please enter a tag name")
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
                    print("[post] Create tag: ")
                    cmd = readLine()!!
                    while((cmd != "yes") and (cmd != "no")){
                        println("Please type yes or no")
                        print("[post] Create tag: ")
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
                println("Invalid command")
            }
            cmd = readLine()!!
        }
        println("Your question has been posted!")
        questionService.postQuestion(currentUser!!, title, text, tags)

    }

    private fun handleList() {
        questionService.listAllQuestionsByPosted().forEach {q ->
            println(q.display())
//            q.answers.forEach {a ->
//                println(a.display())
//                println()
//            }
            println("".padEnd(100, '-'))
        }
    }

    private fun handleHelp(){
        val l = listOf("logout", "post", "list", "answer", "help", "filter")
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