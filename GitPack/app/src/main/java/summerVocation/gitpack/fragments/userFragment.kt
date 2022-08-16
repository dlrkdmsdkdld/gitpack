package summerVocation.gitpack.fragments

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import summerVocation.gitpack.SQLiteDBHelper
import summerVocation.gitpack.databinding.FragmentUserBinding


class userFragment : Fragment() {
    private var mBinding : FragmentUserBinding? =null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUserBinding.inflate(inflater,container,false)
        binding.btnlogout.setOnClickListener {
            // sqlite 테이블 없애는 함수
            val sqlDrop : String = "DROP TABLE if exists tb_login"
            val db: SQLiteDatabase = SQLiteDBHelper(requireContext()).writableDatabase
            db.execSQL(sqlDrop)

        }


        mBinding =binding
        return mBinding?.root
    }

    override fun onDestroyView() { // 프래그먼트 삭제될때 자동으로실행
        mBinding=null
        super.onDestroyView()
    }
}