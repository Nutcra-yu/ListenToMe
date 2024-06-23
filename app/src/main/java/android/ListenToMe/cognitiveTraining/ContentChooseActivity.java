package android.ListenToMe.cognitiveTraining;

import androidx.appcompat.app.AppCompatActivity;

import android.ListenToMe.cognitiveTraining.Phrase.PhraseStudyActivity;
import android.ListenToMe.cognitiveTraining.Sentence.SentenceStudyActivity;
import android.ListenToMe.cognitiveTraining.Words.WordsStudyActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.ListenToMe.R;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 短语训练 内容选择
 */
public class ContentChooseActivity extends AppCompatActivity{

    public static final String LEVEL = "LEVEL { 0=WORD 1=PHRASE 2=SENTENCE }";


    private ImageView food;
    private ImageView person;
    private ImageView clothes;
    private ImageView toys;

    private Intent intent;

    public static Intent newIntent(Context context, int level) {
        Intent intent = new Intent(context, ContentChooseActivity.class);
        intent.putExtra(LEVEL,level);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_choose);

        food = findViewById(R.id.food);
        food.setOnClickListener(v -> start(0) );

        person = findViewById(R.id.person);
        person.setOnClickListener(v -> start(1));

        clothes = findViewById(R.id.clothes);
        clothes.setOnClickListener(v -> start(2));

        toys = findViewById(R.id.toys);
        toys.setOnClickListener(v -> start(3));

        Button btn_data_MainMenuActivity = findViewById(R.id.btn_data_MainMenuActivity);
        int level = getIntent().getIntExtra(LEVEL,-1);
        String levelStr = "";
        switch (level){
            case 0:
                levelStr = "名词模块";
                break;
            case 1:
                levelStr = "短语模块";
                break;
            case 2:
                levelStr = "句子模块";
                break;
        }
        btn_data_MainMenuActivity.setText(levelStr);

        //返回
        ImageView iv_back = findViewById(R.id.iv_back_WordChooseActivity);
        iv_back.setOnClickListener(v -> finish());
    }

    public void start(int content){
        int level = getIntent().getIntExtra(LEVEL,-1);
        switch (level){
            case 0:
                Log.d("level", "Words" + level);
                intent = WordsStudyActivity.newIntent(ContentChooseActivity.this,content);
                break;
            case 1:
                Log.d("level", "Phrase" + level);
                intent = PhraseStudyActivity.newIntent(ContentChooseActivity.this,content);
                break;
            case 2:
                Log.d("level","Sentence" + level );
                intent = SentenceStudyActivity.newIntent(ContentChooseActivity.this,content);
                break;
        }
        startActivity(intent);
    }

}