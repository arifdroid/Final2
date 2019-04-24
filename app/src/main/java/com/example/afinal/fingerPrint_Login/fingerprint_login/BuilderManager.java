package com.example.afinal.fingerPrint_Login.fingerprint_login;

import android.graphics.Rect;

import com.example.afinal.R;
import com.nightonke.boommenu.BoomButtons.BoomButtonBuilder;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;

public class BuilderManager {


    private static int[] imageResources = new int[]{
            R.drawable.bat,
            R.drawable.bear,
            R.drawable.bee,
            R.drawable.butterfly,
            R.drawable.cat,
            R.drawable.deer,
            R.drawable.dolphin,
            R.drawable.eagle,
            R.drawable.horse,
            R.drawable.elephant,
            R.drawable.owl,
            R.drawable.peacock,
            R.drawable.pig,
            R.drawable.rat,
            R.drawable.snake,
            R.drawable.squirrel
    };

    private static int imageResourceIndex = 0;
    private static int h;

    static int getImageResource() {
        if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
        return imageResources[imageResourceIndex++];
    }


    static TextOutsideCircleButton.Builder getTextOutsideCircleButtonBuilder() {
        return new TextOutsideCircleButton.Builder()
                .normalImageRes(getImageResource())

                //test need to be personalized for each button
                .normalTextRes(R.string.text_outside_circle_button_text_normal);
    }

    public static BoomButtonBuilder getTextOutsideCircleButtonBuilder(int j) {

        if(j==0){ //register as admin

             h = R.string.button_zero;
        }
        if(j==1){ //register as user

            h = R.string.button_one;
        }
        if(j==2){ //MC

            h = R.string.button_two;
        }
        if(j==3){ //log in to admin 2

            h = R.string.button_three;
        }
        if(j==4){ //log in to admin 1

            h = R.string.button_four;
        }

        return new TextOutsideCircleButton.Builder()
                .rotateText(true)
                .imagePadding(new Rect(0,0,70,50))
                .normalImageRes(getImageResource())
                .buttonRadius(100)
                //test need to be personalized for each button
                .normalTextRes(h);



    }
}
