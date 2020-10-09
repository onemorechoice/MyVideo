package com.yjf.myvideo.data

data class BottomBar(val activeColor:String,val inActiveColor:String,val selectTab:Int,val tabs:List<Tab>)

data class Tab(val size:Int,val enable:Boolean,val index:Int,val pageUrl:String,val title:String,val tintColor:String)