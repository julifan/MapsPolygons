package rs.pupin.custompolyline2;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment implements View.OnClickListener {
    static interface StartListener {
        void drawSomethingNewButtonClicked();

        void showShapesAtLayerButtonClicked();
    }

    private StartListener listener;


    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {// Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_start, container, false);

        Button drawSomethingNewButton = (Button) v.findViewById(R.id.drawSomethingNew);
        Button showShapesAtLayerButton = (Button) v.findViewById(R.id.showShapesAtLayer);

        drawSomethingNewButton.setOnClickListener(this);
        showShapesAtLayerButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            switch (v.getId()) {
                case R.id.drawSomethingNew:
                    listener.drawSomethingNewButtonClicked();
                    break;
                case R.id.showShapesAtLayer:
                    listener.showShapesAtLayerButtonClicked();
                    break;
                default:
                    break;
            }
        }
    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity) {
            a = (Activity) context;
            this.listener = (StartListener) a;
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
            this.listener = (StartListener) activity;
        }
    }

}
