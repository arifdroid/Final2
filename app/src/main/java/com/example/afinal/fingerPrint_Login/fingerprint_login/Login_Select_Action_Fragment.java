package com.example.afinal.fingerPrint_Login.fingerprint_login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.R;
import com.example.afinal.fingerPrint_Login.register.register_as_admin.register_as_admin_regAdmin.RegAdmin_AsAdmin_Activity;
import com.example.afinal.fingerPrint_Login.register.register_with_activity.RegAdmin_Activity;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.nightonke.boommenu.Util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

public class Login_Select_Action_Fragment extends Fragment{

    private Context mContext;

    private FloatingActionButton floatButton_Admin_1,floatButton_Admin_2, floatButton_Reg_Admin, floatButton_Reg_User,
                                floatButton_Note_MC, floatButton_Back;

    private TextView textViewAdmin_1, textViewAdmin_2, textView_RegUser, textView_RegAdmin, textView_Note;

    private FingerPrintFinal_Presenter presenter;
    private String nameHere;
    private String phoneHere;
    private String adminName;
    private String adminPhone;
    private String nameHere_2;
    private String phoneHere_2;
    private String adminName_2;
    private String adminPhone_2;


    //boom menu test

    private BoomMenuButton boomMenuButton;
    private Timer timer;
    private int countHere;
    private boolean registerUserBoolean;
    private boolean registerAdminBoolean;
    private boolean popAdmin_1;
    private boolean popAdmin_2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //        return super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.floatingbutton_fragment_select, container,false);
        mContext = container.getContext();

        countHere =0;
        boomMenuButton = rootView.findViewById(R.id.boomMenuiD);
        assert  boomMenuButton!=null;
        boomMenuButton.setButtonEnum(ButtonEnum.TextOutsideCircle);
        boomMenuButton.setPiecePlaceEnum(PiecePlaceEnum.DOT_5_3);
        boomMenuButton.setButtonPlaceEnum(ButtonPlaceEnum.SC_5_3);

        //test boom menu

        registerUserBoolean =false;
        registerAdminBoolean = false;

        popAdmin_1 =false;
        popAdmin_2 = false;
//
//
//
//        floatButton_Admin_1 = rootView.findViewById(R.id.select_fragment_FloatButton_admin1id);
//        floatButton_Admin_2 = rootView.findViewById(R.id.select_fragment_FloatButton_admin2);
//        floatButton_Reg_User = rootView.findViewById(R.id.select_fragment_FloatButton_RegisterUseriD);
//        floatButton_Reg_Admin = rootView.findViewById(R.id.select_fragment_FloatButton_RegisterAdminiD);
//        floatButton_Note_MC = rootView.findViewById(R.id.select_fragment_FloatButton_addNoteMCiD);
//        floatButton_Back = rootView.findViewById(R.id.select_fragment_FloatButton_backiD);
//
//        textView_Note = rootView.findViewById(R.id.select_fragment_textView_addNote_1id);
//        textViewAdmin_1 = rootView.findViewById(R.id.select_fragment_textView_admin1id);
//        textViewAdmin_2 = rootView.findViewById(R.id.select_fragment_textView_admin2id);
//        textView_RegUser = rootView.findViewById(R.id.select_fragment_textView_regUser_1id);
//        textView_RegAdmin = rootView.findViewById(R.id.select_fragment_textView_regAdmin_1id);

        //we pull from shared preferences here once
        //then set text, this is the part, how we know to which admin do we pull from,
//
//        SharedPreferences prefs = getActivity().getSharedPreferences("com.example.finalV8_punchCard", Context.MODE_PRIVATE);
//
//        //SharedPreferences prefs = (SharedPreferences) getActivity().getPreferences("com.example.finalV8_punchCard", Context.MODE_PRIVATE);
//
//        nameHere = prefs.getString("final_User_Name","");
//        phoneHere = prefs.getString("final_User_Phone","");
//        adminName = prefs.getString("final_Admin_Name","");
//        adminPhone = prefs.getString("final_Admin_Phone","");
//
//        Log.i("finalSharePreDataCheck","Login_Select_Fragment 3,name: "+ nameHere+ ", phone: "+phoneHere + ", adminName:"
//                +adminName+" , adminPhone: "+adminPhone);

        //REAL PULL HERE., we dont add anything, we just check

        //check if exist first,

        File f = new File("/data/data/com.example.afinal/shared_prefs/com.example.finalV8_punchCard.MAIN_POOL.xml");

