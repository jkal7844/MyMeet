package com.jk.framework.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created By Admin on 2020/3/29
 * Describe:
 */
public class DialogView extends Dialog {
    public DialogView(Context mContext, int layout, int style, int gravity) {
        super(mContext, style);

        setContentView(layout);
        Window window = this.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams);
//        ButterKnife.bind(this);


    }
}
