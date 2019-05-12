package ro.utcn.sd.icsaszar.assign1.service

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import ro.utcn.sd.icsaszar.assign1.model.User as StackoverflowUser
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ro.utcn.sd.icsaszar.assign1.exception.UserNotFoundException

@Service
class StackOverflowUserDetailsService(
        private val userService: UserService
): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if(username == null)
            throw UsernameNotFoundException("User not found")
        val user = userService.findUserByName(username) ?: throw UsernameNotFoundException("User not found")
        return User(user.userName, user.password, listOf(SimpleGrantedAuthority("ROLE_USER")))
    }



    @Transactional
    fun loadCurrentUser(): StackoverflowUser {
        val name = SecurityContextHolder.getContext().authentication.name
        return userService.findUserByName(name) ?: throw UserNotFoundException(name)
    }
}