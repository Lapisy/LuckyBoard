package com.hr.nipuream.luckpan.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;

import com.hr.nipuream.luckpan.R;

/**
 * Created by Administrator on 2017-05-25.
 */

public class CompanyInfoDialog extends Dialog {
    private final Context mContext;

    public CompanyInfoDialog(@NonNull Context context) {
        this(context, R.style.add_dialog);
    }

    public CompanyInfoDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comany_info);
        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompanyInfoDialog.this.dismiss();
            }
        });
    }
}
