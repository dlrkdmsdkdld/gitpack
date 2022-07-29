package summerVocation.gitpack

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import summerVocation.gitpack.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mbinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(mbinding.root)

        var userId = intent.getStringExtra("loginId")
        println(userId)
        /// 로그인한 아이디 확인 자동로그인시 if 문 사용 -> 반대 else문
        if (userId == null){
            val db: SQLiteDatabase = SQLiteDBHelper(this).readableDatabase
            val cursor = db.rawQuery("select * from tb_login",null)
            if(cursor.moveToFirst()){
                userId= cursor.getString(0)
                //binding.mainText.setText(userId)
            }
            db.close()
        }else{
           //binding.mainText.setText(userId)
        }
        //네비게이션들을 담는 호스트
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.myNavHost) as NavHostFragment

        //네비게이션 컨트롤러 가져옴
        val navController = navHostFragment.navController
        //바텀네비게이션뷰와 네비게이션을 묶어준다
        NavigationUI.setupWithNavController(mbinding.myBottomNav , navController)



////// sqlite 테이블 없애는 함수
//        binding.dropSqliteBtn.setOnClickListener {
//            val sqlDrop : String = "DROP TABLE if exists tb_login"
//            val db: SQLiteDatabase = SQLiteDBHelper(this).writableDatabase
//            db.execSQL(sqlDrop)
//        }



    }
}