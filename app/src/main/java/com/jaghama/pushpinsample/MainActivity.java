package com.jaghama.pushpinsample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;


public class MainActivity extends ActionBarActivity {

    private MapView m_map;
    private IMapController m_mapController;

    // MAPNIKタイルをズームレベル19まで表示する設定
    public static final XYTileSource MAPNIK_19 = new XYTileSource(
            "Mapnik",
            ResourceProxy.string.mapnik, 0, 20, 256, ".png", new String[] {
            "http://a.tile.openstreetmap.org/",
            "http://b.tile.openstreetmap.org/",
            "http://c.tile.openstreetmap.org/" });

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

        double center_lat = 34.70504;
        double center_lng = 137.73362;
        GeoPoint center_gpt = new GeoPoint(center_lat, center_lng);
        m_mapController.setCenter(center_gpt);
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
}
