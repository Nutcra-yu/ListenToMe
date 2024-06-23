package android.ListenToMe.cognitiveTraining;

import android.ListenToMe.cognitiveTraining.Words.WordsGameActivity;
import android.ListenToMe.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.Arrays;

/**
 * CardsActivity -> CardsChooseActivity -> CardsWordsGameActivity
 * 模式选择 词语短语句子  {@link CognitiveActivity }
 * 内容选择 人物食物玩具  {@link }
 * 游戏界面             {@link WordsGameActivity }
 * 每一次选择之后添加extra至下一个 实现复用
 */
public class CognitiveActivity extends AppCompatActivity {

    int[] flags = {0,0,0};
    private ImageView iv_word;
    private ImageView iv_phrase;
    private ImageView iv_sentence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        //词语
        iv_word = findViewById(R.id.cardgame_iv_word);
        iv_word.setOnClickListener(v -> click(0));

        //短语
        iv_phrase = findViewById(R.id.cardgame_iv_phrase);
        iv_phrase.setOnClickListener(v -> click(1));

        //句子
        iv_sentence = findViewById(R.id.cardgame_iv_sentence);
        iv_sentence.setOnClickListener(v -> click(2));

        //返回
        ImageView iv_back = findViewById(R.id.iv_back_CardsActivity);
        iv_back.setOnClickListener(v -> finish());
    }

    private void click(int level){
//        if (flags[level] == 0 ){
//            Arrays.fill(flags,0);
//            flags[level] = 1;
//            loadDrawables();
//        }else {
            Intent intent = ContentChooseActivity.newIntent(CognitiveActivity.this,level);
            startActivity(intent);
//        }
    }

    private void loadDrawables() {
        if (flags[0] == 0){
            iv_word.setBackground( ResourcesCompat.getDrawable(getResources(),R.drawable.game_choose_word,null));
        }else {
            iv_word.setBackground( ResourcesCompat.getDrawable(getResources(),R.drawable.game_choose_word_clicked,null));
        }

        if (flags[1] == 0){
            iv_phrase.setBackground( ResourcesCompat.getDrawable(getResources(),R.drawable.game_choose_phrase,null));
        }else {
            iv_phrase.setBackground( ResourcesCompat.getDrawable(getResources(),R.drawable.game_choose_phrase_clicked,null));
        }

        if (flags[2] == 0){
            iv_sentence.setBackground( ResourcesCompat.getDrawable(getResources(),R.drawable.game_choose_sentence,null));
        }else {
            iv_sentence.setBackground( ResourcesCompat.getDrawable(getResources(),R.drawable.game_choose_sentence_clicked,null));
        }
    }
}