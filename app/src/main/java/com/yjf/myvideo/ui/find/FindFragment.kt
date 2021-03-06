package com.yjf.myvideo.ui.find

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yjf.libnavannotion.FragmentDestination
import com.yjf.myvideo.R
@FragmentDestination(pageUrl = "main/tabs/find",asStart = false)
class FindFragment : Fragment() {

    private lateinit var findViewModel: FindViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("FindFragment","onCreate")
        findViewModel =
            ViewModelProvider(this).get(FindViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_find, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        findViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}