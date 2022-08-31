package summerVocation.gitpack.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.regex.Pattern

class loginViewModel( application: Application) : AndroidViewModel(application){
    private var userId = MutableLiveData<String>()
    val currentUserId: LiveData<String>
        get() = userId
    fun updateId(input:String){
        userId.value=input
    }

    fun clickloginBtn(){

    }
    fun changeLoginId(userId: String):String{
                val emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
                val p = Pattern.matches(emailValidation, userId)
                if (userId.length > 20){
                    val error = "ID의 글자 수 최대 허용치를 초과하였습니다."
                    return error
                }else if(p){
                    val error = "아이디만 적어주세요"
                    return error
                }
                else {
                    return "null"
                }
    }

}