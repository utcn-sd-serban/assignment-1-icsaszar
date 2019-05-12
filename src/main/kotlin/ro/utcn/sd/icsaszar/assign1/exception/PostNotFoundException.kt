package ro.utcn.sd.icsaszar.assign1.exception

import java.lang.RuntimeException

class PostNotFoundException : RuntimeException {
    constructor(id: Long): super("Post with id $id not found!")
}
