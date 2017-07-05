package com.hr.nipuream.luckpan.data;

import android.app.Activity;
import android.content.Context;

import com.hr.nipuream.luckpan.R;
import com.hr.nipuream.luckpan.data.bean.Question;

/**
 * Created by Administrator on 2017-05-23.
 */

public class DataManger {
    private static DataManger mDataManger;
    private final Context mContext;
    private String[] questionArray;
    private String[] anwserArray;

    private DataManger(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static DataManger getInstance(Context context) {
        if (mDataManger == null) {
            synchronized (DataManger.class) {
                if (mDataManger == null)
                    mDataManger = new DataManger(context);
            }
        }
        return mDataManger;
    }

    public Question loadQuestion(int position) {
        Question question = new Question();
        if (questionArray == null) {
            questionArray = mContext.getResources().getStringArray(R.array.question_collection);
        }
        if (anwserArray == null) {
            anwserArray = mContext.getResources().getStringArray(R.array.anwser_collection);
        }
        if (position < 0 || position > questionArray.length)
            throw new RuntimeException("the position is wrong");

        question.setQuestionContent(questionArray[position]);
        question.setAnwser(anwserArray[position]);

        return question;
    }
}
