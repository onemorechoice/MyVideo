package com.yjf.myvideo.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yjf.myvideo.MyApplication
import com.yjf.myvideo.data.BottomBar
import com.yjf.myvideo.data.MyDestination
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class AppConfig {

    companion object{
       var hashMap:HashMap<String,MyDestination>?=null

       var bar:BottomBar?=null

       fun getDestination():HashMap<String,MyDestination>{
           if (hashMap==null){
               val content= parseFile("destnation.json")
               hashMap=Gson().fromJson(content, genericType<HashMap<String,MyDestination>>())
           }
           return hashMap!!
       }

        fun getBottomBar():BottomBar{
            if (bar==null){
            val content=  parseFile("main_tabs_config.json")
               bar=Gson().fromJson(content)
            }
            return bar!!
        }

        private fun parseFile(fileName:String):String {
            val assets=MyApplication.instance.resources.assets

            val inputSteam=  assets.open(fileName)
            val stringBuffer=StringBuffer()
            BufferedReader(InputStreamReader(inputSteam)).use {
                var line: String
                while (true) {
                    line = it.readLine() ?: break //当有内容时读取一行数据，否则退出循环
                    stringBuffer.append(line)
//                    println(line) //打印一行数据 } }
                }
            }
           return stringBuffer.toString()

        }

    }



}