        if(f.exists()){

            SharedPreferences prefs_Main_Pool = getActivity().getSharedPreferences("com.example.finalV8_punchCard.MAIN_POOL", Context.MODE_PRIVATE);
            ///if this not exist, means user need to register to an admin first.

            //here should contain

            //can use string set, but we use simple counter, translate to string instead.

            String countAdmin = prefs_Main_Pool.getString("count_admin","");
            if(countAdmin!=null || !countAdmin.equals("")) {

                if (Integer.valueOf(countAdmin) == 1) {  //this means only 1 admin exist.

                    //hence pull the first value, ,, the admin phone, since we need the admin phone number to retrieve shared prefs

                    String sharedPrefsCheck = prefs_Main_Pool.getString("final_Admin_Phone_MainPool",""); //this relevant if 1 admin only.

                    //after pull admin phone, pull dedicated sharedpreferences to this admin, check exist? if not exist,

                  //  String sharedPrefsCheck = adminPhoneHere;

                    if(!sharedPrefsCheck.equals("")) { //

                        File fileHere = new File("/data/data/com.example.afinal/shared_prefs/" + sharedPrefsCheck + ".xml");

                        if (fileHere.exists()) {

                            SharedPreferences sharedPrefs_1 = getActivity().getSharedPreferences("com.example.finalV8_punchCard."+sharedPrefsCheck, Context.MODE_PRIVATE);
                            //will be read sharedprefs of "com.example.finalV8_punchCard.+60184670568"

                            nameHere = sharedPrefs_1.getString("final_User_Name","");
                            phoneHere = sharedPrefs_1.getString("final_User_Phone","");
                            adminName = sharedPrefs_1.getString("final_Admin_Name","");
                            adminPhone = sharedPrefs_1.getString("final_Admin_Phone","");



                        } else { //file not exist.

                            Toast.makeText(getContext(),"issue: please contact admin.", Toast.LENGTH_LONG).show();


                        }

                    }else{ //somehow pulling data from pull dont contain admin phone

                        Toast.makeText(getContext(),"issue: please contact admin.", Toast.LENGTH_LONG).show();

                    }

                }if(Integer.valueOf(countAdmin) == 2){ //if admin is admin 2nd time registered.

                    //so when we pull here, need special way to pull two shared prefs data.

                    String sharedPrefsCheck_Admin_1 = prefs_Main_Pool.getString("final_Admin_Phone_MainPool",""); //this relevant if 1 admin only.
                    String sharedPrefsCheck_Admin_2 = prefs_Main_Pool.getString("final_Admin_Phone_MainPool_2",""); //this relevant if 1 admin only.

                    //handle admin 1 first.

                    if(!sharedPrefsCheck_Admin_1.equals("")){

                        File fileHere = new File("/data/data/com.example.afinal/shared_prefs/" + sharedPrefsCheck_Admin_1 + ".xml");

                        if(fileHere.exists()){


                            SharedPreferences sharedPrefs_1 = getActivity().getSharedPreferences("com.example.finalV8_punchCard."+sharedPrefsCheck_Admin_1, Context.MODE_PRIVATE);
                            //will be read sharedprefs of "com.example.finalV8_punchCard.+60184670568"

                            nameHere = sharedPrefs_1.getString("final_User_Name","");
                            phoneHere = sharedPrefs_1.getString("final_User_Phone","");
                            adminName = sharedPrefs_1.getString("final_Admin_Name","");
                            adminPhone = sharedPrefs_1.getString("final_Admin_Phone","");

                        }else { //something wrong if not exist.

                            Toast.makeText(getContext(),"issue: please contact admin.", Toast.LENGTH_LONG).show();


                        }




                    }
                    else { //somehow admin 1 phone number not written

                        Toast.makeText(getContext(),"issue: please contact admin.", Toast.LENGTH_LONG).show();



                    }

                    //handle admin 2nd


                    if(!sharedPrefsCheck_Admin_2.equals("")){

                        File fileHere = new File("/data/data/com.example.afinal/shared_prefs/" + sharedPrefsCheck_Admin_2 + ".xml");

                        if(fileHere.exists()){


                            SharedPreferences sharedPrefs_2 = getActivity().getSharedPreferences("com.example.finalV8_punchCard."+sharedPrefsCheck_Admin_2, Context.MODE_PRIVATE);
                            //will be read sharedprefs of "com.example.finalV8_punchCard.+60184670568"

                            nameHere_2 = sharedPrefs_2.getString("final_User_Name","");
                            phoneHere_2 = sharedPrefs_2.getString("final_User_Phone","");
                            adminName_2 = sharedPrefs_2.getString("final_Admin_Name","");
                            adminPhone_2 = sharedPrefs_2.getString("final_Admin_Phone","");

                        }else { //something wrong if not exist.

                            Toast.makeText(getContext(),"issue: please contact admin.", Toast.LENGTH_LONG).show();


                        }

                    }

                }else { //other than string 1 or 2

                    Toast.makeText(getContext(),"issue: please contact admin.", Toast.LENGTH_LONG).show();

                }

            }

        }else { //if main pool dont even exist.


            Toast.makeText(getContext(),"please register to an admin, or create admin", Toast.LENGTH_LONG).show();


        }

