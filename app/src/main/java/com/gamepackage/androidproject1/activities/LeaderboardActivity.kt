package com.gamepackage.androidproject1.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.gamepackage.androidproject1.R
import com.gamepackage.androidproject1.databinding.ActivityLeaderboardBinding
import com.gamepackage.androidproject1.utils.MSPV3
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.CameraUpdateFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamepackage.androidproject1.logic.ScoreAdapter

class LeaderboardActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityLeaderboardBinding
    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Glide.with(this).load(R.drawable.space_background).into(binding.backgroundImg)



        val topScores = MSPV3.getInstance().getTopScores()

        val adapter = ScoreAdapter(topScores) { score ->
            // when a score item is clicked — move the map camera
            val location = LatLng(score.latitude, score.longitude)
            binding.leaderboardMap.getMapAsync { googleMap ->
                googleMap.addMarker(MarkerOptions().position(location).title("${score.playerName}'s Score"))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
            }
        }
        binding.leaderboardRecyclerView.adapter = adapter
        binding.leaderboardRecyclerView.layoutManager = LinearLayoutManager(this)

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.leaderboardRecyclerView.addItemDecoration(dividerItemDecoration)

        binding.btnBackToMenu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish() // close LeaderboardActivity too
        }

        // Initialize MapView with saved instance state
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }

        binding.leaderboardMap.onCreate(mapViewBundle)
        binding.leaderboardMap.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latitude = intent.getDoubleExtra("latitude", 0.0)  // 0.0 is default if not found
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val testLocation = LatLng(latitude, longitude)
        googleMap.addMarker(MarkerOptions().position(testLocation).title("Test Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(testLocation, 10f))
    }

    override fun onResume() {
        super.onResume()
        binding.leaderboardMap.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.leaderboardMap.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.leaderboardMap.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.leaderboardMap.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.leaderboardMap.onDestroy()
    }

    @Deprecated("Deprecated in Java")
    override fun onLowMemory() {
        super.onLowMemory()
        binding.leaderboardMap.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        binding.leaderboardMap.onSaveInstanceState(mapViewBundle)
    }
}
