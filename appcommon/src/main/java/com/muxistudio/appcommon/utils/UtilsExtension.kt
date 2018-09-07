package com.muxistudio.appcommon.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import com.muxistudio.appcommon.data.A
import com.muxistudio.appcommon.net.CampusFactory
import com.muxistudio.appcommon.net.CampusRetrofit
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

fun AppUtil.intent2Wx(context : Context){
    val intent = Intent()
    val cmp = ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI")

    intent.setAction(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    intent.setComponent(cmp)
    context.startActivity(intent)

}

fun Context.cache(username:String?, userPassword:String?){
  if(TextUtils.isEmpty(username) || TextUtils.isEmpty(userPassword))
    return;
  else{
    CampusFactory.getRetrofitService()
        .cache(A(Integer.parseInt(username),userPassword))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object:Subscriber<Any>(){
          override fun onNext(t: Any?) {
            Log.d("Trasmit","complete")
          }

          override fun onCompleted() {
          }

          override fun onError(e: Throwable?) {
            e!!.printStackTrace()
          }
        })
  }
}