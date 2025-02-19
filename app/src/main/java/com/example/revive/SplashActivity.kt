package com.example.revive

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.revive.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoView = binding.videoView
        val videoPath = "android.resource://" + packageName + "/" + R.raw.sample_video
        val uri = Uri.parse(videoPath)
        videoView.setVideoURI(uri)

        // Listen for the media to be ready to set the correct scaling
        videoView.setOnPreparedListener { mediaPlayer ->
            val videoWidth = mediaPlayer.videoWidth
            val videoHeight = mediaPlayer.videoHeight

            val screenWidth = resources.displayMetrics.widthPixels
            val screenHeight = resources.displayMetrics.heightPixels

            val videoProportion = videoWidth.toFloat() / videoHeight.toFloat()
            val screenProportion = screenWidth.toFloat() / screenHeight.toFloat()

            if (videoProportion > screenProportion) {
                videoView.layoutParams.width = screenWidth
                videoView.layoutParams.height = (screenWidth / videoProportion).toInt()
            } else {
                videoView.layoutParams.width = (screenHeight * videoProportion).toInt()
                videoView.layoutParams.height = screenHeight
            }

            videoView.start() // Start video playback
        }

        // Redirect to SignInActivity on click
        videoView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish() // Ensure splash screen is not shown again on back press
        }
    }
}
