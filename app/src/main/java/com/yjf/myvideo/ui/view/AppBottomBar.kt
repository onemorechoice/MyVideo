package com.yjf.myvideo.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.yjf.myvideo.R
import com.yjf.myvideo.util.AppConfig
import com.yjf.myvideo.util.dp2px

class AppBottomBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr) {

companion object{
    private val sIcons = intArrayOf(
        R.drawable.icon_tab_home,
        R.drawable.icon_tab_sofa,
        R.drawable.icon_tab_publish,
        R.drawable.icon_tab_find,
        R.drawable.icon_tab_mine
    )
}


    init {

        val bottomBar=AppConfig.getBottomBar()
        val tabs=bottomBar.tabs

        val state = arrayOfNulls<IntArray>(2)
        state[0] = intArrayOf(android.R.attr.state_selected)
        state[1] = intArrayOf()

        val colors = intArrayOf(
            Color.parseColor(bottomBar.activeColor),
            Color.parseColor(bottomBar.inActiveColor)
        )
        val conStateList=ColorStateList(state,colors)

        itemIconTintList=conStateList
        itemTextColor=conStateList
        labelVisibilityMode=LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        selectedItemId=bottomBar.selectTab


        for ((index,tab)in tabs.withIndex()){
           if (!tab.enable) continue
           val itemId:Int=getItemId(tab.pageUrl)
           if (itemId<0) continue

           val item=menu.add(0,itemId,index,tab.title)
           item.setIcon(sIcons[tab.index])
        }


        @SuppressLint("RestrictedApi")
        for (tab in tabs){
            val bottomNavigationMenuView=  getChildAt(0) as BottomNavigationMenuView
            val itemView=bottomNavigationMenuView.getChildAt(tab.index) as BottomNavigationItemView
            itemView.setIconSize(dp2px(tab.size).toInt())
            if (TextUtils.isEmpty(tab.title)){
                itemView.setIconTintList(ColorStateList.valueOf(Color.parseColor(tab.tintColor)))
                itemView.setShifting(false)
            }
        }

        if (bottomBar.selectTab!=0){
            val selectTab=bottomBar.tabs[bottomBar.selectTab]
            if (selectTab.enable){
              val itemId=getItemId(selectTab.pageUrl)
              post { selectedItemId=itemId }
            }


        }




    }
    private fun getItemId(pageUrl:String):Int{
        val destination=AppConfig.getDestination()[pageUrl]
        return destination?.id ?: -1
    }



}