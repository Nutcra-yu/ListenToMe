package android.ListenToMe.launch;

import androidx.appcompat.app.AppCompatActivity;

import android.ListenToMe.MainMenuActivity;
import android.ListenToMe.MySPManager;
import android.content.Intent;
import android.os.Bundle;
import android.ListenToMe.R;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText phone;
    private EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.et_phone_login);
        pass = findViewById(R.id.et_pass_login);

        //登录按钮
        Button btn_login = findViewById(R.id.btn_login_LoginActivity);
        btn_login.setOnClickListener(v -> {
            if (checkPass()) {
                Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            }else
                Toast.makeText(this,"手机号或密码输入错误",Toast.LENGTH_SHORT).show();
        });

        //注册按钮
        Button btn_register = findViewById(R.id.btn_register_LoginActivity);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean checkPass() {
        boolean checkPass = true;
        MySPManager manager = MySPManager.getSPInstance();

        if (! phone.getText().toString().equals(manager.getString(this,MySPManager.INFO_PHONE)))
            checkPass = false;
        if (! pass.getText().toString().equals(manager.getString(this,MySPManager.INFO_PASSWORD)))
            checkPass = false;

        return checkPass;
    }
}