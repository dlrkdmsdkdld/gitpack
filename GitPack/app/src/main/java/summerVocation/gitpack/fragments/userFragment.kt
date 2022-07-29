package summerVocation.gitpack.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import summerVocation.gitpack.databinding.FragmentRoadmapBinding
import summerVocation.gitpack.databinding.FragmentUserBinding


class userFragment : Fragment() {
    private var mBinding : FragmentUserBinding? =null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUserBinding.inflate(inflater,container,false)
        mBinding =binding
        return mBinding?.root
    }

    override fun onDestroyView() { // 프래그먼트 삭제될때 자동으로실행
        mBinding=null
        super.onDestroyView()
    }
}