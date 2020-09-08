package com.example.cepattanggapapp.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.*
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.cepattanggapapp.R
import com.example.cepattanggapapp.fragment.FAQFragment
import com.example.cepattanggapapp.fragment.HomeFragment
import com.example.cepattanggapapp.fragment.MessageFragment
import com.example.cepattanggapapp.fragment.ProfileFragment
import com.example.cepattanggapapp.utils.Constants
import com.example.cepattanggapapp.utils.MySimpleLocation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    MySimpleLocation.MySimpleLocationCallback {
    private var currentFragment: Int? = null
    private var fragment: Fragment? = null
    private var doubleBack = false

    // pertama
    lateinit var mySimpleLocation: MySimpleLocation

    // kedua
    private val REQUEST_CODE_PERMISSION = 2
    val mPermission: String = Manifest.permission.ACCESS_FINE_LOCATION

    // address
    private var myaddress: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(HomeFragment())
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        setSupportActionBar(main_toolbar)
        supportActionBar?.title = ""
        main_toolbar.elevation = 0.0F

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = resources.getColor(R.color.colorWhite)

        // service
//        val intent = Intent(this, RegistrationIntentService::class.java)
//        startService(intent)

        // check permission
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1
                )
            }
        }
        permissionGranted()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                loadFragment(HomeFragment())
                currentFragment = 0
                title_toolbar.text = resources.getString(R.string.home)
                return true
            }
            R.id.nav_message -> {
                loadFragment(MessageFragment())
                currentFragment = 1
                title_toolbar.text = resources.getString(R.string.message)

                return true
            }
            R.id.nav_faq -> {
                loadFragment(FAQFragment())
                currentFragment = 2
                title_toolbar.text = resources.getString(R.string.faq)
                return true
            }
            R.id.nav_profile -> {
                loadFragment(ProfileFragment())
                currentFragment = 3
                title_toolbar.text = resources.getString(R.string.profile)
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {
        if (doubleBack) {
            super.onBackPressed()
            return
        }

        this.doubleBack = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            doubleBack = false
        }, 2000)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            // .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.frame_layout, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    override fun onResume() {
        super.onResume()

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    // Log.w("TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token
                Log.d("TOKEN_REFRESH", token!!)
                Constants.setTokenFCM(this, token)
                //Log and toast
                //val msg = getString(R.string.msg_token_fmt, token)
                //Log.d("TAG_, msg)
                //Toast.makeText(context, token, Toast.LENGTH_SHORT).show()
            })
    }

    private fun permissionGranted() {
        this.let {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.CALL_PHONE
                ) -> {
                    // You can use the API that requires the permission.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        it.requestPermissions(
                            arrayOf(
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                            ),
                            1
                        )
                    }
                }
                else -> {
                    mySimpleLocation = MySimpleLocation(this@MainActivity, this@MainActivity)
                    mySimpleLocation.checkLocationSetting(this@MainActivity)
                }
            }
        }
    }

    override fun getLocation(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        Log.d("MAINACTIVITY", "$latitude $longitude")

        GlobalScope.launch {

            val geoCoder = Geocoder(applicationContext, Locale.getDefault())
            val addresses = geoCoder.getFromLocation(latitude, longitude, 1)

            val address = if (addresses != null && addresses.size != 0) {
                val fullAddress = addresses[0].getAddressLine(0)
                fullAddress.plus(", Latitude: $latitude").plus("- Longitude: $longitude")
            } else {
                "Addresses Not Found!"
            }
            launch {
                try {
                    Log.d("ADDRESS", address)
                    Constants.setAddress(applicationContext, address)
                    myaddress = address
                    //If you want to stop get your location on first result
                    mySimpleLocation.stopGetLocation()
                } catch (e: Exception) {
                    Log.e("MAINACTIVITY", e.message.toString())
                }
            }
        }
    }

}