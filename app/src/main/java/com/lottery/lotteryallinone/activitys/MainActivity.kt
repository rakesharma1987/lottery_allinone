package com.lottery.lotteryallinone.activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.lottery.lotteryallinone.R
import com.lottery.lotteryallinone.adapter.RVAdapter
import com.lottery.lotteryallinone.databinding.ActivityMainBinding
import com.lottery.lotteryallinone.interfaces.OnItemClickListener1
import com.lottery.lotteryallinone.model.LotteryItem

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.btnPowerballOrMegamillion.setOnClickListener(this)
        binding.btnLotto.setOnClickListener(this)
        binding.btnPic3NPic4.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_lotto ->{

            }

            R.id.btn_Pic3_n_pic4 ->{

            }

            R.id.btn_powerball_or_megamillion ->{
                startActivity(Intent(this, PowerballActivity::class.java))
            }
        }
    }
}