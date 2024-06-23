package android.ListenToMe.LanguageAssess;

import android.ListenToMe.LanguageAssess.Bean.Option;
import android.ListenToMe.LanguageAssess.Bean.Question;
import android.ListenToMe.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class ThreeButtonsFragment extends Fragment {

    /**
     * 三个按钮Fragment的监听器接口 (3F代表含有两个按钮的Fragment)
     */
    public interface OnButtonClickListener_3F {
        void onOption1Click_3F();
        void onOption2Click_3F();
        void onOption3Click_3F();
    }

    //当前界面的问题
    private final Question question;
    private final Option answer;

    Button option1,option2,option3;

    private OnButtonClickListener_3F listener;

    public ThreeButtonsFragment(Question question, Option answer) {
        this.question = question;
        this.answer = answer;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.access_fragment_three_button,container,false);

        //三个按钮的回调
        option1 = view.findViewById(R.id.option1_ThreeFragment);
        option1.setText(question.getOptions().get(0).getOptionStr());
        option1.setOnClickListener(v->{
            darkerOption1();
            if (listener != null)
                listener.onOption1Click_3F();
        });

        option2 = view.findViewById(R.id.option2_ThreeFragment);
        option2.setText(question.getOptions().get(1).getOptionStr());
        option2.setOnClickListener(v->{
            darkerOption2();
            if (listener != null)
                listener.onOption2Click_3F();
        });

        option3 = view.findViewById(R.id.option3_ThreeFragment);
        option3.setText(question.getOptions().get(2).getOptionStr());
        option3.setOnClickListener(v->{
            darkerOption3();
            if (listener != null)
                listener.onOption3Click_3F();
        });

        if (answer != null) {
            //已选择的答案等于第一个选项
            if (Objects.equals(answer.getOptionStr(), question.getOptions().get(0).getOptionStr())) {
                darkerOption1();
            }
            //已选择的答案等于第二个选项
            else if (Objects.equals(answer.getOptionStr(), question.getOptions().get(1).getOptionStr())) {
                darkerOption2();
            }
            //已选择的答案等于第三个选项
            else if (Objects.equals(answer.getOptionStr(), question.getOptions().get(2).getOptionStr())) {
                darkerOption3();
            }
        }


        return view;
    }

    public void setListener_3F(OnButtonClickListener_3F listener){
        this.listener = listener;
    }

    private void darkerOption1() {
        option1.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.button_darkpurple,null));
        option2.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.button_purple,null));
        option3.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.button_purple,null));
    }
    private void darkerOption2() {
        option1.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.button_purple,null));
        option2.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.button_darkpurple,null));
        option3.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.button_purple,null));
    }
    private void darkerOption3() {
        option1.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.button_purple,null));
        option2.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.button_purple,null));
        option3.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.button_darkpurple,null));
    }
}
