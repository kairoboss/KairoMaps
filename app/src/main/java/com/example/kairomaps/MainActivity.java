package com.example.kairomaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.kairomaps.databinding.ActivityMainBinding;
import com.example.kairomaps.utils.Prefs;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, View.OnClickListener {

    private ActivityMainBinding binding;
    private GoogleMap gMap;
    private List<LatLng> coordinates;
    private List<PolygonOptions> polygons;
    private List<PolylineOptions> polylines;
    private Prefs prefs;
    public static String PREFS_KEY = "coords";
    public static String POLYLINES_KEY = "polylines";
    public static String POLYGONS_KEY = "polygons";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefs = new Prefs(getApplicationContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        googleMap.setOnMapClickListener(this);
        coordinates = new ArrayList<>();
        polylines = new ArrayList<>();
        polygons = new ArrayList<>();
        btnListeners();
    }
    private void btnListeners() {
        binding.btnConfig.setOnClickListener(this);
        binding.btnPolyline.setOnClickListener(this);
        binding.btnPolygon.setOnClickListener(this);
        binding.btnClear.setOnClickListener(this);
        binding.btnLoad.setOnClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        addMarker(latLng);
    }

    private void addMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon));
        coordinates.add(latLng);
        gMap.addMarker(markerOptions);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_config)
            changeMapConfiguration();
        if (view.getId() == R.id.btn_polyline)
            addPolyLine();
        if (view.getId() == R.id.btn_polygon)
            addPolygon();
        if(view.getId() == R.id.btn_clear)
            clearMap();
        if (view.getId() == R.id.btn_load)
            loadMap();
    }

    private void loadMap() {
        coordinates = prefs.getCoords(PREFS_KEY);
        polylines = prefs.getPolyLines(POLYLINES_KEY);
        polygons = prefs.getPolygons(POLYGONS_KEY);
        if (!coordinates.isEmpty()) {
            for (int i = 0; i < coordinates.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(coordinates.get(i));
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon));
                gMap.addMarker(markerOptions);
            }
            for (int i = 0; i <polylines.size() ; i++) {
                gMap.addPolyline(polylines.get(i));
            }
            for (int i = 0; i < polygons.size(); i++) {
                gMap.addPolygon(polygons.get(i));
            }
        }
    }

    private void clearMap() {
        prefs.clearPrefs();
        gMap.clear();
    }

    private void addPolygon() {
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.addAll(coordinates);
        polygons.add(polygonOptions);
        gMap.addPolygon(polygonOptions);
    }

    private void addPolyLine() {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(coordinates);
        polylines.add(polylineOptions);
        gMap.addPolyline(polylineOptions);
    }

    private void changeMapConfiguration() {
        if (gMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL)
            gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        else
            gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        prefs.putCoords(PREFS_KEY, coordinates);
        prefs.putPolyLines(POLYLINES_KEY, polylines);
        prefs.putPolygons(POLYGONS_KEY, polygons);
    }
}