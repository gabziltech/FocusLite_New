package com.example.focuslite;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.focuslite.Database.DataHelp;
import com.example.focuslite.Database.MyOpenHelper;
import com.example.focuslite.Entities.UserEntities;

import java.util.List;

public class Login extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    private TextView ForgotPass,NewAccount;
    private Button SignIn,Skip;
    private String Email,Password;
    private MyOpenHelper db;
    private DataHelp dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DeclareVariables();

        String Session = db.getSessionTake();
        if (Session.equals("MainActivity")) {
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
        } else {
//        ForgotPass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "In Process...", Toast.LENGTH_SHORT).show();
////                Intent i = new Intent(Login.this, NewAccount.class);
////                startActivity(i);
//            }
//        });
            SignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetLoginData();
                    submitForm();
                }
            });

            NewAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Login.this, NewAccount.class);
                    startActivity(i);
                }
            });

            Skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Login.this, DemoActitivity.class);
                    startActivity(i);
                }
            });
        }
    }

    private void DeclareVariables() {
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
//        ForgotPass = (TextView) findViewById(R.id.forgot_password);
        NewAccount = (TextView) findViewById(R.id.signup);
        SignIn = (Button) findViewById(R.id.login_btn);
        Skip = (Button) findViewById(R.id.skip_btn);

        db = new MyOpenHelper(this);
        dh = new DataHelp(this);
    }

    private void SetLoginData() {
        Email = inputEmail.getText().toString().trim();
        Password = inputPassword.getText().toString().trim();
    }

    private void submitForm() {
        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        GetLogin();
    }

    private boolean validateEmail() {
        if (inputEmail.getText().toString().isEmpty() || !isValidEmail(inputEmail.getText().toString())) {
            inputLayoutEmail.setError("Enter valid email address");
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError("Enter the password");
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void GetLogin() {
        List<UserEntities> Users = db.getLogins(Email,Password);
        if (Users.size() > 0) {
            dh.UpdateSession("MainActivity",Users.get(0).getId());
            Toast.makeText(getApplicationContext(), "You Are Successfully Login...", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(), "Please Insert Correct Username & Password", Toast.LENGTH_SHORT).show();
            inputEmail.setText("");
            inputPassword.setText("");
            requestFocus(inputEmail);
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}
