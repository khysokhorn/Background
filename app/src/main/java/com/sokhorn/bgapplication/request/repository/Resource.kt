package com.nexvis.sunfixmall.request.repo


//@Parcelize
//data class Resource<T>(
//    val state: State,
//    val response: @RawValue T? = null,
//    val error: Throwable? = null
//) : Parcelable {
//    companion object {
//        fun <T> none(response: T? = null) =
//            Resource(State.NONE, response)
//
//        fun <T> progress() = Resource<T>(State.PROGRESS)
//        fun <T> success(response: T? = null) =
//            Resource(State.SUCCESS, response)
//
//        fun <T> error(t: Throwable) =
//            Resource<T>(State.ERROR, null, t)
//
//    }
//}

data class Resource<out T>(val status: State, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> =
            Resource(status = State.SUCCESS, data = data, message = null)

        fun <T> error(t: Throwable) =
            Resource<T>(State.ERROR, null, t.message)

        fun  progress() = Resource(State.PROGRESS, null, null)
    }
}