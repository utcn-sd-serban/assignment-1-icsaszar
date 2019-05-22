package ro.utcn.sd.icsaszar.assign1.exception

import java.lang.RuntimeException

class AuthorizationException : RuntimeException{
    private constructor(msg: String): super(msg)
    companion object{
        val notOwnContent
            get() = AuthorizationException("Trying to edit other users content!")
    }
}