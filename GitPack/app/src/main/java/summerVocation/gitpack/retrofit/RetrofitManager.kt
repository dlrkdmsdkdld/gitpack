package summerVocation.gitpack.retrofit

import android.util.Log
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Response
import summerVocation.gitpack.model.SearchUser
import summerVocation.gitpack.utils.APIKEY
import summerVocation.gitpack.utils.Constant.TAG
import summerVocation.gitpack.utils.RESPONSE_STATUS
import java.text.SimpleDateFormat

class RetrofitManager {
    companion object{
        val instance = RetrofitManager()
    }
    private val iRetrofit : IRetrofit? = RetrofitClient.getClient(APIKEY.BASE_URL)?.create(IRetrofit::class.java)

    fun searchUser(username:String? , completion:(RESPONSE_STATUS,SearchUser?) -> Unit){
        val term = username ?:""
        val call = iRetrofit?.searchUser(username = term).let {
            it
        }?: return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when(response.code()){
                    200 ->{
                        response.body()?.let {
                            var parsedSearchUserDataArray = ArrayList<SearchUser>()
                            val body =it.asJsonObject
                            val Dusername = body.get("login").asString
                            val DcreatAt = body.get("created_at").asString
                            val DlastCommit = body.get("public_repos").asInt
                            var Dfollowcount = body.get("followers").asInt
                            var DimageUrl = body.get("avatar_url").asString
                            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                            val formatter = SimpleDateFormat("yyyy년 MM월 dd일")
                            val creatAtDateString = formatter.format(parser.parse(DcreatAt))
//                            val lastCommitDateString = formatter.format(parser.parse(DlastCommit))

                            val userItem = SearchUser(imageurl = DimageUrl,name = Dusername, lastcommit = DlastCommit, createdAt = creatAtDateString, follower = Dfollowcount)
                            parsedSearchUserDataArray.add(userItem)
                            completion(RESPONSE_STATUS.OKAY,userItem)

                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                completion(RESPONSE_STATUS.OKAY,null)
            }

        })

    }
    fun searchFollower(  completion:(RESPONSE_STATUS,ArrayList<String>?) -> Unit){
            val call = iRetrofit?.searchFollower().let {
                it
            }?: return

            call.enqueue(object : retrofit2.Callback<JsonElement>{
                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    val resultFinal = ArrayList<String>()
                    when(response.code()){
                        200 ->{
                            response.body()?.let {
                                    val body = it.asJsonArray
                                if(body !=null){
                                    body.forEach { resultItem ->
                                        val resultItmeObject = resultItem.asJsonObject
                                        val name = resultItmeObject.get("login").asString
                                        resultFinal.add(name)
                                        Log.d(TAG,"name = $name")
                                    }
                                }
                                completion(RESPONSE_STATUS.OKAY,resultFinal)

                            }
                        }
                    }
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    completion(RESPONSE_STATUS.OKAY,null)
                }

            })

        }
}