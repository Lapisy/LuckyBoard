package com.hr.nipuream.luckpan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hr.nipuream.luckpan.bean.DialogEvent;
import com.hr.nipuream.luckpan.service.MusicPlayerService;
import com.hr.nipuream.luckpan.view.EnvironmentLayout;
import com.hr.nipuream.luckpan.view.LuckPanLayout;
import com.hr.nipuream.luckpan.view.RotatePan;
import com.hr.nipuream.luckpan.widget.CompanyInfoDialog;
import com.hr.nipuream.luckpan.widget.QuestionDialog;


public class MainActivity extends AppCompatActivity implements RotatePan.AnimationEndListener {

    private EnvironmentLayout layout;
    private RotatePan rotatePan;
    private LuckPanLayout luckPanLayout;
    private ImageView goBtn;
    private ImageView yunIv;
    private int LOTTERY_MUSIC = 1;
    private int QUESTION_SHOW_MUSIC = LOTTERY_MUSIC + 1;
    private int ANWSER_SHOW_MUSIC = QUESTION_SHOW_MUSIC;
    private ImageView ivCompanyInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //弹出技术支持的公司
        ivCompanyInfo = (ImageView) findViewById(R.id.iv_show_company_info);
        ivCompanyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompanyInfoDialog dialog = new CompanyInfoDialog(MainActivity.this);
                if (!MainActivity.this.isFinishing()) {
                    dialog.show();
                }
            }
        });

        luckPanLayout = (LuckPanLayout) findViewById(R.id.luckpan_layout);
        luckPanLayout.startLuckLight();
        rotatePan = (RotatePan) findViewById(R.id.rotatePan);
        rotatePan.setAnimationEndListener(this);
        goBtn = (ImageView) findViewById(R.id.go);

        //动态设置外部框框的宽高
        luckPanLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                int height = getWindow().getDecorView().getHeight();
                int width = getWindow().getDecorView().getWidth();

                int backHeight = 0;
                int MinValue = Math.min(width, height);
                MinValue -= Util.dip2px(MainActivity.this, 60) * 2;
                backHeight = MinValue / 2;

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) luckPanLayout.getLayoutParams();
                lp.width = MinValue;
                lp.height = MinValue;
                luckPanLayout.setLayoutParams(lp);

                lp = (RelativeLayout.LayoutParams) goBtn.getLayoutParams();
                lp.topMargin += backHeight;
                lp.topMargin -= (goBtn.getHeight() / 2);
                goBtn.setLayoutParams(lp);

                getWindow().getDecorView().requestLayout();
            }
        },200);
    }

    public void rotation(View view) {
        rotatePan.startRotate(-1);
        luckPanLayout.setDelayTime(100);
        goBtn.setEnabled(false);

        startPlayMusic(LOTTERY_MUSIC);
    }

    private void startPlayMusic(int type) {
        MusicPlayerService.getInstance(this).play(type);
    }

    @Override
    public void endAnimation(final int position) {
        //停止背景音乐
        stopBGMusic();

        goBtn.setEnabled(true);
        luckPanLayout.setDelayTime(500);
        rotatePan.postDelayed(new Runnable() {
            @Override
            public void run() {
                //开始问题出现的音乐
                startPlayMusic(QUESTION_SHOW_MUSIC);
                showQuestionDialog(position);
            }
        }, 200);
    }

    private void stopBGMusic() {
        MusicPlayerService.getInstance(this).stop();
    }

    private void showQuestionDialog(int position) {
        QuestionDialog mQuestionDialog = new QuestionDialog(this);
        mQuestionDialog.addCallBack(new QuestionDialog.CallBack() {
            @Override
            public void questionComplete() {
                //进入到答案之前，结束答案的背景音乐
                MusicPlayerService.getInstance(MainActivity.this).stop();
                //开始答案的背景音乐
                MusicPlayerService.getInstance(MainActivity.this).play(ANWSER_SHOW_MUSIC);
            }

            @Override
            public void anwserComplete() {
                //结束答案的背景音乐
                MusicPlayerService.getInstance(MainActivity.this).stop();
            }
        });
        if (mQuestionDialog.isShowing())
            mQuestionDialog.dismiss();

        //随机产生数字
        int number = getRandomNumber();
        if (!this.isFinishing()) {
            mQuestionDialog.show(number);
        }
    }


    public int getRandomNumber() {
        int randomNumber;
        randomNumber = (int) (Math.random() * 16);
        return randomNumber;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicPlayerService.getInstance(this).release();
        android.os.Process.killProcess(Process.myPid());
    }
}
