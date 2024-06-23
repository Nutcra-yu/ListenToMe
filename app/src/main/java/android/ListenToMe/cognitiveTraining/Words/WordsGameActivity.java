package android.ListenToMe.cognitiveTraining.Words;

import android.ListenToMe.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class WordsGameActivity extends AppCompatActivity {

    public static final String CONTENT = "CONTENT{ 0=FOOD 1=PERSON 2=CLOTHES 3=TOYS }";

    public static Intent newIntent(Context context,int content){
        Intent intent = new Intent(context,WordsStudyActivity.class);
        intent.putExtra(CONTENT,content);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_cards_game);

        //返回
        ImageView iv_back = findViewById(R.id.iv_back_WordsCardsGameActivity);
        iv_back.setOnClickListener(v->finish());

    }

}