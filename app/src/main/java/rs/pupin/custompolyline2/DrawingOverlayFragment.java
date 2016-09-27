package rs.pupin.custompolyline2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class DrawingOverlayFragment extends Fragment implements View.OnClickListener {

    static interface DrawingOverlayListener {
        void okButtonClicked();

        void showShapesButtonClicked();

        void finishedButtonClicked();

        void changeButtonClicked(float width, float height, float rot);

    }

    EditText setHeight;
    EditText setWidth;
    EditText setRot;


    private DrawingOverlayListener listener;

    public DrawingOverlayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_drawing_overlay, container, false);

        Button okButton = (Button) v.findViewById(R.id.ok);
        Button showShapesButton = (Button) v.findViewById(R.id.showShapes);
        Button finishedButton = (Button) v.findViewById(R.id.finished);
        Button changeButton = (Button) v.findViewById(R.id.change);

        okButton.setOnClickListener(this);
        showShapesButton.setOnClickListener(this);
        finishedButton.setOnClickListener(this);
        changeButton.setOnClickListener(this);


        setHeight = (EditText) v.findViewById(R.id.setHeight);
        setWidth = (EditText) v.findViewById(R.id.setWidth);
        setRot = (EditText) v.findViewById(R.id.setRot);


        return v;
    }


    @Override
    public void onClick(View v) {
        if (listener != null) {
            switch (v.getId()) {
                case R.id.ok:
                    listener.okButtonClicked();
                    break;
                case R.id.showShapes:
                    listener.showShapesButtonClicked();
                    break;
                case R.id.finished:
                    listener.finishedButtonClicked();
                    break;
                case R.id.change:
                    if (inputValid()) {
                        listener.changeButtonClicked(getWidth() * 10000, getHeight() * 10000, getRot());
                    }
                default:
                    break;
            }
        }
    }

    private float getWidth() {
        Editable textW = setWidth.getText();
        if (textW != null) {
            try {
                float width = Float.parseFloat(textW.toString());
                return width;
                //TODO: do not catch exception
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0.f;
    }

    private float getHeight() {
        Editable textH = setHeight.getText();
        if (textH != null) {
            try {
                float height = Float.parseFloat(textH.toString());
                return height;
                //TODO: do not catch exception
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0.f;
    }

    private float getRot() {
        Editable textR = setRot.getText();
        if (textR != null) {
            try {
                float rot = Float.parseFloat(textR.toString());
                return rot;
                //TODO: do not catch exception
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0.f;
    }

    /**
     * checks if input in the edit texts field is valid.
     *
     * @return true if yes false if not
     */
    private boolean inputValid() {
                if (getWidth() > 0 && getHeight() > 0 && getRot() >= 0) {
                    return true;
                }
        return false;

    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity) {
            a = (Activity) context;
            this.listener = (DrawingOverlayListener) a;
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
            this.listener = (DrawingOverlayListener) activity;
        }
    }

}
