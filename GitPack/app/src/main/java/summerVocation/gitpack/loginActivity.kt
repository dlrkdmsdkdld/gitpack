package summerVocation.gitpack

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import summerVocation.gitpack.databinding.ActivityLoginBinding

class loginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
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