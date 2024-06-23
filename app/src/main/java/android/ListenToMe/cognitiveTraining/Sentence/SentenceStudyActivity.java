package android.ListenToMe.cognitiveTraining.Sentence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.ListenToMe.cognitiveTraining.Words.WordsStudyActivity;
import android.ListenToMe.R;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SentenceStudyActivity extends AppCompatActivity {

    public static final String CONTENT = "CONTENT{ 0=FOOD 1=PERSON 2=CLOTHES 3=TOYS }";

    /**
     * 控件资源
     */
    private ImageView card_subject;
    private ImageView card_verb;
    private ImageView card_object;

    private TextView name_subject;
    private TextView name_verb;
    private TextView name_object;

    private TextView pinyin_subject;
    private TextView pinyin_verb;
    private TextView pinyin_object;

    private MediaPlayer mediaPlayer;

    /**
     * arraylist
     */
    private ArrayList< HashMap<String,String> > arrayListSubject;
    private ArrayList< HashMap<String,String> > arrayListVerb;
    private ArrayList< HashMap<String,String> > arrayListObject;

    private int indexSubject = 0;
    private int indexVerb = 0;
    private int indexObject = 0;

    private AssetFileDescriptor[] fds = new AssetFileDescriptor[3];
    private int playIndex = 0;

    /**
     * 生成intent
     */
    public static Intent newIntent(Context context,int content){
        Intent intent = new Intent(context, SentenceStudyActivity.class);
        intent.putExtra(CONTENT,content);
        return intent;
    }

    /**
     * onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence_cards_game);

        Log.d("activity", "onCreate: Sentence");

        binding();

        loadArrayFromContent();

        load();

    }

    /**
     * 视图绑定
     */
    private void binding(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d("prepare", String.valueOf(playIndex));
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playIndex += 1;

                if (playIndex < fds.length) {
                    playAudio(fds[playIndex]);
                } else {
                    // 播放完所有音频，停止 MediaPlayer 或进行其他操作
                    playIndex = 0;
                    mediaPlayer.stop();
                }
            }

            private void playAudio(AssetFileDescriptor a) {
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(a.getFileDescriptor(), a.getStartOffset(), a.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        card_subject = findViewById(R.id.iv_cardSubject_SentenceStudyActivity);
        card_verb = findViewById(R.id.iv_cardVerb_SentenceStudyActivity);
        card_object = findViewById(R.id.iv_cardObject_SentenceStudyActivity);
        card_subject.setOnClickListener(v -> loadVoice());
        card_verb.setOnClickListener(v -> loadVoice());
        card_object.setOnClickListener(v -> loadVoice());

        name_subject = findViewById(R.id.tv_nameSubject_SentenceStudyActivity);
        name_verb = findViewById(R.id.tv_nameVerb_SentenceStudyActivity);
        name_object = findViewById(R.id.tv_nameObject_SentenceStudyActivity);

        pinyin_subject = findViewById(R.id.pinyinSubject_SentenceStudyActivity);
        pinyin_verb = findViewById(R.id.pinyinVerb_SentenceStudyActivity);
        pinyin_object = findViewById(R.id.pinyinObject_SentenceStudyActivity);

        Button next = findViewById(R.id.next_SentenceStudyActivity);
        Button previous = findViewById(R.id.previous_SentenceStudyActivity);
        next.setOnClickListener(v -> {
            int subOrObj  = new Random().nextInt(2);
            if (subOrObj== 0){
                indexSubject += 1;
                indexSubject %= arrayListSubject.size();
            } else {
                indexObject += 1;
                indexObject %= arrayListSubject.size();
            }
            load();
        });
        previous.setOnClickListener(v -> {
            int subOrObj  = new Random().nextInt(2);
            if (subOrObj== 0){
                indexSubject += arrayListSubject.size() -1;
                indexSubject %= arrayListSubject.size();
            } else {
                indexObject += arrayListObject.size()-1;
                indexObject %= arrayListObject.size();
            }
            load();
        });

        ImageView iv_back = findViewById(R.id.iv_back_SentenceStudyActivity);
        iv_back.setOnClickListener(v -> finish());
    }

    /**
     * 根据content获取array
     * 1. 获取文件名
     * 2. 根据文件名字符串获取array
     */
    private void loadArrayFromContent(){
        int content = getIntent().getIntExtra(CONTENT,-1);
        String objectStr = "";
        switch(content){
            case 0:
                objectStr = "Food";
                indexVerb = 0;
                break;
            case 3:
            case 1:
            case 2:
                indexVerb = 1;
                objectStr = "Clothes";
                break;
        }
        String subjectFileName = "Table" + "Words" + "Person" + ".json";
        String verbFileName = "Table" + "Words" +"Verb" + ".json";
        String objectFileName = "Table" + "Words" + objectStr + ".json";

        arrayListSubject = loadArrayFromJsonStr(subjectFileName);
        arrayListVerb = loadArrayFromJsonStr(verbFileName);
        arrayListObject = loadArrayFromJsonStr(objectFileName);

    }

    /**
     * 根据文件名字符串获取array
     */
    private ArrayList<HashMap<String,String>> loadArrayFromJsonStr(String fileName){

        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();

        try {
            //读取准备
            InputStream inputStream = getAssets().open(fileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            //全盘读取文件生成字符串
            String jsonContent = new String(buffer);

            //将字符串转换成Array
            JSONArray jsonArray = new JSONArray(jsonContent);
            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("name", jsonArray.getJSONObject(i).getString("name"));
                hashMap.put("pinYin", jsonArray.getJSONObject(i).getString("pinYin"));
                hashMap.put("ENG", jsonArray.getJSONObject(i).getString("ENG"));
                //Log.d("map", String.valueOf(hashMap));
                arrayList.add(hashMap);
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
        return arrayList;
    }

    /**
     * 根据array加载界面资源
     */
    private void load(){
        name_subject.setText(arrayListSubject.get(indexSubject).get("name"));
        pinyin_subject.setText(arrayListSubject.get(indexSubject).get("pinYin"));
        card_subject.setBackground(getDrawableFromArray(arrayListSubject, indexSubject));

        name_verb.setText(arrayListVerb.get(indexVerb).get("name"));
        pinyin_verb.setText(arrayListVerb.get(indexVerb).get("pinYin"));
        card_verb.setBackground(getDrawableFromArray(arrayListVerb,indexVerb));

        name_object.setText(arrayListObject.get(indexObject).get("name"));
        pinyin_object.setText(arrayListObject.get(indexObject).get("pinYin"));
        card_object.setBackground(getDrawableFromArray(arrayListObject,indexObject));

        loadVoice();
    }

    private Drawable getDrawableFromArray(ArrayList<HashMap<String,String>> arrayList,int index){
        String ENGStr = arrayList.get(index).get("ENG");
        assert ENGStr != null;
        String LowerENGStr = "card_" + ENGStr.toLowerCase();
        int drawableVerbId = SentenceStudyActivity.this.getResources().getIdentifier(LowerENGStr,"drawable",SentenceStudyActivity.this.getPackageName());
        return ContextCompat.getDrawable(this, drawableVerbId);
    }

    /**
     * 加载声音资源
     */
    private void loadVoice(){

        //随机播放男声女声
        int boyOrGirl = new Random().nextInt(2);
        String flag;
        if (boyOrGirl == 0) {
            flag = "Boy";
        } else {
            flag = "Girl";
        }

        //获取音频名字 并存入audioFiles[]
        String ENGStrSubject = arrayListSubject.get(indexSubject).get("ENG");
        String voiceFileNameSubject = "Voice" + ENGStrSubject + flag + ".mp3";
        String ENGStrVerb = arrayListVerb.get(indexVerb).get("ENG");
        String voiceFileNameVerb = "Voice" + ENGStrVerb + flag + ".mp3";
        String ENGStrObject = arrayListObject.get(indexObject).get("ENG");
        String voiceFileNameObject = "Voice" + ENGStrObject + flag + ".mp3";

        try {
            fds[0] = getAssets().openFd(voiceFileNameSubject);
            fds[1] = getAssets().openFd(voiceFileNameVerb);
            fds[2] = getAssets().openFd(voiceFileNameObject);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        playIndex = 0;

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }

        mediaPlayer.reset();

        try {
            mediaPlayer.setDataSource(fds[0].getFileDescriptor(), fds[0].getStartOffset(), fds[0].getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

}