package com.example.myapplication.moment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.R;


public class CommentDialog extends Dialog {

    /**
     * 上下文对象 *
     */
    private Activity context;

    public ImageButton imgBtnSendComment;

    public EditText etCommentMessage;


    public View.OnClickListener clickListener;

    public CommentDialog(Activity context) {
        super(context);
        this.context = context;
    }


    public CommentDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.clickListener = clickListener;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.dialog_moment_comment_box);

        etCommentMessage = (EditText) findViewById(R.id.commentMessage);
        etCommentMessage.setFocusable(true);
        etCommentMessage.setFocusableInTouchMode(true);
        etCommentMessage.requestFocus();



        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth());

        dialogWindow.setAttributes(p);

        // 根据id在布局中找到控件对象
        imgBtnSendComment = (ImageButton) findViewById(R.id.sendComment);

        // 为按钮绑定点击事件监听器
        //sendComment.setOnClickListener(mClickListener);

        this.setCancelable(true);
        //context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}
