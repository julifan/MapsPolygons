package rs.pupin.custompolyline2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.ListActivity;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

import rs.pupin.model.DaoSession;
import rs.pupin.model.Layer;
import rs.pupin.model.LayerDao;

public class LayerListActivity extends ListActivity {
    private DaoSession daoSession;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layer_list);

        listView = (ListView) findViewById(android.R.id.list);

        displayResultList(getEntries());
    }
    private List<String> getEntries() {
        LayerDao layerDao = daoSession.getLayerDao();
        List<Layer> layers = layerDao.loadAll();
        List<String> result = new LinkedList<String>();
        //1st option is always to add a new Layer
        result.add("Add new Layer");
        for (Layer layer : layers) {
            result.add(layer.getName());
        }
        return result;
    }

    private void displayResultList(List<String> result) {
        TextView tView = new TextView(this);
        tView.setText("This data is retrieved from the database and only 4 " +
                "of the results are displayed");
        listView.addHeaderView(tView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, result);
        setListAdapter(arrayAdapter);
        listView.setTextFilterEnabled(true);
    }
}
