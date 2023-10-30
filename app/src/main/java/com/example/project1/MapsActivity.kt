package com.example.project1

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var backButton: Button
    private lateinit var locationText: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MapsInitializer.initialize(this,MapsInitializer.Renderer.LATEST){

        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // set actionbar to correct title
        val actionBar = supportActionBar
        actionBar!!.title = "News by Location"

        backButton = findViewById(R.id.backButtonMap)
        backButton.setOnClickListener {
            val intent = Intent(this@MapsActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val sharedPrefs = getSharedPreferences("savedStuff", MODE_PRIVATE)
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.setOnMapLongClickListener {
            Log.d("Maps", "long click at ${it.latitude},${it.longitude}")

            mMap.clear()
            val geocoder = Geocoder(this, Locale.getDefault())
            val results = try {
                geocoder.getFromLocation(
                    it.latitude,
                    it.longitude,
                    10
                )
            } catch (exception: Exception) {
                Log.e("Maps", "Geocoding failed", exception)
                listOf<Address>()
            }
            if (results.isNullOrEmpty()) {
                Log.e("Maps", "No addressed found")
            } else {
                val currentAddress = results[0]
                val addressline = currentAddress.countryName
                locationText = findViewById(R.id.locationText)
                mMap.addMarker(MarkerOptions().position(it).title(addressline))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(it))
                val location = "  Results for: $addressline  "
                locationText.isVisible = true
                locationText.text = location

                sharedPrefs.edit().putString("LAT", it.latitude.toString()).apply()
                sharedPrefs.edit().putString("LONG", it.longitude.toString()).apply()
                sharedPrefs.edit().putString("MAP_COUNTRY", addressline.toString()).apply()

                recyclerView = findViewById(R.id.articleRecyclerView)
                val sourceManager = SourceManager()
                val apiKey = getString(R.string.news_api_key)
                var sources = listOf<SourcesData>()

                CoroutineScope(IO).launch {
                    sources = sourceManager.retrieveArticles(addressline.toString(), apiKey)

                    withContext(Main) {
                        val adapter = ArticleAdapter(sources)
                        recyclerView.adapter = adapter
                        adapter.setOnItemClickListener(object : ArticleAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {
                                val browserIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(sources[position].articleURL)
                                )
                                startActivity(browserIntent)
                            }

                        })
                        val HorizontalLayout = LinearLayoutManager(
                            this@MapsActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        recyclerView.setLayoutManager(HorizontalLayout)
                    }
                }
            }
        }
        val sharedLat = sharedPrefs.getString("LAT", "")
        val sharedLong = sharedPrefs.getString("LONG", "")
        val addressLine = sharedPrefs.getString("MAP_COUNTRY", "")

        val location = "  Results for: $addressLine  "
        locationText = findViewById(R.id.locationText)
        if (addressLine != "" && addressLine != null){
            locationText.text = location
            locationText.isVisible = true
            if (sharedLat == "" || sharedLong == "" || sharedLat == null || sharedLong == null){
                val latLng = LatLng(38.898365, -77.046753)
                val title = "Start"
                mMap.addMarker(
                    MarkerOptions().position(latLng).title(title)
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            } else {
                val latLng = LatLng(sharedLat.toDouble(), sharedLong.toDouble())
                val title = "Start"
                mMap.addMarker(
                    MarkerOptions().position(latLng).title(title)
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))

                // load news articles
                recyclerView = findViewById(R.id.articleRecyclerView)

                val sourceManager = SourceManager()
                val apiKey = getString(R.string.news_api_key)
                var sources = listOf<SourcesData>()

                CoroutineScope(IO).launch {
                    sources = sourceManager.retrieveArticles(addressLine, apiKey)

                    withContext(Main) {
                        val adapter = ArticleAdapter(sources)
                        recyclerView.adapter = adapter
                        adapter.setOnItemClickListener(object : ArticleAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {
                                val browserIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(sources[position].articleURL)
                                )
                                startActivity(browserIntent)
                            }

                        })
                        val HorizontalLayout = LinearLayoutManager(
                            this@MapsActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        recyclerView.setLayoutManager(HorizontalLayout)
                    }
                }
            }

        }
//        // No sharedpref, default to gwu
//        val latLng = LatLng(38.898365, -77.046753)
//        val title = "Start"
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }
}