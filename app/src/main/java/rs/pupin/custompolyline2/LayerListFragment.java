package rs.pupin.custompolyline2;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import rs.pupin.model.DaoSession;
import rs.pupin.model.Layer;
import rs.pupin.model.LayerDao;

/**
 * A simple {@link Fragment} subclass.
 */
public class LayerListFragment extends ListFragment {

    static interface LayerListListener {
        void layerItemClicked(int pos, String value);
    }

    ;

    private LayerListListener listener;
    private DaoSession daoSession;
    ArrayAdapter<String> adapter;

    public LayerListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        daoSession = ((CustomPolyline2Application) getActivity().getApplicationContext()).getDaoSession();
        LayerDao layerDao = daoSession.getLayerDao();
        List<Layer> layers = layerDao.loadAll();
        List<String> result = new LinkedList<String>();
        //1st option is always to add a new Layer
        result.add("Add new Layer");
        for (Layer layer : layers) {
            result.add(layer.getName());
        }

        adapter = new ArrayAdapter<String>(
                inflater.getContext(), android.R.layout.simple_list_item_1,
                result);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity) {
            a = (Activity) context;
            this.listener = (LayerListListener) a;
        }
    }
    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < 23) {
            this.listener = (LayerListListener) activity;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (listener != null) {
            String value = (String) adapter.getItem(position);
            listener.layerItemClicked(position, value);
        }
    }

}
