package com.yjf.myvideo

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import java.util.*

@Navigator.Name("fixfragment")
class FixFragmentNavigator(context: Context,manager: FragmentManager,contentId:Int) : FragmentNavigator(context,manager,contentId) {

    companion object{
        const val TAG:String="FixFragmentNavigator"
    }
    var mFragmentManager: FragmentManager = manager
    var mContext:Context=context
    var mContentId:Int=contentId


    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        if (mFragmentManager.isStateSaved) {
            Log.i(
                TAG, "Ignoring navigate() call: FragmentManager has already"
                        + " saved its state"
            )
            return null
        }
        var className = destination.className
        if (className[0] == '.') {
            className = mContext.packageName + className
        }
//        val frag = instantiateFragment(
//            mContext, mFragmentManager,
//            className, args
//        )
//        frag.arguments = args
        val ft = mFragmentManager.beginTransaction()

        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

        val fragment=mFragmentManager.primaryNavigationFragment
        if (fragment!=null){
            ft.hide(fragment)
        }

        var frag: Fragment ?=null
        val tag=destination.id.toString()
        frag=mFragmentManager.findFragmentByTag(tag)
        if (frag!=null){
            ft.show(frag)
        }else{
            frag= instantiateFragment(
            mContext, mFragmentManager,
            className, args
          )
            frag.arguments = args
            //记得Add的时候要加tag
            ft.add(mContentId,frag,tag)
        }
        ft.setPrimaryNavigationFragment(frag)

        @IdRes val destId = destination.id
        var mBackStack: ArrayDeque<Int>? = null
        try {
            val field=FragmentNavigator::class.java.getDeclaredField("mBackStack")
            field.isAccessible = true
            mBackStack=field.get(this) as ArrayDeque<Int>
        }catch (e:Exception){
          e.printStackTrace()
        }


        val initialNavigation = mBackStack!!.isEmpty()
        // TODO Build first class singleTop behavior for fragments
        // TODO Build first class singleTop behavior for fragments
        val isSingleTopReplacement = (navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && mBackStack.peekLast() == destId)

        val isAdded: Boolean
        isAdded = if (initialNavigation) {
            true
        } else if (isSingleTopReplacement) {
            // Single Top means we only want one instance on the back stack
            if (mBackStack.size > 1) {
                // If the Fragment to be replaced is on the FragmentManager's
                // back stack, a simple replace() isn't enough so we
                // remove it from the back stack and put our replacement
                // on the back stack in its place
                mFragmentManager.popBackStack(
                    generateBackStackName(mBackStack?.size, mBackStack.peekLast()),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                ft.addToBackStack(generateBackStackName(mBackStack.size, destId))
            }
            false
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack.size + 1, destId))
            true
        }
        if (navigatorExtras is Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key!!, value!!)
            }
        }
        ft.setReorderingAllowed(true)
        ft.commit()
        // The commit succeeded, update our view of the world
        // The commit succeeded, update our view of the world
        return if (isAdded) {
            mBackStack.add(destId)
            destination
        } else {
            null
        }
    }


    private fun generateBackStackName(backStackindex: Int, destid: Int): String? {
        return "$backStackindex-$destid"
    }
}