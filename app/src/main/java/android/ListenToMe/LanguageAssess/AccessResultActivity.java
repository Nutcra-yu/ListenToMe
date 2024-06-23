package android.ListenToMe.LanguageAssess;

import android.ListenToMe.LanguageAssess.Bean.Option;
import android.ListenToMe.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AccessResultActivity extends AppCompatActivity {

    public static final String ACCESS_NAME = "ACCESS_NAME";
    public static final String ANSWERS = "ANSWERS";

    /**
     * 启动此activity
     */
    public static Intent newResult(Context context, String accessName,Option[][] answers){
        Intent intent = new Intent(context,AccessResultActivity.class);
        intent.putExtra(ACCESS_NAME,accessName);
        intent.putExtra(ANSWERS,answers);
        return intent;
    }

    /**
     * 答案数组
     */
    Option[][] answers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abc_result_list_old);

        //感觉(Sensory)、交往(Relating)、躯体和物体使用 (Body_and_object_use)、语言(Language)、社会生活自理(Social_and_self_help)

        //子量表得分的高低通常反映了个体在相应行为领域的表现程度。具体来说：
        //得分较低： 表明个体在相应行为领域的表现相对正常，没有明显的问题或症状。
        //得分较高： 表明个体在相应行为领域可能存在问题或症状。高得分并不直接指示自闭症，而是可能存在多种发育障碍、行为障碍或其他相关问题。
        int Sensory,Relating,Body_and_object_use,Language,Social_and_self_help;
    }

}
