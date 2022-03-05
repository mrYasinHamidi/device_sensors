import 'dart:async';

import 'package:flutter/services.dart';

import 'dart:async';

import 'package:flutter/services.dart';

class DeviceSensors {
  static const EventChannel _eventChannel = EventChannel('eventsSensors');

  static void listenToLightSensor(Function(String value) listener) {
    _eventChannel.receiveBroadcastStream('lightSensor').distinct().listen((event) {
      listener.call(event.toString());
    });
  }

  static void listenToProximitySensor(Function(String value) listener) {
    // accelerationSensor
    _eventChannel.receiveBroadcastStream('proximitySensor').distinct().listen((event) {
      listener.call(event.toString());
    });
  }

  static void listenTo(SensorType sensorType, Function(List value) callback) {
    _eventChannel.receiveBroadcastStream(_getStreamName(sensorType)).distinct().listen((event) {
      callback.call(event);
    });
  }

  static _getStreamName(SensorType sensorType) {
    switch(sensorType){
      case SensorType.LIGHT:
        return 'lightSensor';
      case SensorType.PROXIMITY:
        return 'proximitySensor';
      case SensorType.ACCELERATION:
        return 'accelerationSensor';
    }
  }
}

enum SensorType {
  LIGHT,
  PROXIMITY,
  ACCELERATION
}