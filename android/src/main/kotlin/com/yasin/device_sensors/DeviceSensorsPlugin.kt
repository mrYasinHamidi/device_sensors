package com.yasin.device_sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

/** SensorsPlugin */
class DeviceSensorsPlugin : FlutterPlugin, MethodChannel.MethodCallHandler,
    EventChannel.StreamHandler,
    ActivityAware {

    private lateinit var context: Context
    private lateinit var methodChannel: MethodChannel
    private lateinit var eventChannel: EventChannel
    private lateinit var sensorManager: SensorManager


    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext
        methodChannel = MethodChannel(flutterPluginBinding.binaryMessenger, "methodSensors")
        eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "eventsSensors")
        methodChannel.setMethodCallHandler(this)
        eventChannel.setStreamHandler(this)
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        var sensor: Sensor? = null
        if (arguments == "lightSensor") {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        } else if (arguments == "proximitySensor") {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        }
        sensor?.let {
            sensorManager.registerListener(object : SensorEventListener {
                override fun onSensorChanged(p0: SensorEvent?) {
                    p0?.let {
                        events?.success(p0.values)
                    }
                }

                override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
                }
            }, sensor, 1)
        }

    }

    override fun onCancel(arguments: Any?) {
        TODO("Not yet implemented")
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        methodChannel.setMethodCallHandler(null)
        eventChannel.setStreamHandler(null)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if (call.method == "getLightSensorEvent") {
            sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val lightSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            sensorManager.registerListener(object : SensorEventListener {
                override fun onSensorChanged(p0: SensorEvent?) {
                    p0?.let {
                        result.success(it.values)
                    }
                }

                override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
                }
            }, lightSensor, 1)
        } else {
            result.notImplemented()
        }

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

