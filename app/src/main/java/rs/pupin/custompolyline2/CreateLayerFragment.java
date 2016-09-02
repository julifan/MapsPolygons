package rs.pupin.custompolyline2;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateLayerFragment extends Fragment implements View.OnClickListener {
    static interface CreateLayerListener {
        void createLayerButtonClicked(String message);
    }

    private CreateLayerListener listener;
    private EditText editText;

    public CreateLayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_layer, container, false);

        Button okButton = (Button) v.findViewById(R.id.createLayer);
        editText = (EditText) v.findViewById(R.id.message2);
        okButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            Editable text = editText.getText();
            if (text != null) {
                String message = text.toString();
                listener.createLayerButtonClicked(message);
            }

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity) {
            a = (Activity) context;
            this.listener = (CreateLayerListener) a;
        }
    }
}
