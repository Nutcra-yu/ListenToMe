package android.ListenToMe.cognitiveTraining.Words;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.ListenToMe.R;
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

public class WordsStudyActivity extends AppCompatActivity {



    private ArrayList< HashMap<String,String> > arrayList;
    private int currentId = 0;
    private MediaPlayer mediaPlayer;

    private TextView pin_yin;
    private TextView name;
    private ImageView card;

    public static final String CONTENT = "CONTENT{ 0=FOOD 1=PERSON 2=CLOTHES 3=TOYS }";

    public static Intent newIntent(Context context,int content){
        Intent intent = new Intent(context,WordsStudyActivity.class);
        intent.putExtra(CONTENT,content);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_study);

        Log.d("activity", "onCreate: Word");

        //绑定控件
        bindView();

        arrayList = getArray();
        load();


    }

    /**
     * 视图绑定
     */
    private void bindView() {
        mediaPlayer = new MediaPlayer();

        pin_yin = findViewById(R.id.pinyin_WordsStudyActivity);
        name = findViewById(R.id.name_WordsStudyActivity1);
        Button next = findViewById(R.id.next_WordsStudyActivity);
        Button previous = findViewById(R.id.previous_WordsStudyActivity);
        card = findViewById(R.id.card_WordsStudyActivity1);
        ImageView back = findViewById(R.id.iv_back_WordsStudyActivity);

        back.setOnClickListener(v -> finish());

        card.setOnClickListener(v -> loadVoice());

        next.setOnClickListener(v -> {
            currentId += 1;
            currentId %= arrayList.size();
            load();
        });

        previous.setOnClickListener(v -> {
            currentId += arrayList.size()-1;
            currentId %= arrayList.size();
            load();
        });
    }

    /**
     * 从json资源中获取数据
     * @return 资源结构体（汉字 拼音 图片）
     */
    private ArrayList< HashMap<String,String> > getArray() {

        //通过content生成json文件名
        //CONTENT{ 0=FOOD 1=PERSON 2=CLOTHES 3=TOYS }
        int content = getIntent().getIntExtra(CONTENT,-1);
        String contentStr = "";
        switch (content){
            case 0:
                contentStr = "Food";
                break;
            case 1:
                contentStr = "Person";
                break;
            case 2:
                contentStr = "Clothes";
                break;
            case 3:
                contentStr = "Toys";
                break;
        }
        String fileName = "Table" + "Words" + contentStr + ".json";

        Log.d("fileName", fileName);


        /*获取json资源*/
        AssetManager assetManager = this.getAssets();
        String jsonString;
        ArrayList< HashMap<String,String> > arrayList = new ArrayList<>();
        try {
            InputStream inputStream = assetManager.open(fileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            // 生成string
            jsonString = new String(buffer);

            JSONArray jsonArray=new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("name", jsonArray.getJSONObject(i).getString("name"));
                hashMap.put("pinYin", jsonArray.getJSONObject(i).getString("pinYin"));
                hashMap.put("ENG", jsonArray.getJSONObject(i).getString("ENG"));
                //Log.d("map", String.valueOf(hashMap));
                arrayList.add(hashMap);
            }
            return arrayList;

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 根据获取的json加载界面资源
     */
    private void load(){
        Log.d("currentId", String.valueOf(currentId));

        pin_yin.setText(arrayList.get(currentId).get("pinYin"));
        name.setText(arrayList.get(currentId).get("name"));

        String strENG = arrayList.get(currentId).get("ENG");
        assert strENG != null;
        String strLowerENG = "card_" + strENG.toLowerCase();

        //设置图片
        int drawableId = WordsStudyActivity.this.getResources().getIdentifier(strLowerENG,"drawable",WordsStudyActivity.this.getPackageName());
        Drawable drawable = ContextCompat.getDrawable(WordsStudyActivity.this,drawableId);
        card.setImageDrawable(drawable);

        loadVoice();
    }

    /**
     * 加载声音资源
     */
    private void loadVoice(){

        String strENG = arrayList.get(currentId).get("ENG");

        //停止
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            Log.d("mp3","stop");
        }
        //复位
        mediaPlayer.reset();
        Log.d("mp3","reset");
        //切换资源 播放
        int boyOrGirl = new Random().nextInt(2);
        String flag ;
        //随机播放男声女声
        if (boyOrGirl==0){
            flag = "Boy";
        }else {
            flag = "Girl";
        }
        //获取音频资源
        String mp3Resource = "Voice" + strENG +flag+ ".mp3";
        try {
            AssetFileDescriptor fd = getAssets().openFd(mp3Resource);
            mediaPlayer.setDataSource(fd.getFileDescriptor(),fd.getStartOffset(), fd.getLength());
            Log.d("mp3","setDataSource");
            mediaPlayer.prepare();
            Log.d("mp3","prepare");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }
}