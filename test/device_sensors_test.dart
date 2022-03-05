import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:device_sensors/device_sensors.dart';

void main() {
  const MethodChannel channel = MethodChannel('device_sensors');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });
}
