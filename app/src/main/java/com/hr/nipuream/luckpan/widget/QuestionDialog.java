package com.hr.nipuream.luckpan.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.hr.nipuream.luckpan.R;
import com.hr.nipuream.luckpan.bean.DialogEvent;
import com.hr.nipuream.luckpan.data.DataManger;
import com.hr.nipuream.luckpan.data.bean.Question;


/**
 * Created by Administrator on 2017-05-23.
 */
public class QuestionDialog extends Dialog implements View.OnClickListener {
    private final Context mContext;
    private ViewFlipper vfQuestion;
    private TextView tvQuestionContent;
    private TextView tvQuestionAnwser;
    private Button btnCheckAnwser;
    private Button btnClose;
    private int mCurrentChosePostion;
    private DataManger mDataManger;
    private Question currentQuestion;
    private CallBack mCallBack;

    public QuestionDialog(@NonNull Context context) {
        this(context, R.style.add_dialog);
    }

    public QuestionDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_dialog);
        initView();
        this.setCancelable(false);
    }

    private void initView() {
        vfQuestion = (ViewFlipper) findViewById(R.id.vf_question);
        tvQuestionContent = (TextView) findViewById(R.id.tv_question_content);
        btnCheckAnwser = (Button) findViewById(R.id.btn_check_anwser);
        tvQuestionAnwser = (TextView) findViewById(R.id.tv_question_answer);
        btnClose = (Button) findViewById(R.id.btn_close);

        btnCheckAnwser.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_check_anwser:
                checkAnswer();
                break;
            case R.id.btn_close:
                close();
                break;
        }
    }

    private void close() {
        if (mCallBack != null) {
            mCallBack.anwserComplete();
        }

        this.dismiss();
        currentQuestion = null;
    }

    private void checkAnswer() {
        if (null != mCallBack) {
            mCallBack.questionComplete();
        }

        if (null != tvQuestionAnwser) {
            tvQuestionAnwser.setText(currentQuestion.getAnwser());
        }
        vfQuestion.showNext();
    }

    public void show(int positon) {
        super.show();
        mCurrentChosePostion = positon;
        laodQuestion(positon);

    }


    private void laodQuestion(int positon) {
        currentQuestion = DataManger.getInstance(mContext).loadQuestion(positon);
        tvQuestionContent.setText(currentQuestion.getQuestionContent());
    }

    public interface CallBack {
        void questionComplete();

        void anwserComplete();
    }

    public void addCallBack(CallBack callBack) {
        this.mCallBack = callBack;
    }


}
