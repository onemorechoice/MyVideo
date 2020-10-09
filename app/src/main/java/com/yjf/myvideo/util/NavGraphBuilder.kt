package com.yjf.myvideo.util

import android.content.ComponentName
import androidx.fragment.app.FragmentActivity
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphNavigator
import androidx.navigation.fragment.FragmentNavigator
import com.yjf.myvideo.FixFragmentNavigator
import com.yjf.myvideo.MyApplication

class NavGraphBuilder {

    companion object{
        fun build(controller:NavController,activity:FragmentActivity,contentId:Int){
            val provider=controller.navigatorProvider
//            val fragmentNavigator=provider.getNavigator(FragmentNavigator::class.java)
            val fragmentNavigator=FixFragmentNavigator(activity,activity.supportFragmentManager,contentId)
            provider.addNavigator(fragmentNavigator)
            val activityNavigator=provider.getNavigator(ActivityNavigator::class.java)

            val navGraph=NavGraph(NavGraphNavigator(provider))

            val hashMap=AppConfig.getDestination()

            for ((key, value) in hashMap) {
                if (value.isFragment){
                val destination=  fragmentNavigator.createDestination()
                    destination.className=value.clazName
                    destination.id=value.id
                    destination.addDeepLink(value.pageUrl)
                    navGraph.addDestination(destination)
            }else{
               val destination=activityNavigator.createDestination()
                    destination.id=value.id
                    destination.addDeepLink(value.pageUrl)
                    destination.setComponentName(ComponentName(MyApplication.instance.packageName,value.clazName) )
                    navGraph.addDestination(destination)
                }
                if (value.asStart){
                   navGraph.startDestination=value.id
                }
            }

            controller.graph=navGraph


        }




    }

}