package com.hr.nipuream.luckpan.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class MusicPlayerService {
    private static MusicPlayerService mMusicPlayerService;
    private MediaPlayer player;
    private String FILE_NAME;
    private Context mContext;

    private MusicPlayerService(Context context) {
        this.mContext = context;
        player = new MediaPlayer();
    }

    public static MusicPlayerService getInstance(Context context) {
        if (mMusicPlayerService == null) {
            synchronized (MusicPlayerService.class) {
                if (mMusicPlayerService == null)
                    mMusicPlayerService = new MusicPlayerService(context);
            }
        }
        return mMusicPlayerService;
    }

    public void play(int type) {
        FILE_NAME = getFileName(type);
        try {
            player.reset();//把各项参数恢复到初始状态
            AssetFileDescriptor afd = mContext.getAssets().openFd(FILE_NAME);
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();  //进行缓冲
            player.setOnPreparedListener(new PreparedListener(type));//注册一个监听器
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFileName(int type) {
        String fileName = null;
        switch (type) {
            case 1:
                fileName = "lottery_bg.mp3";
                break;
            case 2:
                fileName = "question_show_bg.mp3";
                break;
            case 3:
                fileName = "question_show_bg.mp3";
                break;
        }
        return fileName;
    }

    public void stop() {
        if (player != null) {
            player.stop();
        }
    }

    public void release() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    private class PreparedListener implements MediaPlayer.OnPreparedListener {
        public PreparedListener(int type) {
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
        }
    }
}
