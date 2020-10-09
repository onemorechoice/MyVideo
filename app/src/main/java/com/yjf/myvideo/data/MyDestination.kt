package com.yjf.myvideo.data

data class MyDestination(var pageUrl:String,var needLogin:Boolean=false,
                         var asStart:Boolean=false,var isFragment:Boolean,
                         var id:Int,var clazName:String)