package com.jaghama.pushpinsample;

import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

// メインアクティビティクラス
public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,LocationListener, GoogleApiClient.OnConnectionFailedListener {

    private MapView m_map;
    private IMapController m_mapController;

    // MAPNIKタイルをズームレベル19まで表示する設定
    public static final XYTileSource MAPNIK_19 = new XYTileSource(
            "Mapnik",
            ResourceProxy.string.mapnik, 0, 20, 256, ".png", new String[] {
            "http://a.tile.openstreetmap.org/",
            "http://b.tile.openstreetmap.org/",
            "http://c.tile.openstreetmap.org/" });
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<OverlayItem> overlayItemArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_map = new org.osmdroid.views.MapView(this, 256);
        m_map.setBuiltInZoomControls(true);
        // MAPNIKタイルをズームレベル19で設定
        m_map.setTileSource(MAPNIK_19);
        // 最大ズームレベルを20に設定(19が拡大されるだけ)
        m_map.setMaxZoomLevel(20);
        m_map.setMultiTouchControls(true);

        org.osmdroid.views.MapView.LayoutParams mapParams =
            new org.osmdroid.views.MapView.LayoutParams(
                org.osmdroid.views.MapView.LayoutParams.MATCH_PARENT,
                org.osmdroid.views.MapView.LayoutParams.MATCH_PARENT, null,
                0, 0, org.osmdroid.views.MapView.LayoutParams.BOTTOM_CENTER);

        LinearLayout map_layout = (LinearLayout) findViewById(R.id.osm_mapview);
        map_layout.addView(m_map, mapParams);

        m_mapController = m_map.getController();
        m_mapController.setZoom(18);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

//        double center_lat = 34.70504;
//        double center_lng = 137.73362;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("debug","connected");
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("debug", location.toString());
        GeoPoint center_gpt = new GeoPoint(location.getLatitude(), location.getLongitude());
        m_mapController.setCenter(center_gpt);
        overlayItemArray = new ArrayList<>();

        // Create som init objects
        OverlayItem linkopingItem = new OverlayItem("Linkoping", "Sweden",
                new GeoPoint(location.getLatitude(), location.getLongitude()));


        // Add the init objects to the ArrayList overlayItemArray
        overlayItemArray.add(linkopingItem);

        // Add the Array to the IconOverlay
        ItemizedIconOverlay<OverlayItem> itemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(this, overlayItemArray, null);
        m_map.getOverlays().add(itemizedIconOverlay);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("debug",connectionResult.toString());


    }
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

}
