package com.lottery.lotteryallinone.activitys
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetailsParams
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.lottery.lotteryallinone.R
import com.lottery.lotteryallinone.adapter.MyNumbersAdapter
import com.lottery.lotteryallinone.adapter.SixNumberAdapter
import com.lottery.lotteryallinone.databinding.ActivityPowerballBinding
import com.lottery.lotteryallinone.databinding.LayoutDialogNumbersBinding
import com.lottery.lotteryallinone.db.MyDatabase
import com.lottery.lotteryallinone.interfaces.RecyclerItemClickListener
import com.lottery.lotteryallinone.prefs.Preferences
import kotlinx.coroutines.launch

class PowerballActivity : BaseActivity(), View.OnClickListener {
    private lateinit var context: Context
    private lateinit var binding: ActivityPowerballBinding
    private lateinit var dialogBinding: LayoutDialogNumbersBinding
    private lateinit var dialog: Dialog
    private var selectedNumber = ArrayList<String>()
    private val listOfFirst5Cols = listOf<String>(
        "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
        "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
        "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
        "61", "62", "63", "64", "65", "66", "67", "68", "69", "70",
        "71", "72", "73", "74", "75", "76", "77", "78", "79", "80"
    )
    private val listOfFirst6Col = listOf<String>(
        "10",
        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
        "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
        "31", "32", "33", "34", "35", "36", "37", "38", "39", "40"
    )

    private var sumCol17: Int? = 0
    private var sumCol28: Int? = 0
    private var sumCol39: Int? = 0
    private var sumCol410: Int? = 0
    private var sumCol511: Int? = 0
    private var sumCol612: Int? = 0

    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "MainActivity"
    private lateinit var billingClient: BillingClient
    lateinit var skulList: ArrayList<String>
    private lateinit var fbInterstitialAdd: InterstitialAd
    private lateinit var myDatabase: MyDatabase
    private var handler: Handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPowerballBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        myDatabase = MyDatabase(context)

        MobileAds.initialize(this)
        binding.bannerAdview.loadAd(AdRequest.Builder().build())

        Glide.with(this).load(R.drawable.iv_anim).into(binding.ivAnim)

        binding.recyclerview6column.layoutManager = LinearLayoutManager(this)

        binding.llFirst5col.setOnClickListener(this)
        binding.llFirst6col.setOnClickListener(this)
        binding.llTv1.setOnClickListener(this)
        binding.llTv2.setOnClickListener(this)
        binding.llTv3.setOnClickListener(this)
        binding.llTv4.setOnClickListener(this)
        binding.llTv5.setOnClickListener(this)
        binding.llTv6.setOnClickListener(this)
        binding.llTv7.setOnClickListener(this)
        binding.llTv8.setOnClickListener(this)
        binding.llTv9.setOnClickListener(this)
        binding.llTv10.setOnClickListener(this)
        binding.llTv11.setOnClickListener(this)
        binding.llTv12.setOnClickListener(this)

        binding.btnGen2nos.setOnClickListener(this)
        binding.btnGen40nos.setOnClickListener(this)

        if (!Preferences.isPurchased()) {
            binding.btnGen40nos.text = "Generate 40's Lines/Rows(Paid Version)"
        } else {
            binding.btnGen40nos.text = "Generate 40's Lines/Rows"
        }

