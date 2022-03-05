package com.yasin.device_sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.BatteryManager
import android.widget.Switch
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

/** SensorsPlugin */
class DeviceSensorsPlugin : FlutterPlugin, EventChannel.StreamHandler, ActivityAware {

    private lateinit var eventChannel: EventChannel
    private lateinit var sensorHelper: SensorHelper


    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        sensorHelper = SensorHelper(flutterPluginBinding.applicationContext)
        eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "eventsSensors")
        eventChannel.setStreamHandler(this)
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        var sensorType: SensorType? = null
        when (arguments) {
            "lightSensor" -> {
                sensorType = SensorType.LIGHT
            }
            "proximitySensor" -> {
                sensorType = SensorType.PROXIMITY
            }
            "accelerationSensor" -> {
                sensorType = SensorType.ACCELERATION
            }
        }
        if (sensorType == null) events?.endOfStream()

        events?.let {
            sensorType?.let {
                sensorHelper.setListener(sensorType) { data ->
                    events.success(data.data)
                }
            }
        }

    }

    override fun onCancel(arguments: Any?) {
        sensorHelper.removeListener()
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        eventChannel.setStreamHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        TODO("Not yet implemented")
    }

    override fun onDetachedFromActivityForConfigChanges() {
        TODO("Not yet implemented")
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        TODO("Not yet implemented")
    }

    override fun onDetachedFromActivity() {
        TODO("Not yet implemented")
    }

}

class SensorHelper(context: Context) {
    private var sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private var refreshPeriod: Int = 1
        set(value) {
            field = if (value < 1)
                1
            else
                value
        }

    private lateinit var sensorEventListener: SensorEventListener

    private fun isSensorAvailable(sensor: Sensor): Boolean {

        return true
    }

    private fun getSensorId(type: SensorType): Int =
        when (type) {
            SensorType.PROXIMITY -> Sensor.TYPE_PROXIMITY
            SensorType.LIGHT -> Sensor.TYPE_LIGHT
            SensorType.ACCELERATION -> Sensor.TYPE_ACCELEROMETER
        }

    private fun getSensor(type: SensorType): Sensor? {
        val sensor: Sensor = sensorManager.getDefaultSensor(getSensorId(type))

        if (!isSensorAvailable(sensor))
            return null
        return sensor
    }

    fun setListener(sensorType: SensorType, response: (data: SensorData) -> Unit) {
        val sensor: Sensor = getSensor(sensorType) ?: return
        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    response(SensorData(type = sensorType, data = event.values))
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }

        }
        sensorManager.registerListener(sensorEventListener, sensor, refreshPeriod)
    }

    fun removeListener() {
        sensorManager.unregisterListener(sensorEventListener)
    }

}

data class SensorData(val type: SensorType, val data: FloatArray)

enum class SensorType {
    PROXIMITY, LIGHT, ACCELERATION
}
