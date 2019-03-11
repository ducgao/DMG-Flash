package dmg.flash.helper

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Build.VERSION_CODES
import android.support.annotation.RequiresApi

@RequiresApi(VERSION_CODES.M)
class NewFlashHelperImplementation(context: Context) : FlashHelper() {

  private val cameraManager: CameraManager? = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager?

  override fun turnOn() {
    if (cameraManager == null) {
      return
    }

    val cameraId = cameraManager.cameraIdList[0]
    cameraManager.setTorchMode(cameraId, true)
    flashLightStatus = true
  }

  override fun turnOff() {
    if (cameraManager == null) {
      return
    }

    val cameraId = cameraManager.cameraIdList[0]
    cameraManager.setTorchMode(cameraId, false)
    flashLightStatus = false
  }
}