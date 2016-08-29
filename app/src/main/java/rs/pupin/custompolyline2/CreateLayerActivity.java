package rs.pupin.custompolyline2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import rs.pupin.model.DaoSession;
import rs.pupin.model.Layer;
import rs.pupin.model.LayerDao;

public class CreateLayerActivity extends AppCompatActivity {

    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_layer);
        daoSession = ((CustomPolyline2Application) getApplicationContext()).getDaoSession();
    }

    public void onClickCreateLayer(View view) {
        EditText messageView = (EditText) findViewById(R.id.message);
        String layerName = messageView.getText().toString();

        //store layerName
        Layer layer = new Layer();
        layer.setName(layerName);

        LayerDao layerDao = daoSession.getLayerDao();
        layerDao.insertOrReplace(layer);

        Intent intent = new Intent(this, LayerListActivity.class);
        startActivity(intent);
    }
}
