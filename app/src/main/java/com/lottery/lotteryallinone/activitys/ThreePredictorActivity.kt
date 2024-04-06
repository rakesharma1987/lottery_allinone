package com.lottery.lotteryallinone.activitys

import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.lottery.lotteryallinone.R
import com.lottery.lotteryallinone.adapter.SixNumberAdapter
import com.lottery.lotteryallinone.adapter.ThreePredictorAdapter
import com.lottery.lotteryallinone.databinding.ActivityThreePredictorBinding
import com.lottery.lotteryallinone.db.MyDatabase
import kotlinx.coroutines.launch

class ThreePredictorActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var context: Context
    private lateinit var binding: ActivityThreePredictorBinding
    private var numberList = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private var intSpinner1: String = ""
    private var intSpinner2: String = ""
    private var intSpinner3: String = ""
    private var intSpinner4: String = ""
    private var intSpinner5: String = ""
    private var intSpinner6: String = ""
    private lateinit var billingClient : BillingClient

    private lateinit var myDatabase: MyDatabase
    private var handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThreePredictorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        supportActionBar!!.title = getString(R.string.app_name)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.purple_200)))
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_200)
        myDatabase = MyDatabase(context)

//        if (!GooglePlayBillingPreferences.isPurchasedForThreeNo()){
//            binding.btnGen40nos.text = "Generate 40 Lines/Rows(Paid Version)"
//        }else{
//            binding.btnGen40nos.text = "Generate 40 Lines/Rows"
//        }
        binding.recyclerview3column.layoutManager = LinearLayoutManager(context)

        val purchasesUpdatedListener =
            PurchasesUpdatedListener { billingResult, purchases ->
                if(billingResult.responseCode == BillingClient.BillingResponseCode.OK){
                    if (purchases != null) {
                        handlePurchases(purchases)
                    }
//                    GooglePlayBillingPreferences.setPurchasedValueForThreeNo(true)
                    binding.btnGen40nos.text = "Generate 40 Lines/Rows"
                }else if(billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED){
//                    GooglePlayBillingPreferences.setPurchasedValueForThreeNo(false)
                    binding.btnGen40nos.text = "Generate 40 Lines/Rows(Paid Version)"
                }
            }

        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        val skulList = ArrayList<String>()
        skulList.add("pick3and4proversion")

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                }
            }
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })

        arrayAdapter =
            ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, numberList)

        binding.spinner1.adapter = arrayAdapter
        binding.spinner2.adapter = arrayAdapter
        binding.spinner3.adapter = arrayAdapter
        binding.spinner4.adapter = arrayAdapter
        binding.spinner5.adapter = arrayAdapter
        binding.spinner6.adapter = arrayAdapter

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

        binding.btnGen2nos.setOnClickListener(this)
        binding.btnGen40nos.setOnClickListener(this)

//        binding.btnGen40nos.setOnClickListener {
//            generate40sRows()
//            if (GooglePlayBillingPreferences.isPurchasedForThreeNo()) {
//                generate40sRows()
//            } else {
//                billingClient.startConnection(object : BillingClientStateListener {
//                    override fun onBillingSetupFinished(billingResult: BillingResult) {
//                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                            val params = SkuDetailsParams.newBuilder()
//                            params.setSkusList(skulList)
//                                .setType(BillingClient.SkuType.INAPP)
//
//                            billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailList ->
//
//                                for (skuDetail in skuDetailList!!) {
//                                    val flowPurchase = BillingFlowParams.newBuilder()
//                                        .setSkuDetails(skuDetail)
//                                        .build()
//
//                                    val responseCode = billingClient.launchBillingFlow(
//                                        this@ThreePredictorActivity,
//                                        flowPurchase
//                                    ).responseCode
//                                    if (responseCode == 0) {
//                                        GooglePlayBillingPreferences.setPurchasedValueForThreeNo(true)
//                                        binding.btnGen40nos.text = "Generate 40 Lines/Rows"
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    override fun onBillingServiceDisconnected() {
//                        // Try to restart the connection on the next request to
//                        // Google Play by calling the startConnection() method.
//                        GooglePlayBillingPreferences.setPurchasedValueForThreeNo(false)
//                    }
//                })
//            }
//
//        }

    }

    private fun generate40sRows(){
        binding.ivAnim.visibility = View.VISIBLE
        handler.postDelayed({
            binding.ivAnim.visibility = View.GONE
            binding.recyclerview3column.visibility = View.VISIBLE
            var sixNumberAdapter = ThreePredictorAdapter(myDatabase.getFourtyRowThreePredctor())
            binding.recyclerview3column.adapter = sixNumberAdapter
        }, 5000)
        lifecycleScope.launch {
            binding.recyclerview3column.visibility = View.GONE
            myDatabase.deleteInsertedDetailsTable()
            myDatabase.deleteUserSelectionTable()

            myDatabase.insertIntoUserSelection(3, 0, 0)
            myDatabase.insertDetails(intSpinner1, intSpinner2, intSpinner3, null!!, null!!, null!!, null!!)
            myDatabase.insertDetails(intSpinner4, intSpinner5, intSpinner6, null!!, null!!, null!!, null!!)
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
    var ackPurchase = AcknowledgePurchaseResponseListener { billingResult ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//            GooglePlayBillingPreferences.setPurchasedValueForThreeNo(true)
            Toast.makeText(applicationContext, "Item Purchased", Toast.LENGTH_SHORT).show()
            recreate()
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_gen_2nos ->{
                binding.ivAnim.visibility = View.VISIBLE
                handler.postDelayed({
                    binding.ivAnim.visibility = View.GONE
                    binding.recyclerview3column.visibility = View.VISIBLE
                    val threePredictorAdapter = ThreePredictorAdapter(myDatabase.getTwoRowForThreePredctor())
                    binding.recyclerview3column.adapter = threePredictorAdapter
                }, 5000)
                lifecycleScope.launch {
                    binding.recyclerview3column.visibility = View.GONE
                    myDatabase.deleteInsertedDetailsTable()
                    myDatabase.deleteUserSelectionTable()

                    myDatabase.insertIntoUserSelection(3, 1, 1)
                    myDatabase.insertDetails(intSpinner1, intSpinner2, intSpinner3, "null!!", "null!!", null!!, "null!!")
                    myDatabase.insertDetails(intSpinner4, intSpinner5, intSpinner6, "null!!", "null!!", "null!!", "null!!")
                }
            }

            R.id.btn_gen_40nos ->{
                generate40sRows()
            }
        }
    }

}