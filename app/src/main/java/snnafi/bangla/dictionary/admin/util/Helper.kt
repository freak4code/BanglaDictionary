package snnafi.bangla.dictionary.admin.util

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.SuperscriptSpan
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.multidex.MultiDexApplication
import java.util.regex.Matcher
import java.util.regex.Pattern


object Helper {

    lateinit var application: MultiDexApplication
    var isServiceFound = false

    fun validate(fields: List<EditText>): Boolean {
        for (i in fields.indices) {
            val currentField = fields[i]
            if (currentField.text.toString().length <= 0) {
                return false
            }
        }
        return true
    }

    fun setTypeFace(textViews: List<TextView>) {
        Typeface.createFromAsset(application.assets, Constant.APP_FONT).apply {
            textViews.forEach {
                it.setTypeface(this)
            }
        }
    }


    @SuppressLint("DefaultLocale")
    fun checkEmpty(content: String): String {
        Log.d("------", content.trim().toUpperCase())
        if (content.trim().toUpperCase() == "NIL") {
            return "—"
        }
        return content
    }

    fun addNil(textViews: List<TextView>) {
        textViews.forEach {
            it.text = "—"
        }
    }

    fun replaceBanglaNumber(text: String): String {
        return text.replace("0", "০").replace("1", "১").replace("2", "২").replace("3", "৩")
            .replace("4", "৪")
            .replace("5", "৫").replace("6", "৬").replace("7", "৭").replace("8", "৮")
            .replace("9", "৯")

    }

    fun replacePodShortForm(text: String): String {
        return text.replace("\\bঅব্য\\b".toRegex(), "অব্যয়")
            .replace("\\bবি\\b".toRegex(), "বিশেষ্য")
            .replace("\\bবিণ\\b".toRegex(), "বিশেষণ")
            .replace("\\bসর্ব\\b".toRegex(), "সর্বনাম").replace("\\bক্রি\\b".toRegex(), "ক্রিয়া")
            .replace(".", "")


    }


    fun replaceShortForm(text: String): String {
        return text.replace("\\bঅজ্ঞা\\b".toRegex(), "অজ্ঞাতমূল")
            .replace("\\bঅনুক্রি\\b".toRegex(), "অনুজ্ঞা ক্রিয়া")
            .replace("\\bঅনু\\b".toRegex(), "অনুসর্গ").replace("\\bঅপ্র\\b".toRegex(), "অপ্রচলিত")
            .replace("\\bঅর্বাস\\b".toRegex(), "অর্বাচীন সংস্কৃত")
            .replace("\\bআ\\b".toRegex(), "আরবি").replace("\\bঅহ\\b".toRegex(), "অহমিয়া")
            .replace("\\bআইরি\\b".toRegex(), "আইরিশ").replace("\\bআঞ্চ\\b".toRegex(), "আঞ্চলিক")
            .replace("\\bআবৈশ\\b".toRegex(), "আন্তর্জাতিক বৈজ্ঞানিক শব্দাবলি")
            .replace("\\bআল\\b".toRegex(), "আলংকারিক")
            .replace("\\bই\\b".toRegex(), "ইংরেজি").replace("\\bউ\\b".toRegex(), "উর্দু")
            .replace("\\bকিমি\\b".toRegex(), "কিলোমিটার")
            .replace("\\bক্রিবি\\b".toRegex(), "ক্রিয়াবিশেষ্য")
            .replace("\\bক্রিবিণ\\b".toRegex(), "ক্রিয়াবিশেষণ")
            .replace("\\bক্রিমূ\\b".toRegex(), "ক্রিয়ামূল")
            .replace("\\bখ্রি\\b".toRegex(), "খ্রিষ্টাব্দ")
            .replace("\\bখ্রি.পূ\\b".toRegex(), "খ্রিষ্টপূর্ব")
            .replace("\\bগ\\b".toRegex(), "গণিত").replace("\\bজ্যা\\b".toRegex(), "জ্যামিতি")
            .replace("\\bদেশি\\b".toRegex(), "দেশি শব্দ")
            .replace("\\bদ্র\\b".toRegex(), "দ্রষ্টব্য")
            .replace("\\bনজরুল\\b".toRegex(), "কাজী নজরুল ইসলাম")
            .replace("\\bপরি\\b".toRegex(), "পরিভাষা").replace("\\bপা\\b".toRegex(), "পালি")
            .replace("\\bপৃ\\b".toRegex(), "পৃষ্ঠাসংখ্যা")
            .replace("\\bপ্র\\b".toRegex(), "প্রবাদ").replace("\\bপ্রা\\b".toRegex(), "প্রাকৃত")
            .replace("\\bফ\\b".toRegex(), "ফরাসিি")
            .replace("\\bফা\\b".toRegex(), "ফারসি")
            .replace("\\bবা\\b".toRegex(), "বাংলা").replace("\\bব্যা\\b".toRegex(), "ব্যাকরণ")
            .replace("\\bভাষা\\b".toRegex(), "ভাষাবিজ্ঞান")
            .replace("\\bস\\b".toRegex(), "সংস্কৃত")
            .replace("\\bসা\\b".toRegex(), "সল্লাল্লাহু আলাইহে ওয়াসাল্লাম")
            .replace("\\bসেমি\\b".toRegex(), "সেন্টিমিটার")
            .replace("\\bহি\\b".toRegex(), "হিন্দি").replace("\\bঅব্য\\b".toRegex(), "অব্যয়")
            .replace("\\bবি\\b".toRegex(), "বিশেষ্য")
            .replace("\\bবিণ\\b".toRegex(), "বিশেষণ")
            .replace("\\bসর্ব\\b".toRegex(), "সর্বনাম").replace("\\bক্রি\\b".toRegex(), "ক্রিয়া")
            .replace("\\bলা\\b".toRegex(),"লাতিন")
            .replace(".", "")


    }

