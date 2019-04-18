package com.example.afinal.fingerPrint_Login.register.register_as_admin_setupProfile;

import android.widget.ImageView;

public class AdminDetail {

    private String textShow;
    private String imageViewPath;
    private boolean checkBox;

    public AdminDetail(String textShow, String imageViewPath) {
        this.textShow = textShow;
        this.imageViewPath = imageViewPath;
    }

    public AdminDetail(String textShow, String imageViewPath, boolean checkBox) {
        this.textShow = textShow;
        this.imageViewPath = imageViewPath;
        this.checkBox = checkBox;
    }

    public void setCheckBox(boolean checkBox) {
        this.checkBox = checkBox;
    }

    public boolean isCheckBox() {
        return checkBox;
    }

    public String getTextShow() {
        return textShow;
    }

    public String getImageViewPath() {
        return imageViewPath;
    }

    public void setTextShow(String textShow) {
        this.textShow = textShow;
    }

    public void setImageViewPath(String imageViewPath) {
        this.imageViewPath = imageViewPath;
    }
}
