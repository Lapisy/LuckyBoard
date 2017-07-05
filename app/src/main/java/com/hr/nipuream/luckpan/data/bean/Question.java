package com.hr.nipuream.luckpan.data.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-05-23.
 */

public class Question  implements Serializable{
    private String questionContent;
    private String anwser;

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getAnwser() {
        return anwser;
    }

    public void setAnwser(String anwser) {
        this.anwser = anwser;
    }
}
