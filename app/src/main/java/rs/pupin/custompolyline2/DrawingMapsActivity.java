package rs.pupin.custompolyline2;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.text.Text;

import java.util.LinkedList;

import rs.pupin.model.DaoSession;
import rs.pupin.model.GroundOverlayDao;
import rs.pupin.model.PointOfInterestDao;
import rs.pupin.model.Polygon;
import rs.pupin.model.PolygonDao;
import rs.pupin.model.Polyline;
import rs.pupin.model.PolylineDao;

/**
 * is called by ShapesListActivity with an intent.
 * Intent contains what should be drawn.
 * TODO design storing in the db well
 */
public class DrawingMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    /**
     * stuff for the map
     */
    private GoogleMap mMap;
    private LinkedList<Marker> markers;
    /**
     * what should be drawn.
     */
    private ShapesEnum item;
    /**
     * db
     */
    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //read intent: What should be drawn?
        try {
            Intent intent = getIntent();
            item = (ShapesEnum) intent.getSerializableExtra("item");
        } catch (NullPointerException e) {
            //nothing happens.
        }

        //get db
        daoSession = ((CustomPolyline2Application) getApplicationContext()).getDaoSession();
        markers = new LinkedList<Marker>();
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
                markerOptions.snippet("");
                // Placing a marker on the touched position
                Marker marker = mMap.addMarker(markerOptions);
                //keep track of all the markers.
                markers.add(marker);
            }
        });
    }

    /**
     * finishes the drawing if at least one marker was placed.
     * 1. store
     * 2. give intent to other activity
     *
     * @param view actual view
     */
    public void onClickOk(View view) {
        if (!markers.isEmpty()) {
        switch (item) {
            case POLYGON:
                storePolygon();
                break;
            case POLYLINE:
                storePolyline();
                break;
            case POINT_OF_INTEREST:
                storePointOfInterest();
                break;
            case GROUND_OVERLAY:
                storeGroundOverlay();
                break;
            default:
                break;
        }

        markers = new LinkedList<Marker>();

        Intent intent = new Intent(this, MapsActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent); }
    }

    private void storePolyline() {
            PolygonOptions myPolygon = new PolygonOptions();
            for (Marker entry : markers) {
                myPolygon.add(entry.getPosition());
            }

            Polyline polyline = new Polyline();
            polyline.setLat(markers.getFirst().getPosition().latitude);
            polyline.setLongit(markers.getFirst().getPosition().longitude);
            //TODO: set the Layer (give it to this class)
            //TODO: JSON conversion for storing the points

            PolylineDao polylineDao = daoSession.getPolylineDao();
            polylineDao.insertOrReplace(polyline);
    }

    private void storePolygon() {
            PolygonOptions myPolygon = new PolygonOptions();
            for (Marker entry : markers) {
                myPolygon.add(entry.getPosition());
            }
            Polygon polygon = new Polygon();
            polygon.setLat(markers.getFirst().getPosition().latitude);
            polygon.setLongit(markers.getFirst().getPosition().longitude);
            //TODO: same as above

            PolygonDao polygonDao = daoSession.getPolygonDao();
            polygonDao.insertOrReplace(polygon);
    }

    /**
     * stores a single marker as a point of interest.
     * If user set multiple markers, store the last that was set.
     * TODO: give user error message in case of multiple markers.
     */
    private void storePointOfInterest() {
            Marker marker = markers.get(markers.size() - 1);
            rs.pupin.model.PointOfInterest pointOfInterest = new rs.pupin.model.PointOfInterest();
            pointOfInterest.setLat(marker.getPosition().latitude);
            pointOfInterest.setLongit(marker.getPosition().longitude);
            pointOfInterest.setComment(marker.getSnippet());
            //TODO: set Layer

            PointOfInterestDao pointOfInterestDao = daoSession.getPointOfInterestDao();
            pointOfInterestDao.insertOrReplace(pointOfInterest);
    }

    private void storeGroundOverlay() {
        rs.pupin.model.GroundOverlay groundOverlay = new rs.pupin.model.GroundOverlay();
        groundOverlay.setLat(markers.getFirst().getPosition().latitude);
        groundOverlay.setLongit(markers.getFirst().getPosition().longitude);
        //TODO: set rest of params
        //width, height, rotation, path, layer

        GroundOverlayDao groundOverlayDao = daoSession.getGroundOverlayDao();
        groundOverlayDao.insertOrReplace(groundOverlay);
    }
}
