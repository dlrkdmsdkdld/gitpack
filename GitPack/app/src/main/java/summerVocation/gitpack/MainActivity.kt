package summerVocation.gitpack

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import summerVocation.gitpack.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userId = intent.getStringExtra("loginId")
        println(userId)
        /// 로그인한 아이디 확인 자동로그인시 if 문 사용 -> 반대 else문
        if (userId == null){
            val db: SQLiteDatabase = SQLiteDBHelper(this).readableDatabase
            val cursor = db.rawQuery("select * from tb_login",null)
            if(cursor.moveToFirst()){
                binding.mainText.setText(cursor.getString(0))
            }
            db.close()
        }else{
           binding.mainText.setText(userId)
        }


        binding.dropSqliteBtn.setOnClickListener {
            val sqlDrop : String = "DROP TABLE if exists tb_login"
            val db: SQLiteDatabase = SQLiteDBHelper(this).writableDatabase
            db.execSQL(sqlDrop)
        }



    }
}