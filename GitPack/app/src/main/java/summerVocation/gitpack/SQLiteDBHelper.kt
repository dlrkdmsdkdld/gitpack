package summerVocation.gitpack

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteDBHelper(context: Context) : SQLiteOpenHelper(context,"tb_login",null,1){
    //데이터베이스가 만들어 지지않은 상태에서만 작동합니다. 이미 만들어져 있는 상태라면 실행되지 않습니다.
    override fun onCreate(p0: SQLiteDatabase?) {
        var sql : String = "CREATE TABLE if not exists tb_login (" +
                "_id text);"
        p0?.execSQL(sql)
    }


    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}