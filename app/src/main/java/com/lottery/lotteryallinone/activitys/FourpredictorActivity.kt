package com.lottery.lotteryallinone.activitys

import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.BillingClient
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.lottery.lotteryallinone.R
import com.lottery.lotteryallinone.adapter.FourPredictorAdapter
import com.lottery.lotteryallinone.adapter.ThreePredictorAdapter
import com.lottery.lotteryallinone.databinding.ActivityFourpredictorBinding
import com.lottery.lotteryallinone.db.MyDatabase
import kotlinx.coroutines.launch

class FourpredictorActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFourpredictorBinding
    private lateinit var context: Context
    private var numberList = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private var intSpinner1: String = ""
    private var intSpinner2: String = ""
    private var intSpinner3: String = ""
    private var intSpinner4: String = ""
    private var intSpinner5: String = ""
    private var intSpinner6: String = ""
    private var intSpinner7: String = ""
    private var intSpinner8: String = ""
    private lateinit var billingClient : BillingClient

    private lateinit var myDatabase: MyDatabase
    private var handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFourpredictorBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        if (GooglePlayBillingPreferences.isPurchasedForFourNo()){
//            binding.btnGen40nos.text = "Generate 40 Lines/Rows"
//        }else{
//            binding.btnGen40nos.text = "Generate 40 Lines/Rows(Paid Version)"
//        }

        supportActionBar!!.title = getString(R.string.app_name)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.purple_200)))
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_200)

        context = this
        myDatabase = MyDatabase(context)
        binding.recyclerview4column.layoutManager = LinearLayoutManager(context)
        Glide.with(this).load(R.drawable.iv_anim).into(binding.ivAnim)

//        val purchasesUpdatedListener =
//            PurchasesUpdatedListener { billingResult, purchases ->
//                if(billingResult.responseCode == BillingClient.BillingResponseCode.OK){
//                    if (purchases != null) {
//                        handlePurchases(purchases)
//                    }
//                    GooglePlayBillingPreferences.setPurchasedValueForFourNo(true)
//                    binding.btnGen40nos.text = "Generate 40 Lines/Rows"
//                }else if(billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED){
//                    GooglePlayBillingPreferences.setPurchasedValueForFourNo(false)
//                    binding.btnGen40nos.text = "Generate 40 Lines/Rows(Paid Version)"
//                }
//            }

//        billingClient = BillingClient.newBuilder(context)
//            .setListener(purchasesUpdatedListener)
//            .enablePendingPurchases()
//            .build()

//        val skulList = ArrayList<String>()
//        skulList.add("pick3and4proversion")
//
//        billingClient.startConnection(object : BillingClientStateListener {
//            override fun onBillingSetupFinished(billingResult: BillingResult) {
//                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
//                    // The BillingClient is ready. You can query purchases here.
//                }
//            }
//            override fun onBillingServiceDisconnected() {
//                // Try to restart the connection on the next request to
//                // Google Play by calling the startConnection() method.
//            }
//        })

        arrayAdapter =
            ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, numberList)

        binding.spinner1.adapter = arrayAdapter
        binding.spinner2.adapter = arrayAdapter
        binding.spinner3.adapter = arrayAdapter
        binding.spinner4.adapter = arrayAdapter
        binding.spinner5.adapter = arrayAdapter
        binding.spinner6.adapter = arrayAdapter
        binding.spinner7.adapter = arrayAdapter
        binding.spinner8.adapter = arrayAdapter

        binding.spinner1.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                intSpinner1 = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        binding.spinner2.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                intSpinner2 = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        binding.spinner3.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                intSpinner3 = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        binding.spinner4.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                intSpinner4 = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        binding.spinner5.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                intSpinner5 = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        binding.spinner6.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                intSpinner6 = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        binding.spinner6.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                intSpinner6 = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        binding.spinner7.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                intSpinner7 = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        binding.spinner8.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                intSpinner8 = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        binding.btnGen2nos.setOnClickListener(View.OnClickListener {
            binding.ivAnim.visibility = View.VISIBLE
            handler.postDelayed({
                binding.ivAnim.visibility = View.GONE
                binding.recyclerview4column.visibility = View.VISIBLE
                var threePredictorAdapter: FourPredictorAdapter = FourPredictorAdapter(myDatabase.getTwoRowForFourPredctor())
                binding.recyclerview4column.adapter = threePredictorAdapter
            }, 5000)
            lifecycleScope.launch {
                binding.recyclerview4column.visibility = View.GONE
                myDatabase.deleteInsertedDetailsTable()
                myDatabase.deleteUserSelectionTable()

                myDatabase.insertIntoUserSelection(3, 0, 0)
                myDatabase.insertDetails(intSpinner1, intSpinner2, intSpinner3, intSpinner4, "0", "0", "0")
                myDatabase.insertDetails(intSpinner5, intSpinner6, intSpinner7, intSpinner8, "0", "0", "0")
            }
        })

        binding.btnGen40nos.setOnClickListener {
            generate40sRows()
            /*if (GooglePlayBillingPreferences.isPurchasedForFourNo()) {
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
                                        this@FourpredictorActivity,
                                        flowPurchase
                                    ).responseCode
                                    if (responseCode == 0) {
                                        GooglePlayBillingPreferences.setPurchasedValueForFourNo(true)
                                        binding.btnGen40nos.text = "Generate 40 Lines/Rows"
                                    }
                                }
                            }
                        }
                    }

                    override fun onBillingServiceDisconnected() {
                        // Try to restart the connection on the next request to
                        // Google Play by calling the startConnection() method.
                        GooglePlayBillingPreferences.setPurchasedValueForFourNo(false)
                    }
                })
            }*/
        }
    }

    private fun generate40sRows() {
        binding.ivAnim.visibility = View.VISIBLE
        handler.postDelayed({
            binding.ivAnim.visibility = View.GONE
            binding.recyclerview4column.visibility = View.VISIBLE
            var sixNumberAdapter = ThreePredictorAdapter(myDatabase.getFourtyRowThreePredctor())
            binding.recyclerview4column.adapter = sixNumberAdapter
        }, 5000)
        lifecycleScope.launch {
            binding.recyclerview4column.visibility = View.GONE
            myDatabase.deleteInsertedDetailsTable()
            myDatabase.deleteUserSelectionTable()

            myDatabase.insertIntoUserSelection(3, 0, 0)
            myDatabase.insertDetails(intSpinner1, intSpinner2, intSpinner3, intSpinner4, "0", "0", "0")
            myDatabase.insertDetails(intSpinner5, intSpinner6, intSpinner7, intSpinner8, "0", "0", "0")
        }
    }

//    fun handlePurchases(purchases: List<Purchase>) {
//        for (purchase in purchases) {
//            if (!purchase.isAcknowledged) {
//                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
//                    .setPurchaseToken(purchase.purchaseToken)
//                    .build()
//                billingClient!!.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase)
//            }
//        }
//    }
//    var ackPurchase = AcknowledgePurchaseResponseListener { billingResult ->
//        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//            GooglePlayBillingPreferences.setPurchasedValueForFourNo(true)
//            Toast.makeText(applicationContext, "Item Purchased", Toast.LENGTH_SHORT).show()
//            recreate()
//        }
//    }
}