package summerVocation.gitpack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import summerVocation.gitpack.databinding.ActivityDetailRoadmapBinding

class DetailRoadmap : AppCompatActivity() {
    private lateinit var mbinding : ActivityDetailRoadmapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mbinding= ActivityMainBinding.inflate(layoutInflater)
        mbinding=ActivityDetailRoadmapBinding.inflate(layoutInflater)
        setContentView(mbinding.root)
        var touchCnt = intent.getIntExtra("touchCnt",0)
        val imageview = mbinding.detailRoadmapIamgeView
        when(touchCnt){
            0 ->{mbinding.detailText.text = "Frontend 상세 로드맵"
                Glide.with(this)
                    .load("https://roadmap.sh/roadmaps/android/roadmap.png")
                    .centerCrop()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(imageview)
            }
            1 ->{mbinding.detailText.text = "Backend 상세 로드맵"
                Glide.with(this)
                    .load("https://roadmap.sh/roadmaps/android/roadmap.png")
                    .centerCrop()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(imageview)
            }
            2 ->{mbinding.detailText.text = "Android 상세 로드맵"
                Glide.with(this)
                    .load("https://roadmap.sh/roadmaps/android/roadmap.png")
                    .centerCrop()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(imageview)
            }
            3 ->{mbinding.detailText.text = "DBA 상세 로드맵"
                Glide.with(this)
                    .load("https://roadmap.sh/roadmaps/android/roadmap.png")
                    .centerCrop()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(imageview)
            }
            4 ->{mbinding.detailText.text = "Devops 상세 로드맵"
                Glide.with(this)
                    .load("https://roadmap.sh/roadmaps/android/roadmap.png")
                    .centerCrop()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(imageview)
            }
            5 ->{mbinding.detailText.text ="BlockChain 상세 로드맵"
                Glide.with(this)
                    .load("https://roadmap.sh/roadmaps/android/roadmap.png")
                    .centerCrop()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(imageview)
            }

        }


    }
}