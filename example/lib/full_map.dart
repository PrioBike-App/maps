import 'dart:async';
import 'dart:math';

import 'package:flutter/material.dart';
import 'package:mapbox_gl/mapbox_gl.dart';

import 'main.dart';
import 'page.dart';

class FullMapPage extends ExamplePage {
  FullMapPage() : super(const Icon(Icons.map), 'Full screen map');

  @override
  Widget build(BuildContext context) {
    return const FullMap();
  }
}

class FullMap extends StatefulWidget {
  const FullMap();

  @override
  State createState() => FullMapState();
}

class FullMapState extends State<FullMap> {
  MapboxMapController? mapController;
  var isLight = true;

  _onMapCreated(MapboxMapController controller) {
    mapController = controller;

    // Use a random user location in San Francisco.
    var heading = 45.0;
    var lat = 37.790;
    var lng = -122.410;
    var speed = 0.0;
    // Run a timer that updates the location every second
    Timer.periodic(const Duration(seconds: 1), (Timer t) {
      if (mapController == null) return;
      mapController!.updateUserLocation(lat: lat, lon: lng, speed: speed, heading: heading);
    });
  }

  _onStyleLoadedCallback() {
    ScaffoldMessenger.of(context).showSnackBar(SnackBar(
      content: Text("Style loaded :)"),
      backgroundColor: Theme.of(context).primaryColor,
      duration: Duration(seconds: 1),
    ));
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        floatingActionButton: Padding(
          padding: const EdgeInsets.all(32.0),
          child: FloatingActionButton(
            child: Icon(Icons.swap_horiz),
            onPressed: () => setState(
              () => isLight = !isLight,
            ),
          ),
        ),
        body: MapboxMap(
          styleString: isLight ? MapboxStyles.LIGHT : MapboxStyles.DARK,
          accessToken: MapsDemo.ACCESS_TOKEN,
          myLocationEnabled: true,
          myLocationRenderMode: MyLocationRenderMode.COMPASS,
          onUserLocationUpdated: (location) {
            print("User location updated: ${location.position}");
          },
          onMapCreated: _onMapCreated,
          // Focus on San Francisco
          initialCameraPosition: const CameraPosition(target: LatLng(37.7749, -122.4194), zoom: 11.0),
          onStyleLoadedCallback: _onStyleLoadedCallback,
          puckImage: "assets/position.png",
          puckSize: 128,
        ));
  }
}
