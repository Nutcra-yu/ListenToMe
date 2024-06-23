package android.ListenToMe.LanguageAssess;

import android.ListenToMe.LanguageAssess.Bean.Option;
import android.ListenToMe.LanguageAssess.Bean.Question;
import android.ListenToMe.LanguageAssess.Bean.QuestionPart;
import android.ListenToMe.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AccessActivity extends AppCompatActivity implements TwoButtonsFragment.OnButtonClickListener_2F, ThreeButtonsFragment.OnButtonClickListener_3F, MyAdapter.OnClick_rv {

    public static final String FILENAME = "FILENAME";

    //问题数组
    QuestionPart[] questionParts;
    //答案数组
    Option[][] answers;

    //记录当前界面的问题坐标
    int partNum = 0, orderNum = 0;

    //当前问题
    Question currentQuestion;
    //当前答案
    Option currentAnswer;

    MyAdapter myAdapter;

    TextView preQuestion, questionStr, number_tv;
    Button option1, option2;
    DrawerLayout drawer;

    /**
     * 调用此activity
     *
     * @param context  要跳转的界面context
     * @param fileName 量表所在的文件名
     * @return 返回量表界面的Intent
     */
    public static Intent newAccess(Context context, String fileName) {
        Intent intent = new Intent(context, AccessActivity.class);
        intent.putExtra(FILENAME, fileName);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.access_activity);

        //获取所有问题
        questionParts = getQuestions();

        //创建答案数组
        answers = new Option[questionParts.length][];
        for (int i = 0; i < questionParts.length; i++) {
            int num = questionParts[i].getQuestions().size();
            answers[i] = new Option[num];
            for (int j = 0; j < num; j++) {
                answers[i][j] = null;
            }
        }

        initWidget();

        setRes();

        //测试使用 用于填写完问卷
        Button allDone = findViewById(R.id.allDone);
        allDone.setOnClickListener(v -> {
            for (int i = 0; i < answers.length; i++) {
                for (int j = 0; j < answers[i].length; j++) {
                    answers[i][j] = questionParts[i].getQuestions().get(j).getOptions().get(0);
                }
            }
        });
    }

    /**
     * 获取所有的问题
     *
     * @return 返回问题数组
     */
    private QuestionPart[] getQuestions() {

        //从Intent中获取文件名字
        String fileName = getIntent().getStringExtra("FileName");

        StringBuilder stringBuilder = new StringBuilder();

        try {
            InputStream inputStream = getAssets().open(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String problemsString = stringBuilder.toString();

        //使用Gson转化JsonString
        Gson gson = new Gson();

        return gson.fromJson(problemsString, QuestionPart[].class);

    }

    /**
     * 组件以及监听器的绑定
     */
    private void initWidget() {

        //问题前提
        preQuestion = findViewById(R.id.preQuestion_AccessABCActivity2);

        //问题文字
        questionStr = findViewById(R.id.questionStr);

        //两个选项
        option1 = findViewById(R.id.option1_twoFragment);
        option2 = findViewById(R.id.option2_twoFragment);

        //上一道 下一道
        ImageButton left = findViewById(R.id.leftbutton);
        left.setOnClickListener(v -> {
            reduceOrder();
            setRes();
        });
        ImageButton right = findViewById(R.id.rightbutton);
        right.setOnClickListener(v -> {
            addOrder();
            setRes();
        });

        //问题序号
        number_tv = findViewById(R.id.number);

        //提交按钮
        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(v -> {
            boolean allDone = true;
            int i, j = 0;
            notDone:
            for (i = 0; i < answers.length; i++) {
                for (j = 0; j < answers[i].length; j++) {
                    if (answers[i][j] == null) {
                        allDone = false;
                        break notDone;
                    }
                }
            }
            if (allDone) {
                //全部完成 跳转结果分析
                Intent intent = AccessResultActivity.newResult(this,getIntent().getStringExtra(FILENAME),answers);
                startActivity(intent);
                finish();
            } else {
                //未完成
                Toast.makeText(this, "未完成", Toast.LENGTH_SHORT).show();
                partNum = i;
                orderNum = j;
                setRes();
            }
        });

        //返回按钮
        ImageView iv_back = findViewById(R.id.iv_back_AccessABCActivity2);
        iv_back.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(AccessActivity.this);

            builder.setMessage("退出将不会保存答案哦，确定退出嘛？")
                    .setPositiveButton("是的", (dialog, which) -> finish())
                    .setNegativeButton("取消", (dialog, which) -> dialog.cancel());

            AlertDialog dialog = builder.create();

            dialog.show();

        });

        //侧滑栏
        drawer = findViewById(R.id.main2);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                myAdapter.notifyDataSetChanged();
            }
        });

        //更多按钮
        ImageView more = findViewById(R.id.more);
        more.setOnClickListener(v -> drawer.openDrawer(GravityCompat.START));

        RecyclerView recyclerView = findViewById(R.id.rv_drawer_access);
        myAdapter = new MyAdapter(questionParts, this, answers, this);
        recyclerView.setAdapter(myAdapter);

        //继续答题
        Button btn_continue = findViewById(R.id.continue_ABC2);
        btn_continue.setOnClickListener(v -> drawer.close());

    }

    /**
     * 设置界面资源
     */
    private void setRes() {

        updateCurrentQuestion();
        updateCurrentAnswer();

        //问题前提
        preQuestion.setText(questionParts[partNum].getPreQuestion());

        //加载按钮的fragment
        loadButtonFragment();

        //问题
        questionStr.setText(currentQuestion.getQuestionStr());

        //题目序号
        String numStr = "第" + (partNum + 1) + "部分" + ":" + (orderNum + 1) + "/" + questionParts[partNum].getQuestions().size();
        number_tv.setText(numStr);
    }

    /**
     * 加载选项的Fragment
     */
    private void loadButtonFragment() {

        Fragment fragment;

        //选项fragment
        if (currentQuestion.getOptions().size() == 2) {
            fragment = new TwoButtonsFragment(currentQuestion, currentAnswer);
            ((TwoButtonsFragment) fragment).setListener_2F(this);
        } else {
            fragment = new ThreeButtonsFragment(currentQuestion, currentAnswer);
            ((ThreeButtonsFragment) fragment).setListener_3F(this);
        }

        // 获取 FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // 开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // 创建并添加 Fragment
        transaction.replace(R.id.choose_container, fragment);

        // 提交事务
        transaction.commit();
    }

    /**
     * 问题序号的增减
     */
    private void addOrder() {
        orderNum++;
        if (orderNum >= questionParts[partNum].getQuestions().size()) {
            orderNum = 0;
            addPartNum();
        }
    }

    private void reduceOrder() {
        orderNum--;
        if (orderNum < 0) {
            reducePartNum();
        }
    }

    private void reducePartNum() {
        partNum--;

        if (partNum < 0) {
            partNum = questionParts.length - 1;
            orderNum = questionParts[partNum].getQuestions().size() - 1;
        }
        orderNum = questionParts[partNum].getQuestions().size() - 1;

    }

    private void addPartNum() {
        partNum++;
        if (partNum >= questionParts.length) {
            partNum = 0;
        }
        orderNum = 0;

    }

    /**
     * 更新当前问题以及已选择的答案
     */
    private void updateCurrentQuestion() {
        //设置当前问题
        currentQuestion = questionParts[partNum].getQuestions().get(orderNum);
    }

    private void updateCurrentAnswer() {
        currentAnswer = answers[partNum][orderNum];
    }

    /**
     * 选项按钮监听的回调
     */
    @Override
    public void onOption1Click_2F() {
        addAnswer(0);
    }

    @Override
    public void onOption2Click_2F() {
        addAnswer(1);
    }

    @Override
    public void onOption1Click_3F() {
        addAnswer(0);
    }

    @Override
    public void onOption2Click_3F() {
        addAnswer(1);
    }

    @Override
    public void onOption3Click_3F() {
        addAnswer(2);
    }

    /**
     * 添加所选至答案数组
     */
    private void addAnswer(int optionIndex) {
        answers[partNum][orderNum] = currentQuestion.getOptions().get(optionIndex);

        //判断是否为ABC量表 是则添加dimension属性
        if ( getIntent().getStringExtra(FILENAME).equals("a_ABC.json")  )
            answers[partNum][orderNum].setDimension(currentQuestion.getDimension());

        addOrder();
        new Handler().postDelayed(this::setRes, 250);
    }

    /**
     * 抽屉视图里的按钮监听
     *
     * @param partN  传回参数 设置界面
     * @param orderN 传回参数 设置界面
     */
    @Override
    public void onItemClick_rv(int partN, int orderN) {
        this.partNum = partN;
        this.orderNum = orderN;
        setRes();
        drawer.closeDrawers();
    }
}

