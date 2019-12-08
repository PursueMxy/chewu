package com.weimu.universalib.utils;


import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.weimu.universalib.OriginAppData;


public class EditTextUtils {


    //获取编辑框的内容
    public static String getContent(EditText editText) {
        if (editText != null) {
            return editText.getText().toString().trim();
        }
        return null;
    }

    //删除编辑框的内容
    public static void clearContent(EditText... editTexts) {
        for (EditText ed : editTexts) {
            ed.setText("");
        }
    }

    //查看是否有空的值
    public static boolean isEmptys(EditText... editTexts) {
        for (EditText ed : editTexts) {
            String str = ed.getText().toString().trim();
            if (TextUtils.isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    //展示密码 InputTypes
    public static void showPassword(EditText editText) {
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        setSeletionEnd(editText);
    }

    //隐藏密码  InputType
    public static void hidePassword(EditText editText) {
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        setSeletionEnd(editText);
    }

    //是否要显示密码
    public static void changePasswordStatus(EditText editText, boolean isShowPwd) {
        if (isShowPwd)
            showPassword(editText);
        else
            hidePassword(editText);
    }

    //使光标指向末尾处
    public static void setSeletionEnd(EditText editText) {
        editText.setSelection(editText.getText().length());
    }

    public static void setEditMaxLength(final EditText editText, final int maxIndex) {
        editText.addTextChangedListener(new MyTextWathcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (str.length() > maxIndex) {
                    editText.setText(str.substring(0, str.length() - 1));
                }
            }
        });
    }

    public abstract static class MyTextWathcher implements TextWatcher {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


    //隐藏键盘
    public static void hideKeyBorad(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) OriginAppData.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive(editText)) {
            editText.clearFocus();
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    //消除编辑框的所有状态，内容
    public static void clearAllStatus(EditText... editTexts) {
        for (EditText ed : editTexts) {
            clearContent(ed);
            hideKeyBorad(ed);
            setSeletionEnd(ed);
        }

    }

}
