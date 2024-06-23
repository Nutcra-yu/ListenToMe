package android.ListenToMe.launch;

import android.ListenToMe.R;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RegisterFragment1 extends Fragment {

    public interface OnClick_Register {
        void onShowFrag2_frag1();
    }

    private final OnClick_Register onClick_register;

    private final Context context;

    private EditText phone;
    private EditText pass1;
    private EditText pass2;

    public RegisterFragment1(Context context, OnClick_Register onClick_register) {
        this.context = context;
        this.onClick_register = onClick_register;
    }

    public String getPhone() {
        return phone.getText().toString();
    }

    public String getPass1() {
        return pass1.getText().toString();
    }

    public String getPass2() {
        return pass2.getText().toString();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment1, container, false);

        phone = view.findViewById(R.id.et_phone_register);

        pass1 = view.findViewById(R.id.rt_passWord1_register);
        pass2 = view.findViewById(R.id.et_passWord2_register);

        Button next = view.findViewById(R.id.btn_next_register);
        next.setOnClickListener(v -> {
            if (onClick_register != null) {
                    onClick_register.onShowFrag2_frag1();
            }
        });


        return view;
    }
}