    //
//
//

//
//
//
//
//
//
//
//

//
//
//
//
//
//()
//()
//
//
//
//
//
//
//
//
//			.
//
//
//

//
//
//

//
//


    fun duplicateWord(text: String, pattern: Pattern): SpannableStringBuilder {
        if (text.contains("০") || text.contains("১") || text.contains("২") || text.contains("৩") || text.contains(
                "৪"
            ) || text.contains("৫") || text.contains("৬") || text.contains("৭") || text.contains("৮") || text.contains(
                "৯"
            )
        ) {
            val spannableStringBuilder = SpannableStringBuilder(text);
            val matcher: Matcher = pattern.matcher(text)
            while (matcher.find()) {
                val start = matcher.start() + 1
                val end = matcher.end() - 1
                spannableStringBuilder.setSpan(
                    SuperscriptSpan(),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            replaceAll(spannableStringBuilder, Pattern.compile("\\["), "")
            replaceAll(spannableStringBuilder, Pattern.compile("\\]"), "")
            return spannableStringBuilder
        } else {
            return SpannableStringBuilder(text)
        }


    }

    fun checkInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
        val networks = connectivityManager.allNetworks
        var hasInternet = false
        if (networks.size > 0) {
            for (network in networks) {
                val nc = connectivityManager.getNetworkCapabilities(network)
                if (nc!!.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) hasInternet =
                    true
            }
        }
        return hasInternet
    }

    fun isServiceRunning(context: Context?): Boolean {
        if (context != null) {
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val services = activityManager.getRunningTasks(Int.MAX_VALUE)
            isServiceFound = false
            for (i in services.indices) {
                if (services[i].topActivity.toString().equals(
                        "ComponentInfo{com.lyo.AutoMessage/com.lyo.AutoMessage.TextLogList}",
                        ignoreCase = true
                    )
                ) {
                    isServiceFound = true
                }
            }
            return isServiceFound
        } else {
            val activityManager =
                application.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val services = activityManager.getRunningTasks(Int.MAX_VALUE)
            isServiceFound = false
            for (i in services.indices) {
                if (services[i].topActivity.toString().equals(
                        "ComponentInfo{com.lyo.AutoMessage/com.lyo.AutoMessage.TextLogList}",
                        ignoreCase = true
                    )
                ) {
                    isServiceFound = true
                }
            }
            return isServiceFound
        }

    }

    private fun replaceAll(sb: SpannableStringBuilder, pattern: Pattern, replacement: String) {
        val m = pattern.matcher(sb)
        var extra = 0
        while (m.find()) {
            sb.replace(m.start() - extra, m.end() - extra, replacement)
            ++extra
        }
    }
}


