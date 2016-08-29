package rs.pupin.custompolyline2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ShapesListActivity extends AppCompatActivity {

    //TODO: initialize properly
    int layerPos;
    String layerName;

    /**
     * is called from MapsActivity when something new should be added.
     * displays a listView where you can choose:
     * 1. create a new layer
     * 2. one of the already existing layers
     *
     * @param savedInstanceState saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shapes_list);
        //Create an OnItemClickListener
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView,
                                            View v,
                                            int position,
                                            long id) {
                        //TODO: figure out position dynamically
                        switch (position) {
                            case 0:
                                makeIntent(ShapesEnum.POLYGON);
                            case 1:
                                makeIntent(ShapesEnum.POLYLINE);
                            case 2:
                                makeIntent(ShapesEnum.POINT_OF_INTEREST);
                            case 3:
                                makeIntent(ShapesEnum.GROUND_OVERLAY);
                        }
                    }
                };
        //Add the listener to the list view
        ListView listView = (ListView) findViewById(R.id.list_options);
        try {
            listView.setOnItemClickListener(itemClickListener);
        } catch (NullPointerException e) {
            //nothing happens
        }

        Intent intent = getIntent();
        //try {
        layerPos = intent.getIntExtra("position", 0);
        layerName = intent.getStringExtra("value");

        //} catch (Exception e) {

        //}
    }

    /**
     * create intent and launch next activity where the specified
     * item can be drawn
     *
     * @param shapesEnum to put in intent. code for what should be drawn.
     */
    public void makeIntent(ShapesEnum shapesEnum) {
        Intent intent = new Intent(this,
                DrawingMapsActivity.class);
        intent.putExtra("item", shapesEnum);
        intent.putExtra("position", layerPos);
        intent.putExtra("value", layerName);
        startActivity(intent);
    }
}
