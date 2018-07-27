package com.muxistudio.appcommon.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent


fun AppUtil.intent2Wx(context : Context){
    val intent = Intent()
    val cmp = ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI")

    intent.setAction(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    intent.setComponent(cmp)
    context.startActivity(intent)

}