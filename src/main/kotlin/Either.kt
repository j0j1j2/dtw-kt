sealed class Either<out L, out R> {
    data class Left<L>(val value: L): Either<L, Nothing>() {
        override fun left(): L = value
        override fun right(): Nothing? = null
    }
    data class Right<R>(val value: R): Either<Nothing, R>() {
        override fun left(): Nothing? = null
        override fun right(): R = value
    }

    abstract fun left(): L?
    abstract fun right(): R?
}