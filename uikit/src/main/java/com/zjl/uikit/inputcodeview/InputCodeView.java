package com.zjl.uikit.inputcodeview;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.zjl.uikit.R;

/**
 * Created by zjl on 2018/7/12.
 */
public class InputCodeView extends ConstraintLayout implements TextWatcher, View.OnFocusChangeListener, View.OnKeyListener {

    private OnCodeFinishListener onCodeFinishListener;
    private long endTime = 0;

    /**
     * 输入框数量
     */
    private final int mEtNumber;


    public InputCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_input_code_view, this);

        int count = getChildCount();
        mEtNumber = count;

        EditText editText;
        for (int i = 0; i < count; i++) {
            editText = (EditText) getChildAt(i);

            editText.addTextChangedListener(this);
            editText.setOnFocusChangeListener(this);
            editText.setOnKeyListener(this);
        }
    }


    public void setOnCodeFinishListener(OnCodeFinishListener onCodeFinishListener) {
        this.onCodeFinishListener = onCodeFinishListener;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() != 0) {
            focus();

            //如果最后一个输入框有字符，则返回结果
            EditText lastEditText = (EditText) getChildAt(mEtNumber - 1);
            if (lastEditText.getText().length() > 0) {
                getResult();
            }
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            backFocus();
            return true;
        }
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setEnabled(enabled);
        }
    }

    /**
     * 获取焦点
     */
    private void focus() {
        int count = getChildCount();
        EditText editText;
        //利用for循环找出还最前面那个还没被输入字符的EditText，并把焦点移交给它。
        for (int i = 0; i < count; i++) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() < 1) {
                editText.setCursorVisible(true);
                editText.requestFocus();
                return;
            } else {
                editText.setCursorVisible(false);
            }
        }
    }

    private void backFocus() {
        //博主手机不好，经常点一次却触发两次`onKey`事件，就设置了一个防止多点击，间隔100毫秒。
        long startTime = System.currentTimeMillis();
        EditText editText;
        //循环检测有字符的`editText`，把其置空，并获取焦点。
        for (int i = mEtNumber - 1; i >= 0; i--) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() >= 1 && startTime - endTime > 100) {
                editText.setText("");
                editText.setCursorVisible(true);
                editText.requestFocus();
                endTime = startTime;
                return;
            }
        }
    }

    private void getResult() {
        StringBuffer stringBuffer = new StringBuffer();
        EditText editText;
        for (int i = 0; i < mEtNumber; i++) {
            editText = (EditText) getChildAt(i);
            stringBuffer.append(editText.getText());
        }
        if (onCodeFinishListener != null) {
            onCodeFinishListener.onComplete(stringBuffer.toString());
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            focus();
        }
    }

    public interface OnCodeFinishListener {
        void onComplete(String content);
    }
}
