package com.example.afinal.fingerPrint_Login.test_mvp;

public class Test_Presenter implements ILogInPresenter {

    private ThisView thisView;

    //this is how we get back data from model.

    private IUser user;


    public Test_Presenter(ThisView thisView) {

        this.thisView = thisView;
    }


    @Override
    public void clear() {

        thisView.onClearText();
    }

    @Override
    public void doLogIn(String name, String pwd) {
        Boolean isLogInSuccess = true;
        //final int code = UserCheckIn.
        final int code = user.checkUserValidity(name,pwd);
        //thisView.onLogInResult();

    }
}
