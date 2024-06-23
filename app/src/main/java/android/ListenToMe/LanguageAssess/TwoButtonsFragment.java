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

public class TwoButtonsFragment extends Fragment {

    private final Question question;
    private final Option answer;

    Button option1, option2;

    /**
     * 两个按钮Fragment的监听器接口 (2F代表含有两个按钮的Fragment)
     */
    public interface OnButtonClickListener_2F {
        void onOption1Click_2F();

        void onOption2Click_2F();
    }

    private OnButtonClickListener_2F listener;

    public TwoButtonsFragment(Question question, Option answer) {
        this.question = question;
        this.answer = answer;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.access_fragment_two_button, container, false);

        option1 = view.findViewById(R.id.option1_twoFragment);
        option1.setText(question.getOptions().get(0).getOptionStr());
        option1.setOnClickListener(v -> {
            darkerOption1();
            if (listener != null) {
                listener.onOption1Click_2F();
            }
        });

        option2 = view.findViewById(R.id.option2_twoFragment);
        option2.setText(question.getOptions().get(1).getOptionStr());
        option2.setOnClickListener(v -> {
            darkerOption2();
            if (listener != null) {
                listener.onOption2Click_2F();
            }
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
        }

        return view;
    }

    public void setListener_2F(OnButtonClickListener_2F listener) {
        this.listener = listener;
    }

    private void darkerOption1() {
        option1.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_darkpurple, null));
        option2.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_purple, null));
    }

    private void darkerOption2() {
        option1.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_purple, null));
        option2.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_darkpurple, null));
    }

}