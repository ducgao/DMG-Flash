package dmg.flash.helper

import android.hardware.Camera
import android.hardware.Camera.Parameters

@Suppress("DEPRECATION")
class OldFlashHelperImplementation : FlashHelper() {
  private val camera: Camera by lazy { Camera.open() }

  override fun turnOn() {
    val params = camera.parameters
    params.flashMode = Parameters.FLASH_MODE_TORCH
    camera.parameters = params
    camera.startPreview()
    flashLightStatus = true
  }

  override fun turnOff() {
    val params = camera.parameters
    params.flashMode = Parameters.FLASH_MODE_OFF
    camera.parameters = params
    camera.stopPreview()
    flashLightStatus = false
  }
}