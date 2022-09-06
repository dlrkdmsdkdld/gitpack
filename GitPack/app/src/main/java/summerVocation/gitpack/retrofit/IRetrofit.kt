package summerVocation.gitpack.retrofit

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IRetrofit {

    //https://api.github.com/users/Yunjung324
    @GET("users/{username}")
    fun searchUser(@Path("username") username:String ): Call<JsonElement>

}