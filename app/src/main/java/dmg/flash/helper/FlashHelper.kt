package dmg.flash.helper

abstract class FlashHelper {

  protected var flashLightStatus = false

  protected abstract fun turnOn()
  protected abstract fun turnOff()

  fun toggle(callback: (Boolean) -> Unit) {
    if (flashLightStatus) {
      turnOff()
    } else {
      turnOn()
    }

    callback.invoke(!flashLightStatus)
  }
}