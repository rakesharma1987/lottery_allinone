package com.lottery.lotteryallinone.activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lottery.lotteryallinone.R
import com.lottery.lotteryallinone.databinding.ActivityPic34HomeBinding
import com.pick3andpick4pred.suvarnatechlabs.activity.FourpredictorActivity

class Pic34HomeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityPic34HomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPic34HomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPick3.setOnClickListener(this)
        binding.btnPick4.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_pick3 ->{
                startActivity(Intent(this, ThreePredictorActivity::class.java))
            }

            R.id.btn_pick4 ->{
                startActivity(Intent(this, FourpredictorActivity::class.java))
            }
        }
    }
}