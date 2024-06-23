package android.ListenToMe.cognitiveTraining.Phrase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.ListenToMe.R;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class PhraseStudyActivity extends AppCompatActivity {

    public static final String CONTENT = "CONTENT{ 0=FOOD 1=PERSON 2=CLOTHES 3=TOYS }";
    private ArrayList<HashMap<String, String>> arrayListVerb;
    private ArrayList<HashMap<String, String>> arrayListObject;
    private int objectCurrentId = 0;
    private int verbCurrentId = 0;

    private MediaPlayer mediaPlayer;
    private String[] audioFiles = new String[3];
    private AssetFileDescriptor[] fds = new AssetFileDescriptor[2];
    private int playCurrentIndex = 0;

    private ImageView cardVerb;
    private ImageView cardObject;

    private TextView pinyinVerb;
    private TextView pinyinObject;
    private TextView nameVerb;
    private TextView nameObject;

    public static Intent newIntent(Context context, int content) {
        Intent intent = new Intent(context, PhraseStudyActivity.class);
        intent.putExtra(CONTENT, content);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrase_cards_game);

        Log.d("activity", "PhraseStudyActivity");

        binding();

        getContentToArray();

        loadFromArray();

    }

    /**
     * 视图绑定
     */
    private void binding() {
        mediaPlayer = new MediaPlayer();

        cardVerb = findViewById(R.id.iv_cardVerb_PhraseStudyActivity);
        cardObject = findViewById(R.id.iv_cardObject_PhraseStudyActivity);

        nameVerb = findViewById(R.id.tv_nameVerb_PhraseStudyActivity);
        nameObject = findViewById(R.id.tv_nameObject_PhraseStudyActivity);

        pinyinVerb = findViewById(R.id.pinyinVerb_PhraseStudyActivity);
        pinyinObject = findViewById(R.id.pinyinObject_PhraseStudyActivity);

        Button next = findViewById(R.id.next_PhraseStudyActivity);
        Button previous = findViewById(R.id.previous_PhraseStudyActivity);

        cardVerb.setOnClickListener(v -> loadVoice());
        cardObject.setOnClickListener(v -> loadVoice());

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d("prepare", String.valueOf(playCurrentIndex));
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playCurrentIndex += 1;

                if (playCurrentIndex < fds.length) {
                    playAudio(fds[playCurrentIndex]);
                } else {
                    // 播放完所有音频，停止 MediaPlayer 或进行其他操作
                    playCurrentIndex = 0;
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

        next.setOnClickListener(v -> {
            objectCurrentId += 1;
            objectCurrentId %= arrayListObject.size();
            loadFromArray();
        });

        previous.setOnClickListener(v -> {
            objectCurrentId += arrayListObject.size() - 1;
            objectCurrentId %= arrayListObject.size();
            loadFromArray();
        });

        ImageView iv_back = findViewById(R.id.iv_back_PhraseStudyActivity);
        iv_back.setOnClickListener(v -> finish());
    }

    /**
     * 加载json
     */
    private void getContentToArray() {

        /*通过content生成json名字*/
        int content = getIntent().getIntExtra(CONTENT, -1);
        String objectTableStr = "";
        switch (content) {
            case 3:
            case 1:
            case 2:
                verbCurrentId = 1;
                objectTableStr = "Clothes";
                break;
            case 0:
                verbCurrentId = 0;
                objectTableStr = "Food";
                break;
        }
        String objectJsonName = "Table" + "Words" + objectTableStr + ".json";
        String verbJsonName = "Table" + "Words" + "Verb" + ".json";

        Log.d("Object filename", objectJsonName);
        Log.d("Verb filename", verbJsonName);

        //使用获得的fileName将文件读取到Array
        arrayListObject = fromJsonToArray(objectJsonName);
        arrayListVerb = fromJsonToArray(verbJsonName);

    }

    /**
     * 通过文件名 将此文件返回为一个array
     */
    private ArrayList<HashMap<String, String>> fromJsonToArray(String fileName) {

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        try {
            //读取准备
            InputStream inputStream = getAssets().open(fileName);//open需要catch
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            // 将json文件全盘读取为string
            String jsonString = new String(buffer);

            //读取出来的jsonString生成jsonArray
            JSONArray jsonArray = new JSONArray(jsonString);
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
     * 根据json加载界面资源
     */
    private void loadFromArray() {
        /*Verb*/
        pinyinVerb.setText(arrayListVerb.get(verbCurrentId).get("pinYin"));
        nameVerb.setText(arrayListVerb.get(verbCurrentId).get("name"));

        String verbENGStr = arrayListVerb.get(verbCurrentId).get("ENG");
        assert verbENGStr != null;
        String verbLowerENGStr = "card_" + verbENGStr.toLowerCase();
        int drawableVerbId = PhraseStudyActivity.this.getResources().getIdentifier(verbLowerENGStr, "drawable", PhraseStudyActivity.this.getPackageName());
        Drawable drawableVerb = ContextCompat.getDrawable(PhraseStudyActivity.this, drawableVerbId);
        cardVerb.setImageDrawable(drawableVerb);


        /*Object*/
        //设置文本
        pinyinObject.setText(arrayListObject.get(objectCurrentId).get("pinYin"));
        nameObject.setText(arrayListObject.get(objectCurrentId).get("name"));

        //设置图片
        String objectENGStr = arrayListObject.get(objectCurrentId).get("ENG");
        assert objectENGStr != null;
        String objectLowerENGStr = "card_" + objectENGStr.toLowerCase();
        int drawableId = PhraseStudyActivity.this.getResources().getIdentifier(objectLowerENGStr, "drawable", PhraseStudyActivity.this.getPackageName());
        Drawable drawableObject = ContextCompat.getDrawable(PhraseStudyActivity.this, drawableId);
        cardObject.setImageDrawable(drawableObject);

        //设置声音
        loadVoice();
    }

    /**
     * 加载声音资源
     */
    private void loadVoice() {

        //随机播放男声女声
        int boyOrGirl = new Random().nextInt(2);
        String flag;
        if (boyOrGirl == 0) {
            flag = "Boy";
        } else {
            flag = "Girl";
        }

        //获取音频名字 并存入audioFiles[]
        String verbENGStr = arrayListVerb.get(verbCurrentId).get("ENG");
        audioFiles[0] = "Voice" + verbENGStr + flag + ".mp3";
        String objectENGStr = arrayListObject.get(objectCurrentId).get("ENG");
        audioFiles[1] = "Voice" + objectENGStr + flag + ".mp3";
        Log.d("objectStr", "Object:" + audioFiles[1]);
        try {
            AssetFileDescriptor fd = getAssets().openFd(audioFiles[0]);
            fds[0] = fd;
            fd = getAssets().openFd(audioFiles[1]);
            fds[1] = fd;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        playCurrentIndex = 0;

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