        ///
//
//        textView_RegAdmin.setText("Register As Admin");
//        textView_RegUser.setText("Register As User");
//        textViewAdmin_1.setText("Log in to Admin 1 :"+adminName);
//        textViewAdmin_2.setText("Log in to Admin 2 :"+adminName_2);
//        textView_Note.setText("MC or Outstation?");

//        floatButton_Admin_1.setOnClickListener(this);
//        floatButton_Admin_2.setOnClickListener(this);
//        floatButton_Reg_User.setOnClickListener(this);
//        floatButton_Reg_Admin.setOnClickListener(this);
//        floatButton_Back.setOnClickListener(this);
//        floatButton_Note_MC.setOnClickListener(this);

        // must be done here , boom menu

        for(int j = 0; j < boomMenuButton.getPiecePlaceEnum().pieceNumber();j++){

            boomMenuButton.addBuilder(BuilderManager.getTextOutsideCircleButtonBuilder(j));
            final long ss = boomMenuButton.getShowDuration();

            //boomMenuButton.setButtonRadius(60);
//            int size = boomMenuButton.getButtonRadius();
            //boomMenuButton.radius

            if(j==4) {
                boomMenuButton.getBuilder(4).normalColor(Color.YELLOW);

                //boomMenuButton.getBuilder(4).rad
                //boomMenuButton.getBuilder(4).

            }
            if(j==2) {

//                GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
//                        new int[] {0xFF616261,0xFF131313});
//                drawable.setCornerRadius(0f);

                //int trydraw = getResources().getDrawable(R.drawable.button_default, );

               // int trydraw = getActivity().getResources().getIdentifier("drawable/button_default",null,getActivity().getPackageName());

//                int trydraw = getResId("R.drawable/button_default",R.drawable.button_default);


                int trydraw = R.drawable.button_default;

                //boomMenuButton.getBuilder(2).normalColorRes(trydraw);
                boomMenuButton.getBuilder(2).imagePadding(new Rect(200,200,200,200));
                boomMenuButton.getBuilder(2).normalColor(trydraw);
                //boomMenuButton.getBuilder(2).
                //boomMenuButton.getBuilder(2).normalImageRes(trydraw);
//
//                Rect rect = new Rect();
//                rect.bottom=1;
//                rect.left=1;
//                rect.right=1;
//                rect.top=0;
//
//                boomMenuButton.getBuilder(4).imagePadding(rect);
//

            }
            boomMenuButton.setAutoBoom(true);
            boomMenuButton.setShadowEffect(true);



            //    Toast.makeText(getContext(),"size is: "+String.valueOf(size),Toast.LENGTH_SHORT).show();



            boomMenuButton.setOnBoomListener(new OnBoomListener() {


                @Override
                public void onClicked(int index, BoomButton boomButton) {

                    if(index==0){ //register admin

                       // boomButton.getLayoutAnimationListener().onAnimationEnd(this);

                        registerAdminBoolean = true;



                    }

                    if(index==1){ // register user

                        registerUserBoolean=true;

                    }

                    if(index==2){ // MC


                        Toast.makeText(getContext(),"duration is: "+String.valueOf(ss),Toast.LENGTH_SHORT).show();
                    }


                    if(index==3){ //log to admin 2

                        popAdmin_2=true;





                    }


                    if(index==4){ //log to admin 1


                    }




                }

                @Override
                public void onBackgroundClick() {

                    getFragmentManager().popBackStack();

                }

                @Override
                public void onBoomWillHide() {

                }

                @Override
                public void onBoomDidHide() {

                    if(registerUserBoolean) {

                        registerUserBoolean=false;
                        Intent intent = new Intent(getActivity(), RegAdmin_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //creating new task for registering,
                        //but we dont want user to be able to back.
                        startActivity(intent);
                    }

                    if(registerAdminBoolean){
                        registerAdminBoolean=false;

                        Intent intent2 = new Intent(getActivity(), RegAdmin_AsAdmin_Activity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent2);


                    }

                    if(popAdmin_1){
                        popAdmin_1=false;

                        if(nameHere!=null) {
                            FingerPrint_LogIn_Final_Activity.timeFragmentBoolean=true;
                            ((FingerPrint_LogIn_Final_Activity) getActivity()).nameUser = nameHere; //
                            ((FingerPrint_LogIn_Final_Activity) getActivity()).phoneUser = phoneHere; //
                            ((FingerPrint_LogIn_Final_Activity) getActivity()).globalAdminNameHere = adminName; //
                            ((FingerPrint_LogIn_Final_Activity) getActivity()).globalAdminPhoneHere = adminPhone; //

                            Log.i("finalSharePreDataCheck","Login_Select_Fragment 4, before return,name: "
                                    + nameHere+ ", phone: "+phoneHere+ ", adminName:"
                                    +adminName+" , adminPhone: "+adminPhone);
                        }

                        //this is we setup shared prefe
                        getFragmentManager().popBackStack();




                    }

                    if(popAdmin_2){
                        popAdmin_2=false;

                        if(nameHere!=null) {
                            FingerPrint_LogIn_Final_Activity.timeFragmentBoolean=true;
                            ((FingerPrint_LogIn_Final_Activity) getActivity()).nameUser = nameHere_2; //
                            ((FingerPrint_LogIn_Final_Activity) getActivity()).phoneUser = phoneHere_2; //
                            ((FingerPrint_LogIn_Final_Activity) getActivity()).globalAdminNameHere = adminName_2; //
                            ((FingerPrint_LogIn_Final_Activity) getActivity()).globalAdminPhoneHere = adminPhone_2; //

                            Log.i("finalSharePreDataCheck","Login_Select_Fragment 4, before return,name: "
                                    + nameHere_2+ ", phone: "+phoneHere_2+ ", adminName:"
                                    +adminName_2+" , adminPhone: "+adminPhone_2);
                        }

                        getFragmentManager().popBackStack();


                    }

                }

                @Override
                public void onBoomWillShow() {

                }

                @Override
                public void onBoomDidShow() {



                }
            });
        }




        return rootView;
    }

