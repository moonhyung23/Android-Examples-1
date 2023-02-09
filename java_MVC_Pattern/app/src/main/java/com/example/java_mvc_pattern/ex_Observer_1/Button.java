package com.example.java_mvc_pattern.ex_Observer_1;

public class Button {
    private OnclickListener onclickListener;


    public void setOnclickListener(OnclickListener onclickListener) {
        this.onclickListener = onclickListener;
    }

    public void onClick() {
        if (onclickListener != null) {
            onclickListener.onClick(this);
        }
    }



    public interface OnclickListener {
        public void onClick(Button button);
    }


}
