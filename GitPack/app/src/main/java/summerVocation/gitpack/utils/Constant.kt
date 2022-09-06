package summerVocation.gitpack.utils

object Constant {
    const val TAG : String = "로그"
}
object APIKEY{
    const val BASE_URL : String = "https://api.github.com/"
    const val SEARCH_USERS : String = "users"
}
enum class RESPONSE_STATUS{
    OKAY,
    FAIL,
    NO_CONTENT
}