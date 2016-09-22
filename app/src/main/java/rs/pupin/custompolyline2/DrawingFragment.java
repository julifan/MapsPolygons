package rs.pupin.custompolyline2;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.lang.annotation.Target;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrawingFragment extends Fragment implements View.OnClickListener {
    static interface DrawingListener {
        void okButtonClicked();
        void showShapesButtonClicked();
        void finishedButtonClicked();
    }

    private DrawingListener listener;


    public DrawingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_drawing, container, false);

        Button okButton = (Button) v.findViewById(R.id.ok);
        Button showShapesButton = (Button) v.findViewById(R.id.showShapes);
        Button finishedButton = (Button) v.findViewById(R.id.finished);

        okButton.setOnClickListener(this);
        showShapesButton.setOnClickListener(this);
        finishedButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            switch (v.getId()) {
                case R.id.ok: listener.okButtonClicked();
                    break;
                case R.id.showShapes: listener.showShapesButtonClicked();
                    break;
                case R.id.finished: listener.finishedButtonClicked();
                    break;
                default: break;
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
            this.listener = (DrawingListener) a;
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
            this.listener = (DrawingListener) activity;
        }
    }
}
