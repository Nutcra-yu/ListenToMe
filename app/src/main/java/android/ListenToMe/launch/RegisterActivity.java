package android.ListenToMe.launch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.ListenToMe.MySPManager;
import android.ListenToMe.Utils.Gender;
import android.content.Context;
import android.os.Bundle;
import android.ListenToMe.R;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements RegisterFragment1.OnClick_Register, RegisterFragment2.OnCLick_Register2 {

    private RegisterFragment1 fragment1;
    private RegisterFragment2 fragment2;
    FragmentManager fragmentManager;

    private MySPManager mySPManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        //获取SP
        mySPManager = MySPManager.getSPInstance();

        //获取对Fragment管理器
        fragmentManager = getSupportFragmentManager();
        fragment1 = new RegisterFragment1(this, this);
        fragment2 = new RegisterFragment2(this, this);

        fragmentManager.beginTransaction().replace(R.id.register_container, fragment1).commit();

    }

    /**
     * 第一个Fragment的next按钮
     */
    @Override
    public void onShowFrag2_frag1() {

        //判断手机号
        if (fragment1.getPhone().length() != 11)
            checkToast("手机号输入错误");

            //判断是否填写密码
        else if (Objects.equals(fragment1.getPass1(), ""))
            checkToast("请输入密码");

            //判断两次密码是否一致
        else if (!fragment1.getPass1().equals(fragment1.getPass2()))
            checkToast("两次密码输入不一致");

            //输入无误则切换frag
        else {
            //获取事务
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //隐藏frag1
            fragmentTransaction.hide(fragment1);
            //展示frag2
            if (fragment2.isAdded())
                fragmentTransaction.show(fragment2);
            else
                fragmentTransaction.add(R.id.register_container, fragment2);

            fragmentTransaction.commit();
        }
    }


    /**
     * 第二个Fragment的Pre按钮
     */
    @Override
    public void onShowFrag1_frag2() {
        //获取事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //隐藏frag2
        fragmentTransaction.hide(fragment2);
        //实现frag1
        fragmentTransaction.show(fragment1);

        fragmentTransaction.commit();
    }


    /**
     * 第二个Fragment的完成按钮
     */
    @Override
    public void onDoneClick_frag2() {

        //存储phone
        mySPManager.saveString(this, MySPManager.INFO_PHONE, fragment1.getPhone());

        //存储密码
        mySPManager.saveString(this, MySPManager.INFO_PASSWORD, fragment1.getPass1());

        //存储姓名
        if (!Objects.equals(fragment2.getName(), ""))
            mySPManager.saveString(this, MySPManager.INFO_NAME, fragment2.getName());
        else
            checkToast("请输入宝宝名字");

        //存储性别
        if (fragment2.getGender() != Gender.None)
            mySPManager.saveInt(this, MySPManager.INFO_GENDER, Gender.genderToInt(fragment2.getGender()));
        else
            checkToast("请选择宝宝性别");

        //存储生日
        if (fragment2.getBirth() != 0)
            mySPManager.saveLong(this, MySPManager.INFO_BIRTH, fragment2.getBirth());
        else
            checkToast("请选择宝宝生日");

        System.out.println(mySPManager.getString(this, MySPManager.INFO_PHONE));
        System.out.println(mySPManager.getString(this, MySPManager.INFO_PASSWORD));
        System.out.println(mySPManager.getString(this, MySPManager.INFO_NAME));

        System.out.println(Gender.intToString(mySPManager.getInt(this, MySPManager.INFO_GENDER)));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mySPManager.getLong(this, MySPManager.INFO_BIRTH));
        System.out.println("年月日:" +
                calendar.get(Calendar.YEAR) + " "
                + (calendar.get(Calendar.MONTH) + 1) + " "
                + calendar.get(Calendar.DAY_OF_MONTH));

        finish();

    }

    /**
     * 显示Toast
     *
     * @param toastStr Toast显示的消息
     */
    private void checkToast(String toastStr) {
        Toast.makeText(this, toastStr, Toast.LENGTH_SHORT).show();
    }
}