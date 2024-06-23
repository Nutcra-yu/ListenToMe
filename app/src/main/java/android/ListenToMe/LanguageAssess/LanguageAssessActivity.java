package android.ListenToMe.LanguageAssess;

import android.ListenToMe.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class LanguageAssessActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_assess_new);

        //返回
        ImageView iv_back = findViewById(R.id.iv_back_LanguageAssessActivity);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //ABC
        Button btn_ABC = findViewById(R.id.btn_ABC_LanguageAssessActivity);
        btn_ABC.setOnClickListener(v-> clickToAccess("a_ABC.json"));

        //PCDI手势
        Button btn_PCDIgesture = findViewById(R.id.btn_PCDIgesture_LanguageAssessActivity);
        btn_PCDIgesture.setOnClickListener(v->clickToAccess("a_PCDI_Gesture.json"));

        //PCDI句子
        Button btn_PCDIsentence = findViewById(R.id.btn_PCDIsentence_LanguageAssessActivity);
        btn_PCDIsentence.setOnClickListener(v->clickToAccess("a_PCDI_Sentence.json"));



    }

    private void clickToAccess(String fileName){
        Intent intent = AccessActivity.newAccess(LanguageAssessActivity.this,fileName);
        startActivity(intent);
    }
}
