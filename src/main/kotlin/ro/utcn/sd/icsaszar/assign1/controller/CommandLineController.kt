package ro.utcn.sd.icsaszar.assign1.controller

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Controller
import ro.utcn.sd.icsaszar.assign1.persistence.api.RepositoryFactory
import ro.utcn.sd.icsaszar.assign1.service.QuestionService
import ro.utcn.sd.icsaszar.assign1.service.UserService
import java.util.*

@Controller
class CommandLineController(
       private val userService: UserService,
       private val questionService: QuestionService
) : CommandLineRunner{
    private var currentUser: String? = null


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
                    "post" -> {}
                    "list" -> handleList()
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

    private fun handleList() {
        questionService.listAllQuestions().forEach {
            println(it.display())
            println("".padEnd(45, '-'))
        }
    }

    private fun handleHelp(){
        val l = listOf("logout", "post", "list", "answer", "help")
        println("The available commands are:")
        l.forEach { println(it) }
    }

    private fun handleLogin(){
        print("[login] Username: ")
        val userName = readLine()!!
        if (userService.checkUserLogin(userName)){
            println("[login] Welcome $userName!")
            currentUser = userName
        }
        else
            println("[login] Invalid username")
    }
}