package ro.utcn.sd.icsaszar.assign1.exception

import java.lang.RuntimeException

class UserNotFoundException : RuntimeException {
    constructor(id: Long): super("User with id $id not found!")

    constructor(userName: String): super("User with name $userName not found!")
}
