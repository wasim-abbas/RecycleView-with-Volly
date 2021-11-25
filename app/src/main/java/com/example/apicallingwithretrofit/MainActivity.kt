package com.example.apicallingwithretrofit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileNotFoundException

class MainActivity : AppCompatActivity(), ItemClickListenerCallBack {
    private lateinit var madapter: UniversityAdapter

    companion object {
        private const val TAG = "Hello"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.INTERNET)
        private const val REQUEST_CODE_PERMISSIONS = 160
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Request camera permissions
        if (allPermissionsGranted()) {
            loadRecycview()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }


    }

    fun kprogressHud() {


    }

    fun loadRecycview() {

        myRecycview.layoutManager = LinearLayoutManager(this)
        fetechDAta()
        madapter = UniversityAdapter(this)
        myRecycview.adapter = madapter
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                loadRecycview()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    private fun fetechDAta(): ArrayList<UniversityModelClass> {

        val hud = KProgressHUD.create(this@MainActivity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setMaxProgress(100)
            .show()
        hud.setProgress(90)
        // Instantiate the RequestQueue.
        val url = "http://universities.hipolabs.com/search?country=pakistan"
        val myDatalist = ArrayList<UniversityModelClass>()

        val jsonObject = JsonArrayRequest(Request.Method.GET, url, null,

            {
                for (i in 0 until it.length()) {
                    val myJsonObject = it.getJSONObject(i)
                    val myUniData = UniversityModelClass(
                        myJsonObject.getString("state-province"),
                        myJsonObject.getString("country"),
                        myJsonObject.getString("name"),
                        myJsonObject.getString("alpha_two_code"),
                        myJsonObject.getString("web_pages"),
                        myJsonObject.getString("domains"),
                    )

                    myDatalist.add(myUniData)
                }
                madapter.updateData(myDatalist)
                hud.dismiss()

            },
            {
                Toast.makeText(this, "Failed $it", Toast.LENGTH_SHORT).show()

            })
        MySingleton.getInstance(this).addToRequestQueue(jsonObject)

        return myDatalist
    }

    override fun itemClicked(item: UniversityModelClass) {
        try {
            val builder = CustomTabsIntent.Builder().build()
            val domain: String = item.domin
            val subDomin = domain.substring(2, domain.length - 2)
            val mynewUrl = "http://$subDomin/"
            builder.launchUrl(this, Uri.parse(mynewUrl))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }


    }
}