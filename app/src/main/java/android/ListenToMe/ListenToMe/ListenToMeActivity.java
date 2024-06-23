package android.ListenToMe.ListenToMe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.ListenToMe.ListenToMe.Adapter.ContentSentenceAdapter;
import android.ListenToMe.ListenToMe.Adapter.MenuAdapter;
import android.ListenToMe.R;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListenToMeActivity extends AppCompatActivity {

    private RecyclerView menuRV,contentRV,sentenceRV;

    private MenuAdapter menuAdapter;
    private ContentSentenceAdapter contentSentenceAdapter;
    private ContentSentenceAdapter sentenceAdapter;


    private List<CardBean> menus,sentences;
    private int menusIndex;
    private List<List<CardBean>> contents;

    private MediaPlayer mediaPlayer;
    private List<AssetFileDescriptor> fds;
    private int playIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_to_me);

        binding();

        setMenus();
        menusRVInitializer();

        setContents();
        contentInitializer();

        sentenceInitializer();

    }

    /**
     * 绑定及初始化
     */
    private void binding(){

        menuRV = findViewById(R.id.rv_menu_ListenToMeActivity);
        contentRV = findViewById(R.id.rv_content_ListenToMeActivity);
        sentenceRV = findViewById(R.id.rv_sentence_ListenToMeActivity);

        menus = new ArrayList<>();
        contents = new ArrayList<>();
        sentences = new ArrayList<>();

        playIndex = 0;
        fds = new ArrayList<>();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("play index", "onCompletion"+playIndex);
                playIndex ++;

                if (playIndex < fds.size()){
                    playAudio(fds.get(playIndex));
                }else {
                    menusIndex = 0;
                    mediaPlayer.stop();
                }

            }
            private void playAudio(AssetFileDescriptor a){
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(a.getFileDescriptor(),a.getStartOffset(),a.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //播放
        ImageView iv_play = findViewById(R.id.iv_play_ListenToMeActivity);
        iv_play.setOnClickListener(v -> readSentence());

        //返回
        ImageView iv_back = findViewById(R.id.iv_back_ListenToMeActivity);
        iv_back.setOnClickListener(v -> finish());

        //删除
        ImageView iv_delete = findViewById(R.id.iv_delete_ListenToMeActivity);
        iv_delete.setOnClickListener(v -> {
            if (sentences.size() != 0){
                sentences.remove(sentences.size()-1);
                sentenceAdapter.updateList(sentences);
            }
        });
    }

    /**
     * 菜单设置
     */
    private void setMenus(){
        menus.add(new CardBean("Person","人物"));
        menus.add(new CardBean("Verb","行为"));
        menus.add(new CardBean("Food","食物"));
        menus.add(new CardBean("Clothes","衣服"));
    }
    private void menusRVInitializer(){

        menuAdapter = new MenuAdapter(menus,ListenToMeActivity.this);
        menuRV.setAdapter(menuAdapter);
        menuAdapter.setOnItemClickListener(new MenuAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                menusIndex = position;
                contentSentenceAdapter.updateList(contents.get(position));
            }
        });

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        menuRV.setLayoutManager(manager);
    }

    /**
     * 内容设置
     */
    private void setContents(){
        for (CardBean bean : menus) {

            //循环开始时创建一个一维数组
            List<CardBean> contentList = new ArrayList<>();

            //获取文件名
            String engStrFile = bean.getEngStr();
            String nameFile = "TableWords" + engStrFile + ".json";

            try {
                /*读取json文件为jsonArray*/

                //读取文件流
                InputStream inputStream = getAssets().open(nameFile);
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                //转为string
                String jsonString = new String(buffer);
                //转为jsonArray
                JSONArray jsonArray =  new JSONArray(jsonString);

                /*为一维数组添加内容*/
                for (int i = 0; i < jsonArray.length(); i++) {
                    String eng = jsonArray.getJSONObject(i).getString("ENG");
                    String name = jsonArray.getJSONObject(i).getString("name");
                    CardBean cardBean = new CardBean(eng, name);
                    contentList.add(cardBean);
                }
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }

            //将一维数组加到二维数组中
            contents.add(contentList);
        }
    }
    private void contentInitializer(){

        contentSentenceAdapter = new ContentSentenceAdapter(contents.get(0),ListenToMeActivity.this,1);

        contentSentenceAdapter.setParamsAndClick(new ContentSentenceAdapter.setItemParamsAndClick() {
            @Override
            public void onItemClick(View v, int position) {
                CardBean bean = contents.get(menusIndex).get(position);
                sentences.add(bean);
                sentenceAdapter.updateList(sentences);
            }

        });
        contentRV.setAdapter(contentSentenceAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(ListenToMeActivity.this,2,LinearLayoutManager.HORIZONTAL,false);
        contentRV.setLayoutManager(gridLayoutManager);
    }

    /**
     * 句子设置
     */
    private void sentenceInitializer(){

        sentenceAdapter = new ContentSentenceAdapter(sentences,ListenToMeActivity.this,0);

        sentenceAdapter.setParamsAndClick(new ContentSentenceAdapter.setItemParamsAndClick() {
            @Override
            public void onItemClick(View v, int position) {

            }

        });
        sentenceRV.setAdapter(sentenceAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ListenToMeActivity.this,LinearLayoutManager.HORIZONTAL,false);
        sentenceRV.setLayoutManager(layoutManager);

    }

    /**
     * 播放语音
     */
    private void readSentence(){

        fds.clear();

        int boyOrGirl = new Random().nextInt(2);
        String flag = "Boy";
        if (boyOrGirl == 0){
            flag = "Girl";
        }
        for (CardBean bean :
                sentences) {
            String EngStr = bean.getEngStr();
            String FileName = "Voice" + EngStr + flag + ".mp3";

            try {
                fds.add(getAssets().openFd(FileName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        playIndex = 0;
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(fds.get(0).getFileDescriptor(),fds.get(0).getStartOffset(),fds.get(0).getLength());
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
