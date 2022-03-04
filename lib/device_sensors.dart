
import 'dart:async';

import 'package:flutter/services.dart';

import 'dart:async';

import 'package:flutter/services.dart';

class DeviceSensors {
  static const MethodChannel _methodChannel = MethodChannel('methodSensors');
  static const EventChannel _eventChannel = EventChannel('eventsSensors');

  static Future<String?> get platformVersion async {
    final String? version = await _methodChannel.invokeMethod('getPlatformVersion');
    return version;
  }
  static void listenToLightSensor(Function(String value) listener) {
    _eventChannel.receiveBroadcastStream('lightSensor').distinct().listen((event) {
      listener.call(event.toString());
    });
  }
  static void listenToProximitySensor(Function(String value) listener) {
    _eventChannel.receiveBroadcastStream('proximitySensor').distinct().listen((event) {
      listener.call(event.toString());
    });
  }
}