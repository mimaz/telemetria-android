package putmotorsport.poznan.pl.telemetria.android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ControlFragment extends Fragment {
    private EditText addressInput;
    private EditText portInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.control_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addressInput = view.findViewById(R.id.address_input);
        portInput = view.findViewById(R.id.port_input);
    }

    String getAddress() {
        return addressInput.getText().toString();
    }

    int getPort() {
        return Integer.parseInt(portInput.getText().toString());
    }
}
