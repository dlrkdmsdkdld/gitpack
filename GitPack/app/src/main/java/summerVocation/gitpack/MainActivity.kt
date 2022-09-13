package summerVocation.gitpack

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import summerVocation.gitpack.alarm.Alarmmanager
import summerVocation.gitpack.databinding.ActivityMainBinding
import summerVocation.gitpack.viewmodel.calaenderViewModel

class MainActivity : AppCompatActivity() {
    lateinit var userI : String
    private lateinit var mbinding : ActivityMainBinding

    lateinit var mycalaenderViewModel: calaenderViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(mbinding.root)
        mycalaenderViewModel= ViewModelProvider(this).get(calaenderViewModel::class.java)


        var userId = intent.getStringExtra("loginId")

        /// 로그인한 아이디 확인 자동로그인시 if 문 사용 -> 반대 else문
        if (userId == null){
            try{
            val db: SQLiteDatabase = SQLiteDBHelper(this).readableDatabase
            val cursor = db.rawQuery("select * from tb_login",null)
            if(cursor.moveToFirst()){
                userId= cursor.getString(0)
                //binding.mainText.setText(userId)
            }
            db.close()


            }catch (e: Exception){

            }
        }else{
           //binding.mainText.setText(userId)

        }
        userI=userId!!
        mycalaenderViewModel.updateId(userId)

        Whitelist_check(this)


        println(userId)





        //네비게이션들을 담는 호스트
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.myNavHost) as NavHostFragment

        //네비게이션 컨트롤러 가져옴
        val navController = navHostFragment.navController
        //바텀네비게이션뷰와 네비게이션을 묶어준다
        NavigationUI.setupWithNavController(mbinding.myBottomNav , navController)





    }
    fun getuserId() : String{

        return userI
    }

    override fun onDestroy() {
        super.onDestroy()
        val alarm = Alarmmanager(this)
        alarm.setAlarmManager()
       // val intent = Intent(applicationContext,checkTodayCommitService::class.java)
       // startService(intent)
        println("파괴됨")
    }
    fun Whitelist_check(context: Context){
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        var WhiteCheck = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            /**
             * 등록이 되어있따면 TRUE
             * 등록이 안되있다면 FALSE
             */
            WhiteCheck = powerManager.isIgnoringBatteryOptimizations(context.getPackageName())
            if (!WhiteCheck){
                Log.d("화이트리스트", "화이트리스트에 등록되지않았습니다.")
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:" + context.getPackageName())
                context.startActivity(intent)
            }
            else Log.d("화이트리스트","화이트리스트에 등록되어있습니다.");

        }
    }

}


