import 'package:device_sensors/device_sensors.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String text = '';
  String proximityText = '';

  @override
  void initState() {
    super.initState();
    // Sensors.listenToLightSensor((value){
    //   setState(() {
    //     text = value;
    //   });
    // });
    // DeviceSensors.listenTo(SensorType.PROXIMITY, (value) {
    //   setState(() {
    //     proximityText = value;
    //   });
    // });
    DeviceSensors.listenTo(SensorType.ACCELERATION, (value) {
      setState(() {
        proximityText = 'Z : ${value[0]}\nY : ${value[1]}\nX : ${value[2]}\n';
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('$proximityText'),
        ),
      ),
    );
  }
}
