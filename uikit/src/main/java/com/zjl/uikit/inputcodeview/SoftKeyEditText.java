package com.zjl.uikit.inputcodeview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

/**
 * 解决问题：华为P9手机，EditText内容为空的时候，收不到退格键的KeyEvent
 * Created by zjl on 2018/8/15.
 */
public class SoftKeyEditText extends android.support.v7.widget.AppCompatEditText {
    public SoftKeyEditText(Context context) {
        super(context);
    }

    public SoftKeyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SoftKeyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new MockDelKeyInputConnection(super.onCreateInputConnection(outAttrs),
                true);
    }

    private class MockDelKeyInputConnection extends InputConnectionWrapper {
        public MockDelKeyInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {// 往前删一个字符
                boolean sendDown = sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_DEL));
                boolean sendUp = sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,
                        KeyEvent.KEYCODE_DEL));
                return true;
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }


}
