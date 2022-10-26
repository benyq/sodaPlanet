package com.benyq.sodaplanet.base.ext

import android.app.Activity
import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextPaint
import android.util.TypedValue
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 *
 * @author benyq
 * @date 2022/10/10
 * @email 1520063035@qq.com
 *
 */

lateinit var appCtx: Application

//dp -> px
val Float.dp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

val Int.dp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

fun fromS() = fromSpecificVersion(Build.VERSION_CODES.S)
fun beforeS() = beforeSpecificVersion(Build.VERSION_CODES.S)
fun fromR() = fromSpecificVersion(Build.VERSION_CODES.R)
fun beforeR() = beforeSpecificVersion(Build.VERSION_CODES.R)
fun fromQ() = fromSpecificVersion(Build.VERSION_CODES.Q)
fun beforeQ() = beforeSpecificVersion(Build.VERSION_CODES.Q)
fun fromM() = fromSpecificVersion(Build.VERSION_CODES.M)
fun beforeM() = beforeSpecificVersion(Build.VERSION_CODES.M)
fun fromN() = fromSpecificVersion(Build.VERSION_CODES.N)
fun beforeN() = beforeSpecificVersion(Build.VERSION_CODES.N)
fun fromN_MR1() = fromSpecificVersion(Build.VERSION_CODES.N_MR1)
fun beforeN_MR1() = beforeSpecificVersion(Build.VERSION_CODES.N_MR1)
fun fromO() = fromSpecificVersion(Build.VERSION_CODES.O)
fun beforeO() = beforeSpecificVersion(Build.VERSION_CODES.O)
fun fromP() = fromSpecificVersion(Build.VERSION_CODES.P)
fun beforeP() = beforeSpecificVersion(Build.VERSION_CODES.P)
fun fromSpecificVersion(version: Int): Boolean = Build.VERSION.SDK_INT >= version
fun beforeSpecificVersion(version: Int): Boolean = Build.VERSION.SDK_INT < version


fun Activity.setStatusBarMode(color: Int, dark: Boolean) {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = color
    val insetsControllerCompat = WindowInsetsControllerCompat(window, window.decorView)
    insetsControllerCompat.isAppearanceLightStatusBars = dark
}

fun Fragment.setStatusBarMode(color: Int, dark: Boolean) {
    requireActivity().setStatusBarMode(color, dark)
}


fun Activity.fullScreen(full: Boolean) {
    val insetsControllerCompat = WindowInsetsControllerCompat(window, window.decorView)
    if (full) {
        insetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars())
        if (fromP()) {
            val lp: WindowManager.LayoutParams = window.attributes
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
        }
    } else {
        insetsControllerCompat.show(WindowInsetsCompat.Type.systemBars())
    }
}

fun Fragment.fullScreen(full: Boolean) {
    requireActivity().fullScreen(full)
}

inline fun AppCompatActivity.launchAndRepeatWithViewLifecycle(
    mainActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.()->Unit
) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(mainActiveState) {
            block()
        }
    }
}

inline fun Fragment.launchAndRepeatWithViewLifecycle(
    mainActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.()->Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(mainActiveState) {
            block()
        }
    }
}


private val handler = Handler(Looper.getMainLooper())
fun runOnUIThread(duration: Long = 0, action: ()->Unit) {
    if (Looper.myLooper() == Looper.getMainLooper() && duration == 0L) {
        action()
    }else {
        handler.postDelayed({action()}, duration)
    }
}

fun Context.toast(msg: String) {
    runOnUIThread {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

fun Context.toast(@StringRes resId: Int) {
    runOnUIThread {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.toast(msg: String) {
    requireActivity().toast(msg)
}

fun Fragment.toast(@StringRes resId: Int) {
    requireActivity().toast(resId)
}

val TextPaint.textHeight: Float
    get() = fontMetrics.descent - fontMetrics.ascent + fontMetrics.leading


fun Context.clipboardRead(): String {
    var content = ""
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.primaryClip?.run {
        if (itemCount > 0) {
            content = getItemAt(0).text?.toString() ?: ""
        }
    }
    return content
}

fun Context.clipboardClear() {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.setPrimaryClip(ClipData.newPlainText("", ""))
    clipboardManager.text = ""
}

fun Context.clipboardCopy(content: String) {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.setPrimaryClip(ClipData.newPlainText("", content))
    clipboardManager.text = content
}