class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //两个按钮 partBtn norBtn
    public static final int VIEW_TYPE_PART_BTN = 0;
    public static final int VIEW_TYPE_NOR_BTN = 1;

    //监听器接口
    public interface OnClick_rv {
        void onItemClick_rv(int partN, int orderN);
    }

    //监听器
    private final OnClick_rv onClick_rv;

    //传入的问题
    private final QuestionPart[] questionParts;
    //传入的答案
    private final Option[][] answers;

    private final Context context;

    /**
     * 构造器
     */
    public MyAdapter(QuestionPart[] questionParts, OnClick_rv onClick_rv, Option[][] answers, Context context) {
        this.questionParts = questionParts;
        this.onClick_rv = onClick_rv;
        this.answers = answers;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(recyclerView.getContext(), 5);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int span = 1;
                if (getItemViewType(position) == VIEW_TYPE_PART_BTN) {
                    span = 5;
                }
                return span;
            }
        });

        recyclerView.setLayoutManager(layoutManager);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_NOR_BTN) {
            View norButtonView = inflater.inflate(R.layout.access_rv_item_normal, parent, false);
            return new NormalViewHolder(norButtonView);
        } else {
            View partButtonView = inflater.inflate(R.layout.access_rv_item_part, parent, false);
            return new PartViewHolder(partButtonView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            //普通按钮
            Button button = ((NormalViewHolder) holder).button;

            int rowIndex = -1;
            int colIndex = position;

            for (QuestionPart questionPart : questionParts) {
                rowIndex++;
                if (colIndex <= questionPart.getQuestions().size())
                    break;
                colIndex -= (questionPart.getQuestions().size() + 1);
            }

            colIndex -= 1;

            int partN = rowIndex, orderN = colIndex;
            button.setText(String.valueOf(questionParts[partN].getQuestions().get(orderN).getOrder()));
            button.setOnClickListener(v -> {
                if (onClick_rv != null)
                    onClick_rv.onItemClick_rv(partN, orderN);
                boolean answered = (answers[partN][orderN] != null);
            });
            button.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.button_gray, null));
            if (answers[partN][orderN] != null) {
                button.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.button_chosen, null));
            }

        } else if (holder instanceof PartViewHolder) {
            //part按钮
            Button button = ((PartViewHolder) holder).partButton;

            int rowIndex = -1;
            int colIndex = position;

            for (QuestionPart questionPart : questionParts) {
                rowIndex++;
                if (colIndex == 0)
                    break;
                colIndex -= (questionPart.getQuestions().size() + 1);
            }

            int partN = rowIndex;
            button.setText(questionParts[partN].getPartName());
            button.setOnClickListener(v -> {
                if (onClick_rv != null)
                    onClick_rv.onItemClick_rv(partN, 0);
            });
            boolean allDone = true;
            for (Option option : answers[rowIndex])
                if (option == null) {
                    allDone = false;
                    break;
                }
            button.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.button_gray, null));
            if (allDone)
                button.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.button_chosen, null));

        }

    }

    @Override
    public int getItemViewType(int position) {

        int flag = VIEW_TYPE_NOR_BTN;

        // 计算在展平后的一维 List 中的索引
//        int rowIndex = -1;
        int colIndex = position;

        // 找到对应元素的行和列索引
        for (QuestionPart questionPart : questionParts) {
//            rowIndex++;
            // part按钮
            if (colIndex == 0) {
                flag = VIEW_TYPE_PART_BTN;
                break;
            }
            if (position <= questionPart.getQuestions().size())
                break;
            //额外增加按钮 所以size+1
            colIndex -= (questionPart.getQuestions().size() + 1);
        }

        return flag;

        // 0 * * * * * * * * *
        // 0 * * * * *
        // 0 * * * * * *

        // 13 14 15 16 17 18 19 20 21 22        9
        // 7  8  9  10 11 12                    5
        // 0  1  2  3  4  5  6                  6

    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (QuestionPart questionPart : questionParts)
            count += questionPart.getQuestions().size();
        return count + questionParts.length;
    }

    public static class NormalViewHolder extends RecyclerView.ViewHolder {

        Button button;

        public NormalViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button_item);
        }
    }

    public static class PartViewHolder extends RecyclerView.ViewHolder {
        Button partButton;

        public PartViewHolder(@NonNull View itemView) {
            super(itemView);
            partButton = itemView.findViewById(R.id.part_button_item);
        }
    }
}

