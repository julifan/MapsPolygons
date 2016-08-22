package rs.pupin.custompolyline2;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.LinkedList;
import java.util.List;

import rs.pupin.model.DaoSession;
import rs.pupin.model.PolygonDao;
import rs.pupin.model.Polygon;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    boolean intent;
    private GoogleMap mMap;
    private DaoSession daoSession;

    private TextView textView;

    private LinkedList<Marker> markers;

    /**
     * init. can be called by DrawingMapsActivity, then the Intent exists
     *
     * @param savedInstanceState saved state. useful for rotation of device etc.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        textView = (TextView) findViewById(R.id.displayPolygon);
        textView.setText("welcome!");

        //TODO: make it more specific to determine if intent is from drawingMapsAct
        try {
        getIntent();
            intent = true;
        }
        catch (NullPointerException e) {
            //nothing happens.
        }

        init();
    }

    private void init() {

        markers = new LinkedList<Marker>();
        //get db
        daoSession = ((CustomPolyline2Application) getApplicationContext()).getDaoSession();
    }

    public void onClickAdd(View view) {
        Intent intent = new Intent(MapsActivity.this, LayerListActivity.class);
        startActivity(intent);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            /**
             * Listener for the Clicks on the map
             * @param latLng lat and longitude where the click was
             */
            @Override
            public void onMapClick(LatLng latLng) {
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();
                // Setting the position for the marker
                markerOptions.position(latLng);
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                // Placing a marker on the touched position
                Marker marker = mMap.addMarker(markerOptions);
                //keep track of all the markers.
                markers.add(marker);
            }
        });

        //read out what was drawn last.
        if(intent) {
            //get polygons from db
            PolygonDao polygonDao = daoSession.getPolygonDao();
            List<Polygon> polygonList = polygonDao.loadAll();
            //display the last one
            polygonList.get(polygonList.size()-1);
        }

    }
}