    // HANDLE RESOURCES

    public static int getResId(String variableName, Class<?> с) {

        Field field = null;
        int resId = 0;
        try {
            field = с.getField(variableName);
            try {
                resId = field.getInt(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resId;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        ((FingerPrint_LogIn_Final_Activity)getActivity()).backColor.setAlpha(1f);

        //data we want to sent back


    }
//
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()){
//
//            case R.id.select_fragment_FloatButton_backiD:
//
//                getFragmentManager().popBackStack();
//
//                break;
//
//            case R.id.select_fragment_FloatButton_admin1id:
//
//                //to disable register user, save data to shared preferences,
//                //then pull data, if data exist, dont allow for register, show toast
//                String test = nameHere;
//                if(nameHere!=null) {
//                    FingerPrint_LogIn_Final_Activity.timeFragmentBoolean=true;
//                    ((FingerPrint_LogIn_Final_Activity) getActivity()).nameUser = nameHere; //
//                    ((FingerPrint_LogIn_Final_Activity) getActivity()).phoneUser = phoneHere; //
//                    ((FingerPrint_LogIn_Final_Activity) getActivity()).globalAdminNameHere = adminName; //
//                    ((FingerPrint_LogIn_Final_Activity) getActivity()).globalAdminPhoneHere = adminPhone; //
//
//                    Log.i("finalSharePreDataCheck","Login_Select_Fragment 4, before return,name: "
//                            + nameHere+ ", phone: "+phoneHere+ ", adminName:"
//                            +adminName+" , adminPhone: "+adminPhone);
//                }
//
//                //this is we setup shared prefe
//                getFragmentManager().popBackStack();
//
//                break;
//            case R.id.select_fragment_FloatButton_admin2:
//                if(nameHere!=null) {
//                    FingerPrint_LogIn_Final_Activity.timeFragmentBoolean=true;
//                    ((FingerPrint_LogIn_Final_Activity) getActivity()).nameUser = nameHere_2; //
//                    ((FingerPrint_LogIn_Final_Activity) getActivity()).phoneUser = phoneHere_2; //
//                    ((FingerPrint_LogIn_Final_Activity) getActivity()).globalAdminNameHere = adminName_2; //
//                    ((FingerPrint_LogIn_Final_Activity) getActivity()).globalAdminPhoneHere = adminPhone_2; //
//
//                    Log.i("finalSharePreDataCheck","Login_Select_Fragment 4, before return,name: "
//                            + nameHere_2+ ", phone: "+phoneHere_2+ ", adminName:"
//                            +adminName_2+" , adminPhone: "+adminPhone_2);
//                }
//
//                getFragmentManager().popBackStack();
//
//
//
//                break;
//
//            case R.id.select_fragment_FloatButton_RegisterUseriD:
//
//                Intent intent = new Intent(getActivity(), RegAdmin_Activity.class);
//
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //creating new task for registering,
//                //but we dont want user to be able to back.
//                startActivity(intent);
//
//
//
//
//                break;
//
//            case R.id.select_fragment_FloatButton_RegisterAdminiD:
//
//            Intent intent2 = new Intent(getActivity(), RegAdmin_AsAdmin_Activity.class);
//            startActivity(intent2);
//            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //creating new task for registering,
//                //but we dont want user to be able to back.
//
//
//                break;
//
//            case R.id.select_fragment_FloatButton_addNoteMCiD:
//
//
//
//
//
//                break;
//
//
//
//
//
//        }
//
//
//    }
//



}
