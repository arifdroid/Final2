package com.example.afinal.fingerPrint_Login.fingerprint_login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.afinal.R;
import com.example.afinal.fingerPrint_Login.register.register_as_admin.register_as_admin_regAdmin.RegAdmin_AsAdmin_Activity;
import com.example.afinal.fingerPrint_Login.register.register_with_activity.RegAdmin_Activity;

public class Login_Select_Action_Fragment extends Fragment implements View.OnClickListener{

    private Context mContext;

    private FloatingActionButton floatButton_Admin_1,floatButton_Admin_2, floatButton_Reg_Admin, floatButton_Reg_User,
                                floatButton_Note_MC, floatButton_Back;

    private TextView textViewAdmin_1, textViewAdmin_2, textView_RegUser, textView_RegAdmin, textView_Note;

    private FingerPrintFinal_Presenter presenter;
    private String nameHere;
    private String phoneHere;
    private String adminName;
    private String adminPhone;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //        return super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.floatingbutton_fragment_select, container,false);
        mContext = container.getContext();

        floatButton_Admin_1 = rootView.findViewById(R.id.select_fragment_FloatButton_admin1id);
        floatButton_Admin_2 = rootView.findViewById(R.id.select_fragment_FloatButton_admin2);
        floatButton_Reg_User = rootView.findViewById(R.id.select_fragment_FloatButton_RegisterUseriD);
        floatButton_Reg_Admin = rootView.findViewById(R.id.select_fragment_FloatButton_RegisterAdminiD);
        floatButton_Note_MC = rootView.findViewById(R.id.select_fragment_FloatButton_addNoteMCiD);
        floatButton_Back = rootView.findViewById(R.id.select_fragment_FloatButton_backiD);

        textView_Note = rootView.findViewById(R.id.select_fragment_textView_addNote_1id);
        textViewAdmin_1 = rootView.findViewById(R.id.select_fragment_textView_admin1id);
        textViewAdmin_2 = rootView.findViewById(R.id.select_fragment_textView_admin2id);
        textView_RegUser = rootView.findViewById(R.id.select_fragment_textView_regUser_1id);
        textView_RegAdmin = rootView.findViewById(R.id.select_fragment_textView_regAdmin_1id);

        //we pull from shared preferences here once
        //then set text

        SharedPreferences prefs = getActivity().getSharedPreferences("com.example.finalV8_punchCard", Context.MODE_PRIVATE);

        //SharedPreferences prefs = (SharedPreferences) getActivity().getPreferences("com.example.finalV8_punchCard", Context.MODE_PRIVATE);

        nameHere = prefs.getString("final_User_Name","");
        phoneHere = prefs.getString("final_User_Phone","");
        adminName = prefs.getString("final_Admin_Name","");
        adminPhone = prefs.getString("final_Admin_Phone","");

        Log.i("finalSharePreDataCheck","Login_Select_Fragment 3,name: "+ nameHere+ ", phone: "+phoneHere + ", adminName:"
                +adminName+" , adminPhone: "+adminPhone);




        ///

        textView_RegAdmin.setText("Register As Admin");
        textView_RegUser.setText("Register As UserCheckIn");
        textViewAdmin_1.setText("Log in to Admin 1");
        textViewAdmin_2.setText("Log in to Admin 2");
        textView_Note.setText("MC or Outstation?");

        floatButton_Admin_1.setOnClickListener(this);
        floatButton_Admin_2.setOnClickListener(this);
        floatButton_Reg_User.setOnClickListener(this);
        floatButton_Reg_Admin.setOnClickListener(this);
        floatButton_Back.setOnClickListener(this);
        floatButton_Note_MC.setOnClickListener(this);





        return rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        ((FingerPrint_LogIn_Final_Activity)getActivity()).backColor.setAlpha(1f);

        //data we want to sent back


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.select_fragment_FloatButton_backiD:

                getFragmentManager().popBackStack();

                break;

            case R.id.select_fragment_FloatButton_admin1id:

                //to disable register user, save data to shared preferences,
                //then pull data, if data exist, dont allow for register, show toast
                String test = nameHere;
                if(nameHere!=null) {
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

                break;
            case R.id.select_fragment_FloatButton_admin2:
                ((FingerPrint_LogIn_Final_Activity)getActivity()).nameUser = "ryn";
                getFragmentManager().popBackStack();



                break;

            case R.id.select_fragment_FloatButton_RegisterUseriD:

                Intent intent = new Intent(getActivity(), RegAdmin_Activity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //creating new task for registering,
                //but we dont want user to be able to back.
                startActivity(intent);




                break;

            case R.id.select_fragment_FloatButton_RegisterAdminiD:

            Intent intent2 = new Intent(getActivity(), RegAdmin_AsAdmin_Activity.class);
            startActivity(intent2);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //creating new task for registering,
                //but we dont want user to be able to back.


                break;

            case R.id.select_fragment_FloatButton_addNoteMCiD:





                break;





        }


    }




}
