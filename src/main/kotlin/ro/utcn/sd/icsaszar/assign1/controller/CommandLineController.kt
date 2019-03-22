package ro.utcn.sd.icsaszar.assign1.controller

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Controller
import ro.utcn.sd.icsaszar.assign1.persistence.api.RepositoryFactory
import ro.utcn.sd.icsaszar.assign1.service.UserService
import java.util.*

@Controller
class CommandLineController(
       private val userService: UserService
) : CommandLineRunner{
    private var currentUser: String? = null


    override fun run(vararg args: String?) {
        println("Type help to view a list of available commands")
        readLoop@ while(true)
        {
            print(">")
            val cmd = readLine()!!

            when (cmd) {
                "login" -> handleLogin()
                "post" -> {}
                "list" -> {}
                "answer" -> {}
                "quit" -> {
                    println("Exiting")
                    break@readLoop
                }
                "help" -> handleHelp()
                else -> {
                    println("Invalid command")
                }
            }
        }
    }

    private fun handleHelp(){
        val l = listOf("login", "logout", "post", "list", "answer", "quit", "help")
        println("The available commands are:")
        l.forEach { println(it) }
    }

    private fun handleLogin(){
        print("[login] Username: >")
        val userName = readLine()!!
        if (userService.checkUserLogin(userName)){
            println("[login] Welcome $userName!")
            currentUser = userName
        }
        else
            println("[login] Invalid username")
    }
}