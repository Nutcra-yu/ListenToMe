package android.ListenToMe.launch;

import android.ListenToMe.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivityOld extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity_old2);

        Button previous = findViewById(R.id.btn_previous_Register_frag2);
        previous.setOnClickListener(v -> finish());

        Button finish = findViewById(R.id.btn_finish_Register_frag2);
        finish.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivityOld.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}