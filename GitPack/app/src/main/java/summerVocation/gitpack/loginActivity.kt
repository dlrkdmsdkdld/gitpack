package summerVocation.gitpack

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.rahul.githuboauth.GithubAuthenticator
import summerVocation.gitpack.databinding.ActivityLoginBinding
import summerVocation.gitpack.viewmodel.loginViewModel
import java.util.regex.Pattern

class loginActivity : AppCompatActivity() {
    lateinit var myloginViewModel: loginViewModel
    lateinit var githubAuthenticator:GithubAuthenticator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myloginViewModel= ViewModelProvider(this).get(loginViewModel::class.java)
//        mycalaenderViewModel.checknowday.observe(viewLifecycleOwner, Observer {
//            mBinding!!.todaycommit.text=it
//        })
//        mycalaenderViewModel.scheme.observe(viewLifecycleOwner, Observer {
//            mBinding!!.cvCalendar.setSchemeDate(it)
//        })


        try { // sqlite 데이터 있을때
            val db: SQLiteDatabase = SQLiteDBHelper(this).readableDatabase
            val cursor = db.rawQuery("select * from tb_login",null)
            println("------------------------------------쿼리시작")
            if(cursor.moveToFirst()){
                val inputId = cursor.getString(0)
            }
            db.close()
            val intent =Intent(this,MainActivity::class.java)
            finish()
            startActivity(intent)
        }catch(e: Exception){
            println("---")
        }
//        myloginViewModel.currentUserId.observe(viewLifecycleOwner, Observer {
//
//        })
        binding.loginText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
                val p = Pattern.matches(emailValidation, binding.loginText.text)
                if (binding.loginText.length() > 20){
                    binding.loginText.error = "ID의 글자 수 최대 허용치를 초과하였습니다."
                }else if(p){
                    binding.loginText.error = "아이디만 적어주세요"
                }
                else {
                    binding.loginText.error = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })


//        if(MyApplication.prefs.getString("token","").equals("")){
//            val githubAuthenticatorBuilder  = GithubAuthenticator.builder(this)
//                .clientId(BuildConfig.GITHUB_GLIENT_ID)
//                .clientSecret(BuildConfig.GITHUB_CLIENT_SECRET)
//                .scopeList(arrayListOf("gist", "repo"))
//                .debug(true)
//                .onSuccess(object : SuccessCallback {
//                    override fun onSuccess(result: String) {
//                        runOnUiThread {
//                            MyApplication.prefs.setString("token",result)
//                            println("------------")
//                            println(result)
//                        }
//                    }
//                })
//                .onError(object : ErrorCallback {
//                    override fun onError(error: Exception) {
//                        runOnUiThread {
//                            Toast.makeText(this@loginActivity,error.message, Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                })
//            githubAuthenticatorBuilder.debug(true)
//            githubAuthenticator = githubAuthenticatorBuilder.build()
//        }
        binding.loginButton.setOnClickListener {
            val intent =Intent(this,MainActivity::class.java)
            if (binding.loginText.length()==0){
                binding.loginText.hint = "아이디를 입력하지 않았습니다"
            }else{
                val inputId = binding.loginText.text.toString()
                intent.putExtra("loginId",inputId)
                /////체크박스 체크했을때 단말기 내의 파일에 데이터베이스에 저장
                if (binding.loginCheck.isChecked){
                    val helper = SQLiteDBHelper(this)
                    var Createsql : String = "CREATE TABLE if not exists tb_login (" +
                            "_id text);"
                    val db: SQLiteDatabase = SQLiteDBHelper(this).writableDatabase
                    db.execSQL(Createsql)

                    var contentValue=ContentValues()
                    contentValue.put("_id",inputId)
                    println("db 인서트")
                    db.insert("tb_login",null,contentValue)
                    db.close()
                    

                }



                finish()

                startActivity(intent)
            }

        }


    }

}