package com.yjf.myvideo.ui.publish

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yjf.libnavannotion.ActivityDestination
import com.yjf.myvideo.R

@ActivityDestination(pageUrl = "main/tabs/publish")
class PublishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish)
    }
}