        val purchasesUpdatedListener =
            PurchasesUpdatedListener { billingResult, purchases ->

                if(billingResult.responseCode == BillingClient.BillingResponseCode.OK){
                    if (purchases != null) {
                        handlePurchases(purchases)
                    }
                    Preferences.setPurchased(true)
                    binding.btnGen40nos.text = "Generate 40's Lines/Rows"
                }else if(billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED){
                    Preferences.setPurchased(false)
                    binding.btnGen40nos.text = "Generate 40's Lines/Rows(Paid Version)"
                }
            }

        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        skulList = ArrayList<String>()
        skulList.add("noadspaidversion_pb_generator")

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })

    }

    override fun onClick(view: View?) {
        dialogBinding = LayoutDialogNumbersBinding.inflate(layoutInflater)
        dialogBinding.rvNumbers.layoutManager = GridLayoutManager(this@PowerballActivity, 10)

        dialog = Dialog(this@PowerballActivity)
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(false)
        val window = dialog.window
        window!!.setLayout(
            GridLayout.LayoutParams.MATCH_PARENT,
            GridLayout.LayoutParams.WRAP_CONTENT
        )

        when (view!!.id) {
            R.id.ll_first_5col -> {
                dialog.show()
                val adapter = MyNumbersAdapter(listOfFirst5Cols, selectedNumber)
                dialogBinding.rvNumbers.adapter = adapter
                dialogBinding.tvClose.setOnClickListener {
                    dialog.dismiss()
                }

                dialogBinding.rvNumbers.addOnItemTouchListener(
                    RecyclerItemClickListener(this@PowerballActivity, dialogBinding.rvNumbers,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                binding.tv1First5column.text = listOfFirst5Cols[position]
                                dialog.dismiss()
                            }

                            override fun onItemLongClick(view: View?, position: Int) {
                                TODO("Not yet implemented")
                            }
                        })
                )

            }

            R.id.ll_first_6col -> {
                dialog.show()
                val adapter = MyNumbersAdapter(listOfFirst6Col, selectedNumber)
                dialogBinding.rvNumbers.adapter = adapter
                dialogBinding.tvClose.setOnClickListener {
                    dialog.dismiss()
                }

                dialogBinding.rvNumbers.addOnItemTouchListener(
                    RecyclerItemClickListener(this@PowerballActivity, dialogBinding.rvNumbers,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                binding.tv1First6column.text = listOfFirst6Col[position]
                                dialog.dismiss()
                            }

                            override fun onItemLongClick(view: View?, position: Int) {
                                TODO("Not yet implemented")
                            }
                        })
                )
            }

            R.id.ll_tv1 -> {
                if (binding.tv1First5column.text == "-") {
                    customToast("First, Select a number from first 5 box.")
                    return
                }
                dialog.show()

                var list = ArrayList<String>()
                for (i in 1..Integer.parseInt(binding.tv1First5column.text.toString())) {
                    list.add(i.toString())
                }
                val adapter = MyNumbersAdapter(list, selectedNumber)
                dialogBinding.rvNumbers.adapter = adapter
                dialogBinding.tvClose.setOnClickListener {
                    dialog.dismiss()
                }

                dialogBinding.rvNumbers.addOnItemTouchListener(
                    RecyclerItemClickListener(this@PowerballActivity, dialogBinding.rvNumbers,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                if (selectedNumber.isNotEmpty() && selectedNumber.contains(list[position])) {
                                    customToast("Already selected, please select another number.")
                                } else {
                                    binding.tv1.text = list[position]
                                    selectedNumber.add(list[position])
                                    dialog.dismiss()
                                }
                            }

                            override fun onItemLongClick(view: View?, position: Int) {
                                TODO("Not yet implemented")
                            }
                        })
                )

            }

            R.id.ll_tv2 -> {
                if (binding.tv1First5column.text == "-") {
                    customToast("First, Select a number from first 5 box.")
                    return
                }
                dialog.show()

                var list = ArrayList<String>()
                for (i in 1..Integer.parseInt(binding.tv1First5column.text.toString())) {
                    list.add(i.toString())
                }
                val adapter = MyNumbersAdapter(list, selectedNumber)
                dialogBinding.rvNumbers.adapter = adapter
                dialogBinding.tvClose.setOnClickListener {
                    dialog.dismiss()
                }

                dialogBinding.rvNumbers.addOnItemTouchListener(
                    RecyclerItemClickListener(this@PowerballActivity, dialogBinding.rvNumbers,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                if (selectedNumber.isNotEmpty() && selectedNumber.contains(list[position])) {
                                    customToast("Already selected, please select another number.")
                                } else {
                                    binding.tv2.text = list[position]
                                    selectedNumber.add(list[position])
                                    dialog.dismiss()
                                }
                            }

                            override fun onItemLongClick(view: View?, position: Int) {
                                TODO("Not yet implemented")
                            }
                        })
                )

            }

            R.id.ll_tv3 -> {
                if (binding.tv1First5column.text == "-") {
                    customToast("First, Select a number from first 5 box.")
                    return
                }
                dialog.show()

                var list = ArrayList<String>()
                for (i in 1..Integer.parseInt(binding.tv1First5column.text.toString())) {
                    list.add(i.toString())
                }
                val adapter = MyNumbersAdapter(list, selectedNumber)
                dialogBinding.rvNumbers.adapter = adapter
                dialogBinding.tvClose.setOnClickListener {
                    dialog.dismiss()
                }

                dialogBinding.rvNumbers.addOnItemTouchListener(
                    RecyclerItemClickListener(this@PowerballActivity, dialogBinding.rvNumbers,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                if (selectedNumber.isNotEmpty() && selectedNumber.contains(list[position])) {
                                    customToast("Already selected, please select another number.")
                                } else {
                                    binding.tv3.text = list[position]
                                    selectedNumber.add(list[position])
                                    dialog.dismiss()
                                }
                            }

                            override fun onItemLongClick(view: View?, position: Int) {
                                TODO("Not yet implemented")
                            }
                        })
                )

            }

            R.id.ll_tv4 -> {
                if (binding.tv1First5column.text == "-") {
                    customToast("First, Select a number from first 5 box.")
                    return
                }
                dialog.show()

                var list = ArrayList<String>()
                for (i in 1..Integer.parseInt(binding.tv1First5column.text.toString())) {
                    list.add(i.toString())
                }
                val adapter = MyNumbersAdapter(list, selectedNumber)
                dialogBinding.rvNumbers.adapter = adapter
                dialogBinding.tvClose.setOnClickListener {
                    dialog.dismiss()
                }

                dialogBinding.rvNumbers.addOnItemTouchListener(
                    RecyclerItemClickListener(this@PowerballActivity, dialogBinding.rvNumbers,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                if (selectedNumber.isNotEmpty() && selectedNumber.contains(list[position])) {
                                    customToast("Already selected, please select another number.")
                                } else {
                                    binding.tv4.text = list[position]
                                    selectedNumber.add(list[position])
                                    dialog.dismiss()
                                }
                            }

                            override fun onItemLongClick(view: View?, position: Int) {
                                TODO("Not yet implemented")
                            }
                        })
                )

            }

            R.id.ll_tv5 -> {
                if (binding.tv1First5column.text == "-") {
                    customToast("First, Select a number from first 5 box.")
                    return
                }
                dialog.show()

                var list = ArrayList<String>()
                for (i in 1..Integer.parseInt(binding.tv1First5column.text.toString())) {
                    list.add(i.toString())
                }
                val adapter = MyNumbersAdapter(list, selectedNumber)
                dialogBinding.rvNumbers.adapter = adapter
                dialogBinding.tvClose.setOnClickListener {
                    dialog.dismiss()
                }

                dialogBinding.rvNumbers.addOnItemTouchListener(
                    RecyclerItemClickListener(this@PowerballActivity, dialogBinding.rvNumbers,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                if (selectedNumber.isNotEmpty() && selectedNumber.contains(list[position])) {
                                    customToast("Already selected, please select another number.")
                                } else {
                                    binding.tv5.text = list[position]
                                    selectedNumber.add(list[position])
                                    dialog.dismiss()
                                }
                            }

                            override fun onItemLongClick(view: View?, position: Int) {
                                TODO("Not yet implemented")
                            }
                        })
                )

            }

            R.id.ll_tv6 -> {
                if (binding.tv1First6column.text == "-") {
                    customToast("First, Select a number from first 6 box.")
                    return
                } else if (binding.tv1.text.toString() == "-" || binding.tv2.text.toString() == "-" || binding.tv3.text.toString() == "-"
                    || binding.tv4.text.toString() == "-" || binding.tv5.text.toString() == "-"
                ) {
                    customToast("Please select previous box/boxes of this row.")
                    return
                }
                dialog.show()

                var list = ArrayList<String>()
                for (i in 1..Integer.parseInt(binding.tv1First6column.text.toString())) {
                    list.add(i.toString())
                }
                val adapter = MyNumbersAdapter(list, selectedNumber)
                dialogBinding.rvNumbers.adapter = adapter
                dialogBinding.tvClose.setOnClickListener {
                    dialog.dismiss()
                }

                dialogBinding.rvNumbers.addOnItemTouchListener(
                    RecyclerItemClickListener(this@PowerballActivity, dialogBinding.rvNumbers,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                if (selectedNumber.isNotEmpty() && selectedNumber.contains(list[position])) {
                                    customToast("Already selected, please select another number.")
                                } else {
                                    binding.tv6.text = list[position]
                                    selectedNumber.clear()
                                    dialog.dismiss()
                                }
                            }

                            override fun onItemLongClick(view: View?, position: Int) {
                                TODO("Not yet implemented")
                            }
                        })
                )

            }

            R.id.ll_tv7 -> {
                if (binding.tv1First5column.text == "-") {
                    customToast("First, Select a number from first 5 box.")
                    return
                }
                dialog.show()

                var list = ArrayList<String>()
                for (i in 1..Integer.parseInt(binding.tv1First5column.text.toString())) {
                    list.add(i.toString())
                }
                val adapter = MyNumbersAdapter(list, selectedNumber)
                dialogBinding.rvNumbers.adapter = adapter
                dialogBinding.tvClose.setOnClickListener {
                    dialog.dismiss()
                }

                dialogBinding.rvNumbers.addOnItemTouchListener(
                    RecyclerItemClickListener(this@PowerballActivity, dialogBinding.rvNumbers,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                if (selectedNumber.isNotEmpty() && selectedNumber.contains(list[position])) {
                                    customToast("Already selected, please select another number.")
                                }
                                binding.tv7.text = list[position]
                                selectedNumber.add(list[position])
                                dialog.dismiss()
                            }

                            override fun onItemLongClick(view: View?, position: Int) {
                                TODO("Not yet implemented")
                            }
                        })
                )

            }

            R.id.ll_tv8 -> {
                if (binding.tv1First5column.text == "-") {
                    customToast("First, Select a number from first 5 box.")
                    return
                }
                dialog.show()

                var list = ArrayList<String>()
                for (i in 1..Integer.parseInt(binding.tv1First5column.text.toString())) {
                    list.add(i.toString())
                }
                val adapter = MyNumbersAdapter(list, selectedNumber)
                dialogBinding.rvNumbers.adapter = adapter
                dialogBinding.tvClose.setOnClickListener {
                    dialog.dismiss()
                }

                dialogBinding.rvNumbers.addOnItemTouchListener(
                    RecyclerItemClickListener(this@PowerballActivity, dialogBinding.rvNumbers,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                if (selectedNumber.isNotEmpty() && selectedNumber.contains(list[position])) {
                                    customToast("Already selected, please select another number.")
                                } else {
                                    binding.tv8.text = list[position]
                                    selectedNumber.add(list[position])
                                    dialog.dismiss()
                                }
                            }

                            override fun onItemLongClick(view: View?, position: Int) {
                                TODO("Not yet implemented")
                            }
                        })
                )

            }

            R.id.ll_tv9 -> {
                if (binding.tv1First5column.text == "-") {
                    customToast("First, Select a number from first 5 box.")
                    return
                }
                dialog.show()

                var list = ArrayList<String>()
                for (i in 1..Integer.parseInt(binding.tv1First5column.text.toString())) {
                    list.add(i.toString())
                }
                val adapter = MyNumbersAdapter(list, selectedNumber)
                dialogBinding.rvNumbers.adapter = adapter
                dialogBinding.tvClose.setOnClickListener {
                    dialog.dismiss()
                }

                dialogBinding.rvNumbers.addOnItemTouchListener(
                    RecyclerItemClickListener(this@PowerballActivity, dialogBinding.rvNumbers,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                if (selectedNumber.isNotEmpty() && selectedNumber.contains(list[position])) {
                                    customToast("Already selected, please select another number.")
                                } else {
                                    binding.tv9.text = list[position]
                                    selectedNumber.add(list[position])
                                    dialog.dismiss()
                                }
                            }

                            override fun onItemLongClick(view: View?, position: Int) {
                                TODO("Not yet implemented")
                            }
                        })
                )

            }

            R.id.ll_tv10 -> {
                if (binding.tv1First5column.text == "-") {
                    customToast("First, Select a number from first 5 box.")
                    return
                }
                dialog.show()

                var list = ArrayList<String>()
                for (i in 1..Integer.parseInt(binding.tv1First5column.text.toString())) {
                    list.add(i.toString())
                }
                val adapter = MyNumbersAdapter(list, selectedNumber)
                dialogBinding.rvNumbers.adapter = adapter
                dialogBinding.tvClose.setOnClickListener {
                    dialog.dismiss()
                }

                dialogBinding.rvNumbers.addOnItemTouchListener(
                    RecyclerItemClickListener(this@PowerballActivity, dialogBinding.rvNumbers,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                if (selectedNumber.isNotEmpty() && selectedNumber.contains(list[position])) {
                                    customToast("Already selected, please select another number.")
                                } else {
                                    binding.tv10.text = list[position]
                                    selectedNumber.add(list[position])
                                    dialog.dismiss()
                                }
                            }

                            override fun onItemLongClick(view: View?, position: Int) {
                                TODO("Not yet implemented")
                            }
                        })
                )

            }

            R.id.ll_tv11 -> {
                if (binding.tv1First5column.text == "-") {
                    customToast("First, Select a number from first 5 box.")
                    return
                }
                dialog.show()

                var list = ArrayList<String>()
                for (i in 1..Integer.parseInt(binding.tv1First5column.text.toString())) {
                    list.add(i.toString())
                }
                val adapter = MyNumbersAdapter(list, selectedNumber)
                dialogBinding.rvNumbers.adapter = adapter
                dialogBinding.tvClose.setOnClickListener {
                    dialog.dismiss()
                }

                dialogBinding.rvNumbers.addOnItemTouchListener(
                    RecyclerItemClickListener(this@PowerballActivity, dialogBinding.rvNumbers,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                if (selectedNumber.isNotEmpty() && selectedNumber.contains(list[position])) {
                                    customToast("Already selected, please select another number.")
                                } else {
                                    binding.tv11.text = list[position]
                                    selectedNumber.add(list[position])
                                    dialog.dismiss()
                                }
                            }

                            override fun onItemLongClick(view: View?, position: Int) {
                                TODO("Not yet implemented")
                            }
                        })
                )

            }

            R.id.ll_tv12 -> {
                if (binding.tv1First6column.text == "-") {
                    customToast("First, Select a number from first 6 box.")
                    return
                } else if (binding.tv7.text.toString() == "-" || binding.tv8.text.toString() == "-" || binding.tv9.text.toString() == "-"
                    || binding.tv10.text.toString() == "-" || binding.tv11.text.toString() == "-"
                ) {
                    customToast("Please select previous box/boxes of this row.")
                    return
                }
                dialog.show()

                var list = ArrayList<String>()
                for (i in 1..Integer.parseInt(binding.tv1First6column.text.toString())) {
                    list.add(i.toString())
                }
                val adapter = MyNumbersAdapter(list, selectedNumber)
                dialogBinding.rvNumbers.adapter = adapter
                dialogBinding.tvClose.setOnClickListener {
                    dialog.dismiss()
                }

                dialogBinding.rvNumbers.addOnItemTouchListener(
                    RecyclerItemClickListener(this@PowerballActivity, dialogBinding.rvNumbers,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                if (selectedNumber.isNotEmpty() && selectedNumber.contains(list[position])) {
                                    customToast("Already selected, please select another number.")
                                } else {
                                    binding.tv12.text = list[position]
                                    selectedNumber.clear()
                                    dialog.dismiss()
                                }
                            }

                            override fun onItemLongClick(view: View?, position: Int) {
                                TODO("Not yet implemented")
                            }
                        })
                )

            }

            R.id.btn_gen_2nos -> {
                var adRequest = AdRequest.Builder().build()
                InterstitialAd.load(
                    this,
                    "ca-app-pub-3164749634609559/5015515507",
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            adError?.toString()?.let { Log.d(TAG, it) }
                            mInterstitialAd = null
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            Log.d(TAG, "Ad was loaded.")
                            mInterstitialAd = interstitialAd
                        }
                    })

                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(this)
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
                }

                if (binding.tv1First5column.text.toString() == "-" || binding.tv1First6column.text.toString() == "-"
                    || binding.tv1.text.toString() == "-" || binding.tv2.text.toString() == "-" || binding.tv3.text.toString() == "-"
                    || binding.tv4.text.toString() == "-" || binding.tv5.text.toString() == "-" || binding.tv6.text.toString() == "-"
                    || binding.tv7.text.toString() == "-" || binding.tv8.text.toString() == "-" || binding.tv9.text.toString() == "-"
                    || binding.tv10.text.toString() == "-" || binding.tv11.text.toString() == "-" || binding.tv12.text.toString() == "-"
                ) {
                    customToast("Result can't predict, please select number by clicking box.")
                    return
                }else{
                    binding.ivAnim.visibility = View.VISIBLE
                    handler.postDelayed({
                        binding.ivAnim.visibility = View.GONE
                        binding.recyclerview6column.visibility = View.VISIBLE
                        var sixNumberAdapter = SixNumberAdapter(myDatabase.getTwoRow())
                        binding.recyclerview6column.adapter = sixNumberAdapter
                    }, 5000)
                   lifecycleScope.launch {
                       binding.recyclerview6column.visibility = View.GONE
                       myDatabase.deleteInsertedDetailsTable()
                       myDatabase.deleteUserSelectionTable()

                       myDatabase.insertIntoUserSelection(6, binding.tv1First5column.text.toString().toInt(), binding.tv1First6column.text.toString().toInt())
                       myDatabase.insertDetails(binding.tv1.text.toString(), binding.tv2.text.toString(), binding.tv3.text.toString(), binding.tv4.text.toString(), binding.tv5.text.toString(), binding.tv6.text.toString(), "0")
                       myDatabase.insertDetails(binding.tv7.text.toString(), binding.tv8.text.toString(), binding.tv9.text.toString(), binding.tv10.text.toString(), binding.tv11.text.toString(), binding.tv12.text.toString(), "0")
                   }
                }

            }

            R.id.btn_gen_40nos -> {
                generate40sRows()
                /*if (Preferences.isPurchased()) {
                    generate40sRows()
                } else {
                    billingClient.startConnection(object : BillingClientStateListener {
                        override fun onBillingSetupFinished(billingResult: BillingResult) {
                            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                val params = SkuDetailsParams.newBuilder()
                                params.setSkusList(skulList)
                                    .setType(BillingClient.SkuType.INAPP)

                                billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailList ->

                                    for (skuDetail in skuDetailList!!) {
                                        val flowPurchase = BillingFlowParams.newBuilder()
                                            .setSkuDetails(skuDetail)
                                            .build()

                                        val responseCode = billingClient.launchBillingFlow(
                                            this@PowerballActivity,
                                            flowPurchase
                                        ).responseCode
                                        if (responseCode == 0) {
//                                            Preferences.setPurchased(true)
//                                            binding.btnGen40nos.text = "Generate 40's Lines/Rows"
                                        }
                                    }
                                }
                            }
                        }

                        override fun onBillingServiceDisconnected() {
                            // Try to restart the connection on the next request to
                            // Google Play by calling the startConnection() method.
//                            Preferences.setPurchased(false)
                        }
                    })
                }*/

            }

        }
    }

    private fun generate40sRows() {
        if (binding.tv1First5column.text.toString() == "-" || binding.tv1First6column.text.toString() == "-"
            || binding.tv1.text.toString() == "-" || binding.tv2.text.toString() == "-" || binding.tv3.text.toString() == "-"
            || binding.tv4.text.toString() == "-" || binding.tv5.text.toString() == "-" || binding.tv6.text.toString() == "-"
            || binding.tv7.text.toString() == "-" || binding.tv8.text.toString() == "-" || binding.tv9.text.toString() == "-"
            || binding.tv10.text.toString() == "-" || binding.tv11.text.toString() == "-" || binding.tv12.text.toString() == "-"
        ) {
            customToast("Result can't predict, please select number by clicking box.")
            return
        }else{
            binding.ivAnim.visibility = View.VISIBLE
            handler.postDelayed({
                binding.ivAnim.visibility = View.GONE
                binding.recyclerview6column.visibility = View.VISIBLE
                var sixNumberAdapter = SixNumberAdapter(myDatabase.getFourtyRow())
                binding.recyclerview6column.adapter = sixNumberAdapter
            }, 5000)
            lifecycleScope.launch {
                binding.recyclerview6column.visibility = View.GONE
                myDatabase.deleteInsertedDetailsTable()
                myDatabase.deleteUserSelectionTable()

                myDatabase.insertIntoUserSelection(6, binding.tv1First5column.text.toString().toInt(), binding.tv1First6column.text.toString().toInt())
                myDatabase.insertDetails(binding.tv1.text.toString(), binding.tv2.text.toString(), binding.tv3.text.toString(), binding.tv4.text.toString(), binding.tv5.text.toString(), binding.tv6.text.toString(), "0")
                myDatabase.insertDetails(binding.tv7.text.toString(), binding.tv8.text.toString(), binding.tv9.text.toString(), binding.tv10.text.toString(), binding.tv11.text.toString(), binding.tv12.text.toString(), "0")
            }
        }
    }



    private fun handlePurchases(purchases: List<Purchase>) {
        for (purchase in purchases) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                billingClient!!.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase)
            }
        }
    }
    private var ackPurchase = AcknowledgePurchaseResponseListener { billingResult ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            Preferences.setPurchased(true)
            Toast.makeText(applicationContext, "Item Purchased", Toast.LENGTH_SHORT).show()
            recreate()
        }
    }
}

