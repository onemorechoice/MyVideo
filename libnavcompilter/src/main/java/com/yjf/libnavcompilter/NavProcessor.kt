package com.yjf.libnavcompilter

import com.google.auto.service.AutoService
import com.google.gson.Gson
import com.yjf.libnavannotion.ActivityDestination
import com.yjf.libnavannotion.FragmentDestination
import java.io.File
import java.lang.Exception
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import javax.tools.StandardLocation
import kotlin.math.abs
/**
 * APP页面导航信息收集注解处理器
 * <p>
 * AutoService注解：就这么一标记，annotationProcessor  project()应用一下,编译时就能自动执行该类了。
 * <p>
 * SupportedSourceVersion注解:声明我们所支持的jdk版本
 * <p>
 * SupportedAnnotationTypes:声明该注解处理器想要处理那些注解
 */
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.yjf.libnavannotion.ActivityDestination","com.yjf.libnavannotion.FragmentDestination")
class NavProcessor : AbstractProcessor() {
    lateinit var messager: Messager
    lateinit var filer: Filer

    companion object{
        const val OUTPUT_FILE_NAME:String="destnation.json"
    }

    override fun init(p0: ProcessingEnvironment?) {
        super.init(p0)
        messager=p0!!.messager
        filer=p0!!.filer
    }

    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
        messager.printMessage(Diagnostic.Kind.NOTE,"开始进入process")
        val fragmentElement:Set<out Element> =p1!!.getElementsAnnotatedWith(FragmentDestination::class.java)

        val activityElement:Set<out Element> =p1!!.getElementsAnnotatedWith(ActivityDestination::class.java)

        if (fragmentElement.isNotEmpty()||activityElement.isNotEmpty()){
            val hash=HashMap<String,MyDestination>()
            handleDestination(fragmentElement,FragmentDestination::class.java,hash)
            handleDestination(activityElement,ActivityDestination::class.java,hash)
             try{
                 val resource=filer.createResource(StandardLocation.CLASS_OUTPUT,"", OUTPUT_FILE_NAME)
                 val resourcePath=resource.toUri().path
                 messager.printMessage(Diagnostic.Kind.NOTE,"resourcePath:$resourcePath")
                 val appPath=resourcePath.substring(0,resourcePath.indexOf("app")+4)
                 val assetPath=appPath+"src/main/assets/"

                 val file=File(assetPath)
                 if (!file.exists()){
                     file.mkdirs()
                 }

                 val outPutFile=File(file, OUTPUT_FILE_NAME)
                 if (outPutFile.exists()){
                     outPutFile.delete()
                 }
//读取                 File("/home/test.txt").readLines()
//                     .forEach { println(it) }

                 outPutFile.createNewFile()
                 val content=Gson().toJson(hash)
                 //一次过写入
                 outPutFile.writeText(content)
             }catch (e:Exception){
                 e.printStackTrace()
             }

        }
             return true
    }

    private fun handleDestination(elements: Set<out Element>, annotationClaz: Class<out Annotation>, hash: HashMap<String, MyDestination>) {
         elements.forEach {
             val typeElement:TypeElement=it as TypeElement
             var pageUrl:String?=null
             var clazName:String?=typeElement.qualifiedName.toString()
             var id:Int= abs(clazName.hashCode())
             var needLogin:Boolean=false
             var asStarter:Boolean=false
             var isFragment:Boolean=false
             val annotation=typeElement.getAnnotation(annotationClaz)
             if (annotation is FragmentDestination){
                 pageUrl=annotation.pageUrl
                 needLogin=annotation.needLogin
                 asStarter=annotation.asStart
                 isFragment=true
             }else if (annotation is ActivityDestination){
                 pageUrl=annotation.pageUrl
                 needLogin=annotation.needLogin
                 asStarter=annotation.asStart
                 isFragment=false
             }
             if (hash.containsKey(pageUrl)){
                 messager.printMessage(Diagnostic.Kind.ERROR,"不同页面不允许使用相同pageUrl$clazName")
             }else{
                 val myDestination=MyDestination(pageUrl!!,needLogin,asStarter,isFragment,id,clazName!!)
                 hash[pageUrl] = myDestination
             }
         }
    }
}