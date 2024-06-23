package android.ListenToMe.launch;

import android.ListenToMe.R;
import android.ListenToMe.Utils.Gender;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class RegisterFragment2 extends Fragment {

    private Calendar birthday;

    //接口
    public interface OnCLick_Register2 {

        void onShowFrag1_frag2();

        void onDoneClick_frag2();

    }

    private final OnCLick_Register2 onCLick_register2;
    private final Context context;

    //姓名
    private EditText name;

    //性别
    private ImageView boy;
    private ImageView girl;
    private Gender gender = Gender.None;

    //出生日期
    private Button mYearBtn, mMonthBtn, mDayBtn;
    //是否选择过出生日期
    private boolean chosenBirth=false;

    /**
     * 构造器
     * @param context           context
     * @param onCLick_register2 监听器
     */
    public RegisterFragment2(Context context, OnCLick_Register2 onCLick_register2) {
        this.context = context;
        this.onCLick_register2 = onCLick_register2;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment2, container, false);

        //姓名
        name = view.findViewById(R.id.name_register2);

        //性别
        boy = view.findViewById(R.id.iv_boy_register2);
        boy.setOnClickListener(v -> setGender(Gender.Boy));
        girl = view.findViewById(R.id.iv_girl_register2);
        girl.setOnClickListener(v -> setGender(Gender.Girl));

        //出生日期
        birthday = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                context
                , AlertDialog.THEME_HOLO_LIGHT
                , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //设置所选日期
                mYearBtn.setText(String.valueOf(year));
                mMonthBtn.setText(String.valueOf(month + 1));
                mDayBtn.setText(String.valueOf(dayOfMonth));

                //更新存储
                birthday.set(Calendar.YEAR,year);
                birthday.set(Calendar.MONTH,month);
                birthday.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                //更新flag
                chosenBirth = true;
            }
        }
                //初始化的年月日
                , birthday.get(Calendar.YEAR)
                , birthday.get(Calendar.MONTH)
                , birthday.get(Calendar.DAY_OF_MONTH));
        //不显示日历
        dialog.getDatePicker().setCalendarViewShown(false);
        //设置日期上限
        dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());

        //监听
        mYearBtn = view.findViewById(R.id.btn_year_register2);
        mYearBtn.setOnClickListener(v -> dialog.show());
        mMonthBtn = view.findViewById(R.id.btn_month_register2);
        mMonthBtn.setOnClickListener(v -> dialog.show());
        mDayBtn = view.findViewById(R.id.btn_day_register2);
        mDayBtn.setOnClickListener(v -> dialog.show());


        //上一个按钮
        Button pre = view.findViewById(R.id.btn_previous_Register_frag2);
        pre.setOnClickListener(v -> {
            if (onCLick_register2 != null)
                onCLick_register2.onShowFrag1_frag2();
        });

        //完成按钮
        Button done = view.findViewById(R.id.btn_finish_Register_frag2);
        done.setOnClickListener(v -> {
            if (onCLick_register2 != null)
                onCLick_register2.onDoneClick_frag2();
        });

        return view;
    }

    //获取姓名
    public String getName() {
        return name.getText().toString();
    }

    //选择性别
    public Gender getGender() {
        return gender;
    }

    private void setGender(Gender gender) {
        this.gender = gender;

        if (gender == Gender.Boy) {
            boy.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.register_button_purple, null));
            girl.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.register_button_white, null));
        } else {
            girl.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.register_button_purple, null));
            boy.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.register_button_white, null));

        }
    }

    //获取生日
    public long getBirth() {
        //没有选择过出生日期
        if (chosenBirth)
            return birthday.getTimeInMillis();
        else
            return 0;
    }
}
