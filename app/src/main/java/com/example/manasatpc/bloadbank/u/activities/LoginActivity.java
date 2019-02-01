package com.example.manasatpc.bloadbank.u.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.manasatpc.bloadbank.R;
import com.example.manasatpc.bloadbank.u.fregmants.userCycle.LoginFragment;
import com.example.manasatpc.bloadbank.u.helper.HelperMethod;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginFragment splashFragment = new LoginFragment();
        HelperMethod.replece(splashFragment,getSupportFragmentManager(),R.id.Cycle_User_contener,null,null,null);

    }

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();
    }
}