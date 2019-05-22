package ro.utcn.sd.icsaszar.assign1.exception

class InvalidTagException : RuntimeException{
    constructor(name: String): super("Tag with $name not found!")
}