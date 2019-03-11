package dmg.flash

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View.OnClickListener
import android.widget.ImageButton
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import dmg.flash.helper.FlashHelper
import dmg.flash.helper.NewFlashHelperImplementation
import dmg.flash.helper.OldFlashHelperImplementation

class MainActivity : AppCompatActivity() {
  private val isFlashAvailable: Boolean by lazy { applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH) }

  private val container: ConstraintLayout by lazy { findViewById<ConstraintLayout>(R.id.container) }
  private val btnFlashToggle: ImageButton by lazy { findViewById<ImageButton>(R.id.btnFlashToggle) }
  private val adView: AdView by lazy { findViewById<AdView>(R.id.adView) }

  private val flashHelper: FlashHelper by lazy {
    if (VERSION.SDK_INT >= VERSION_CODES.M) {
      NewFlashHelperImplementation(applicationContext)
    } else {
      OldFlashHelperImplementation()
    }
  }

  private val adListener = object : AdListener() {
    override fun onAdLoaded() {
      println("DMG: add loaded")
    }

    override fun onAdFailedToLoad(errorCode: Int) {
      println("DMG: add load failed with code $errorCode")
      Handler().postDelayed({
        adView.loadAd(AdRequest.Builder().build())
      }, 5000)
    }

    override fun onAdOpened() {
      println("DMG: add opened")
    }

    override fun onAdLeftApplication() {
      println("DMG: add left application")
    }

    override fun onAdClosed() {
      println("DMG: add closed")
    }
  }

  private val onToggleFlash: OnClickListener by lazy {
    OnClickListener {
      flashHelper.toggle { status ->
        val newImage = if (status) {
          ContextCompat.getDrawable(
            this@MainActivity,
            R.drawable.flash_off
          )
        } else {
          ContextCompat.getDrawable(
            this@MainActivity,
            R.drawable.flash_on
          )
        }

        btnFlashToggle.setImageDrawable(newImage)
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    MobileAds.initialize(this, "ca-app-pub-3656670221855991~1023370420")

    actionBar?.hide()
    supportActionBar?.hide()

    if (!isFlashAvailable) {
      showDialog("Error", "Sorry, your device doesn't support flash light!") {
        finish()
      }
    }
  }

  override fun onResume() {
    super.onResume()
    btnFlashToggle.setOnClickListener(onToggleFlash)
    adView.loadAd(AdRequest.Builder().build())
    adView.adListener = adListener
  }

  override fun onDestroy() {
    super.onDestroy()
    btnFlashToggle.setOnClickListener(null)
    adView.adListener = null
  }

  private fun showDialog(title: String, message: String, action: () -> Unit) {
    val alert = AlertDialog.Builder(this@MainActivity).create()
    alert.setTitle(title)
    alert.setMessage(message)
    alert.setButton(DialogInterface.BUTTON_NEGATIVE, "OK") { _, _ ->
      action.invoke()
    }
    alert.show()
  }
}
