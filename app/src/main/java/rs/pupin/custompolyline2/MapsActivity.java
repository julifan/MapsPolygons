package rs.pupin.custompolyline2;

import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rs.pupin.model.DaoSession;
import rs.pupin.model.GroundOverlay;
import rs.pupin.model.GroundOverlayDao;
import rs.pupin.model.Layer;
import rs.pupin.model.LayerDao;
import rs.pupin.model.PointOfInterest;
import rs.pupin.model.PointOfInterestDao;
import rs.pupin.model.PolygonDao;
import rs.pupin.model.Polygon;
import rs.pupin.model.Polyline;
import rs.pupin.model.PolylineDao;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LayerListFragment.LayerListListener,
        ShapesListFragment.ShapesListListener,
        DrawingFragment.DrawingListener,
        StartFragment.StartListener,
        CreateLayerFragment.CreateLayerListener,
        ImageChoosingFragment.ImageListener {

    private GoogleMap mMap;
    private DaoSession daoSession;

    private TextView textView;
    private iImageView imageViewTransform;

    private ImageChoosingFragment imageChoosingFragment = null;

    private boolean draw;
    private boolean mapReady = false;

    /**
     * path of actual img for GroundOverlay
     */
    private String myPath;


    private LinkedList<Marker> markers;
    private GroundOverlay groundOverlayToStore;
    private com.google.android.gms.maps.model.GroundOverlay groundOverlay;

    public static final int REQUEST_CAMERA = 1;

    /**
     * currently active/chosen layer
     */
    private String layerName;
    /**
     * current shape user selected to draw
     */
    private ShapesEnum shape;

    /**
     * init.
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

        textView = (TextView) findViewById(R.id.textView);
        textView.setText("welcome!");
        imageViewTransform = (iImageView) findViewById(R.id.transformImage);


        init();
    }

    private void init() {

        markers = new LinkedList<Marker>();
        groundOverlayToStore = null;
        //get db
        daoSession = ((CustomPolyline2Application) getApplicationContext()).getDaoSession();

        draw = false;

        StartFragment start = new StartFragment();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, start, "layers");
        ft.commit();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * refers to StartFragment.
     * swaps in LayerListFragment.
     */
    public void drawSomethingNewButtonClicked() {
        if (mapReady) {
            draw = true;
            mMap.clear();
            showListLayerFragment();
        }
    }

    /**
     * refers to StartFragment.
     * swaps in LayerListFragment.
     */
    public void showShapesAtLayerButtonClicked() {
        if (mapReady) {
            mMap.clear();
            showListLayerFragment();
        }
    }

    /**
     * refers to ImageChoosingFragment. Is called when the picture is chosen.
     * Places it on the map and gives user the possibility for rotating/etc.
     *
     * @param path   imagepath
     * @param bitmap actual img
     */
    public void imageChosen(String path, Bitmap bitmap) {
        //final String myPath = path;
        myPath = path;
        //TODO: set pic in imgview. allow user to transform img.
        imageViewTransform.setBitmap(bitmap);
        imageViewTransform.setClickable(true);
        //in on finished() set the actual groundOverlay depending on img.

        /*final GroundOverlayOptions options = new GroundOverlayOptions();
        BitmapDescriptor bd = BitmapDescriptorFactory
                .fromBitmap(bitmap);
        options.image(bd);

        groundOverlayToStore = new GroundOverlay();

        textView.setText("Tap the map for positioning of the image");
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {*/

            /**
             * Listener for the Clicks on the map
             * @param latLng lat and longitude where the click was
             */
            /*@Override
            public void onMapClick(LatLng latLng) {
                options.position(latLng, 100000.0f, 100000.0f);
                groundOverlay = mMap.addGroundOverlay(options);

                //set parameters of ground overlay object to store
                groundOverlayToStore.setLat((double) options.getLocation().latitude);
                groundOverlayToStore.setLongit((double) options.getLocation().longitude);
                groundOverlayToStore.setPath(myPath);
                groundOverlayToStore.setHeight((double) options.getHeight());
                groundOverlayToStore.setWidth((double) options.getWidth());
            }
        });
        showDrawingFragment();
        textView.setText("imagefragment funktioniert");*/
    }


    private void showListLayerFragment() {
        LayerListFragment layers = new LayerListFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, layers);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    /**
     * refers to the createLayerFragment
     *
     * @param message name of new layer
     */
    public void createLayerButtonClicked(String message) {
        //store that it is selected
        layerName = message;

        //store layer
        textView.setText(message);
        Layer layerToStore = new Layer();
        layerToStore.setName(layerName);

        LayerDao layerDao = daoSession.getLayerDao();
        layerDao.insert(layerToStore);

        showShapesListFragment();
    }

    /**
     * refers to the LayerListFragment. is called when a layer is selected.
     * if pos == 0: let user create a new layer
     * else:        let user choose what he wants to draw
     *
     * @param pos   chosen layer
     * @param value name of the layer
     */
    public void layerItemClicked(int pos, String value) {
        //draw indicates whether sth new should be drawn or it is just a selection
        if (draw) {
            if (pos == 0) {
                //create a new layer
                CreateLayerFragment createLayer = new CreateLayerFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, createLayer);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
            }
            // display Fragment to choose what he wants to draw.
            else {
                //update current layer
                //layer = pos - 1;
                layerName = value;
                //show next Fragment
                showShapesListFragment();
            }
        } else {
            //do not allow to create new layers bc shapes at a certain layer should be just shown
            if (pos > 0) {
                //store selected layer
                //layer = pos - 1;
                layerName = value;
                //swap start fragment in
                StartFragment start = new StartFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, start);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
                //actually show the shapes on the selected layer
                showShapesButtonClicked();
            }
        }
    }

    private void showShapesListFragment() {
        ShapesListFragment shapes = new ShapesListFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, shapes);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    /**
     * refers to ShapeListFragment
     *
     * @param pos chosen shape
     */
    public void shapeItemClicked(int pos) {
        //store which shape is selected to be drawn
        switch (pos) {
            case 0:
                shape = ShapesEnum.POLYGON;
                setMapClickableMarkers();
                showDrawingFragment();
                break;
            case 1:
                shape = ShapesEnum.POLYLINE;
                setMapClickableMarkers();
                showDrawingFragment();
                break;
            case 2:
                shape = ShapesEnum.POINT_OF_INTEREST;
                setMapClickableMarkers();
                showDrawingFragment();
                break;
            case 3:
                shape = ShapesEnum.GROUND_OVERLAY;
                imageChoosingFragment = new ImageChoosingFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, imageChoosingFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
                //imageChoosingFragment.show(getFragmentManager(), "choosing");
                break;
        }

    }

    private void showDrawingFragment() {
        //setMapClickableMarkers();
        //show next Fragment
        DrawingFragment drawingFragment = new DrawingFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, drawingFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    /**
     * shows all the other shapes on this layer.
     */
    public void showShapesButtonClicked() {
        //get map layer id by name
        List<Layer> layers = daoSession.getLayerDao().queryBuilder().where(LayerDao.Properties.Name.eq(layerName)).list();
        if (!layers.isEmpty()) {
            Long mapLayerId = layers.get(0).getId();
            List<Polyline> polylineList = daoSession.getPolylineDao().queryBuilder()
                    .where(PolylineDao.Properties.Map_layer_id.eq(mapLayerId)).list();
            if (!polylineList.isEmpty()) textView.setText(polylineList.get(0).getMarkers());
            List<Polygon> polygonList = daoSession.getPolygonDao().queryBuilder()
                    .where(PolygonDao.Properties.Map_layer_id.eq(mapLayerId)).list();
            List<PointOfInterest> pointOfInterestList = daoSession.getPointOfInterestDao().queryBuilder()
                    .where(PointOfInterestDao.Properties.Map_layer_id.eq(mapLayerId)).list();
            List<GroundOverlay> groundOverlayList = daoSession.getGroundOverlayDao().queryBuilder()
                    .where(GroundOverlayDao.Properties.Map_layer_id.eq(mapLayerId)).list();

            List<Vec2f> markerList = new ArrayList<Vec2f>();
            for (Polyline polyline : polylineList) {
                markerList = convertMarkers(polyline.getMarkers());
                if (markerList != null) showPolyline(markerList);
            }

            for (Polygon polygon : polygonList) {
                markerList = convertMarkers(polygon.getMarkers());
                if (markerList != null) showPolygon(markerList);
            }

            for (PointOfInterest pointOfInterest : pointOfInterestList) {
                showPointOfInterest(new Vec2f(pointOfInterest.getLat().floatValue(),
                        pointOfInterest.getLongit().floatValue()));
            }

            for (GroundOverlay groundOverlay : groundOverlayList) {
                showGroundOverlay(groundOverlay);
            }

            //TODO: show GroundOverlays
        }
    }

    /**
     * returns to the starting screen
     */
    public void finishedButtonClicked() {
        StartFragment startFragment = new StartFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, startFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

        setMapUnclickable();
        draw = false;
        mMap.clear();
    }

    public void okButtonClicked() {
        if (!markers.isEmpty() || groundOverlayToStore != null) {
            //setMapUnclickable();
            //mMap.clear();
            //store and show
            switch (shape) {
                case POLYGON:
                    storePolygon();
                    showPolygon();
                    break;
                case POLYLINE:
                    storePolyline();
                    showPolyline();
                    break;
                case POINT_OF_INTEREST:
                    storePointOfInterest();
                    showPointOfInterest();
                    break;
                case GROUND_OVERLAY:
                    storeGroundOverlay();
                    StartFragment startFragment = new StartFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, startFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    fragmentTransaction.commit();

                    setMapUnclickable();
                    draw = false;
                    mMap.clear();
                    //showGroundOverlay();
                    break;
            }

            //empty markers
            markers = new LinkedList<Marker>();
        }
    }

    private void setMapClickableMarkers() {
        //make map clickable
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
    }

    private void setMapUnclickable() {
        //make map unclickable
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
            }
        });
    }

    private void storePolyline() {
        //setting up the db object
        Polyline polyline = new Polyline();
        polyline.setLat(markers.getFirst().getPosition().latitude);
        polyline.setLongit(markers.getFirst().getPosition().longitude);
        List<Layer> layers = daoSession.getLayerDao().queryBuilder().where(LayerDao.Properties.Name.eq(layerName)).list();
        if (!layers.isEmpty()) {
            Long mapLayerId = layers.get(0).getId();
            polyline.setLayer(layers.get(0));
        }
        //polyline.setLayer(daoSession.getLayerDao().loadAll().get(layer));
        polyline.setMarkers(convertMarkers(markers));

        //actual storing
        PolylineDao polylineDao = daoSession.getPolylineDao();
        polylineDao.insert(polyline);
    }

    private void storePolygon() {
        //setting up the db object
        Polygon polygon = new Polygon();
        polygon.setLat(markers.getFirst().getPosition().latitude);
        polygon.setLongit(markers.getFirst().getPosition().longitude);

        List<Layer> layers = daoSession.getLayerDao().queryBuilder().where(LayerDao.Properties.Name.eq(layerName)).list();
        if (!layers.isEmpty()) {
            Long mapLayerId = layers.get(0).getId();
            polygon.setLayer(layers.get(0));
        }

        //polygon.setLayer(daoSession.getLayerDao().loadAll().get(layer));
        polygon.setMarkers(convertMarkers(markers));

        //actual storing
        PolygonDao polygonDao = daoSession.getPolygonDao();
        polygonDao.insertOrReplace(polygon);
    }

    /**
     * stores a single marker as a point of interest.
     * If user set multiple markers, store the last that was set.
     */
    private void storePointOfInterest() {
        Marker marker = markers.get(markers.size() - 1);
        rs.pupin.model.PointOfInterest pointOfInterest = new rs.pupin.model.PointOfInterest();
        pointOfInterest.setLat(marker.getPosition().latitude);
        pointOfInterest.setLongit(marker.getPosition().longitude);
        pointOfInterest.setComment(marker.getSnippet());

        List<Layer> layers = daoSession.getLayerDao().queryBuilder().where(LayerDao.Properties.Name.eq(layerName)).list();
        if (!layers.isEmpty()) {
            Long mapLayerId = layers.get(0).getId();
            pointOfInterest.setLayer(layers.get(0));
        }
        //pointOfInterest.setLayer(daoSession.getLayerDao().loadAll().get(layer));

        PointOfInterestDao pointOfInterestDao = daoSession.getPointOfInterestDao();
        pointOfInterestDao.insertOrReplace(pointOfInterest);
    }

    private void storeGroundOverlay() {
        rs.pupin.model.GroundOverlay groundOverlay = new rs.pupin.model.GroundOverlay();
        groundOverlay = groundOverlayToStore;
        List<Layer> layers = daoSession.getLayerDao().queryBuilder().where(LayerDao.Properties.Name.eq(layerName)).list();
        if (!layers.isEmpty()) {
            Long mapLayerId = layers.get(0).getId();
            groundOverlay.setLayer(layers.get(0));
        }

        GroundOverlayDao groundOverlayDao = daoSession.getGroundOverlayDao();
        groundOverlayDao.insertOrReplace(groundOverlay);
    }

    /**
     * converts the global markers List to JSON
     * Only takes care of the coordinates. Rest is ignored.
     *
     * @return json of the list
     */
    private String convertMarkers(List<Marker> markers) {
        List<Vec2f> latLong = new ArrayList<Vec2f>();
        for (Marker marker : markers) {
            latLong.add(new Vec2f((float) marker.getPosition().latitude, (float) marker.getPosition().longitude));
        }
        String json = new Gson().toJson(latLong);
        return json;
    }

    private List<Vec2f> convertMarkers(String toConvert) {
        List<Vec2f> latLong = new ArrayList<Vec2f>();
        latLong = new Gson().fromJson(toConvert, new TypeToken<List<Vec2f>>() {
        }.getType());

        return latLong;
    }

    private void showPolygon() {
        PolygonOptions polygonOptions = new PolygonOptions();
        for (Marker marker : markers) {
            polygonOptions.add(marker.getPosition());
        }
        mMap.addPolygon(polygonOptions);
    }

    private void showPolygon(List<Vec2f> coordinates) {
        PolygonOptions polygonOptions = new PolygonOptions();
        for (Vec2f vec2f : coordinates) {
            polygonOptions.add(new LatLng(vec2f.getX(), vec2f.getY()));
        }
        mMap.addPolygon(polygonOptions);
    }

    private void showPolyline() {
        PolylineOptions polylineOptions = new PolylineOptions();
        for (Marker marker : markers) {
            polylineOptions.add(marker.getPosition());
        }
        mMap.addPolyline(polylineOptions);
    }

    private void showPolyline(List<Vec2f> coordinates) {
        PolylineOptions polylineOptions = new PolylineOptions();
        for (Vec2f vec2f : coordinates) {
            polylineOptions.add(new LatLng(vec2f.getX(), vec2f.getY()));
        }
        mMap.addPolyline(polylineOptions);
    }

    private void showPointOfInterest() {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(markers.get(markers.size() - 1).getPosition());
        mMap.addMarker(markerOptions);
    }

    private void showPointOfInterest(Vec2f point) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(point.getX(), point.getY()));
        mMap.addMarker(markerOptions);
    }

    private void showGroundOverlay() {
        GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions();
        groundOverlayOptions.position(new LatLng(groundOverlayToStore.getLat(),
                        groundOverlayToStore.getLongit()),
                groundOverlayToStore.getWidth().floatValue(),
                groundOverlayToStore.getHeight().floatValue());
        final File image = new File(groundOverlayToStore.getPath());
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        bitmap = Bitmap.createBitmap(bitmap);

        BitmapDescriptor bd = BitmapDescriptorFactory
                .fromBitmap(bitmap);
        groundOverlayOptions.image(bd);
        mMap.addGroundOverlay(groundOverlayOptions);
    }

    private void showGroundOverlay(GroundOverlay groundOverlay) {
        GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions();
        groundOverlayOptions.position(new LatLng(groundOverlay.getLat(),
                        groundOverlay.getLongit()),
                groundOverlay.getWidth().floatValue(),
                groundOverlay.getHeight().floatValue());
        final File image = new File(groundOverlay.getPath());
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        bitmap = Bitmap.createBitmap(bitmap);

        BitmapDescriptor bd = BitmapDescriptorFactory
                .fromBitmap(bitmap);
        groundOverlayOptions.image(bd);
        mMap.addGroundOverlay(groundOverlayOptions);
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
        mapReady = true;
    }
}