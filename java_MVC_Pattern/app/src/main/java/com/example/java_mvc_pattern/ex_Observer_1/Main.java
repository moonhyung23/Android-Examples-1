package com.example.java_mvc_pattern.ex_Observer_1;

public class Main {
    public static void main(String[] args) {
        Button button = new Button();

        button.setOnclickListener(new ButtonClick());
        button.onClick();

    /*    button.setOnclickListener(new Button.OnclickListener() {
            @Override
            public void onClick(Button button) {

            }
        });*/

    }


}

class ButtonClick implements Button.OnclickListener {

    @Override
    public void onClick(Button button) {
    }
}
