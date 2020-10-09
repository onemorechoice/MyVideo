package com.yjf.libnavannotion
@Target(AnnotationTarget.CLASS)
annotation class FragmentDestination(val pageUrl:String,val needLogin:Boolean=false,val asStart:Boolean=false)



