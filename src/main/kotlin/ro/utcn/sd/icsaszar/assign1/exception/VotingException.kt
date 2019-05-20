package ro.utcn.sd.icsaszar.assign1.exception

class VotingException : RuntimeException{
    private constructor(msg: String): super(msg)
    companion object{
        val alreadyVoted
                get() = VotingException("Already voted on post")
        val votingOnOwnPost
                get() = VotingException("Voting on own post")
    }
}