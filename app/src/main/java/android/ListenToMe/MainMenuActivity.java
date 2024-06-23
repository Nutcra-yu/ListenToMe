package android.ListenToMe;

import android.ListenToMe.cognitiveTraining.CognitiveActivity;
import android.ListenToMe.LanguageAssess.LanguageAssessActivity;
import android.ListenToMe.ListenToMe.ListenToMeActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//进入的第一个界面
public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //设置按钮
        ImageView btn_setting = findViewById(R.id.btn_setting_MainMenuActivity);
        btn_setting.setOnClickListener(v -> {
            Toast.makeText(MainMenuActivity.this,"正在开发", Toast.LENGTH_SHORT).show();
        });

        //语言评估按钮
        ImageView btn_language = findViewById(R.id.imageview_languageassess_MainMenuActivity);
        btn_language.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, LanguageAssessActivity.class);
            startActivity(intent);
        });

        //认知训练按钮
        ImageView btn_cards = findViewById(R.id.imageview_cardgame_MainMenuActivity);
        btn_cards.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, CognitiveActivity.class);
            startActivity(intent);
        });

        //听我说按钮
        ImageView btn_listen = findViewById(R.id.imageview_listentome_MainMenuActivity);
        btn_listen.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, ListenToMeActivity.class);
            startActivity(intent);
        });

        //过程数据按钮
        Button btn_data = findViewById(R.id.btn_data_MainMenuActivity);
        btn_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainMenuActivity.this,"正在开发",Toast.LENGTH_SHORT).show();
                Log.d("btn_data","btn_data");
            }
        });
    }
}