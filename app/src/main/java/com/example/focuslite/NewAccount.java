package com.example.focuslite;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.focuslite.Database.DataHelp;
import com.example.focuslite.Database.MyOpenHelper;

public class NewAccount extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText inputEmail, inputPassword, inputConfirmPassword;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword, inputLayoutConfirmPassword;
    private Button SignUp;
    private String Email, Password, ConfirmPass;
    private DataHelp dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        DeclareVariables();

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetData();
                submitForm();
            }
        });
    }

    private void DeclareVariables() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewAccount.this, Login.class);
                startActivity(i);
            }
        });

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.input_layout_confirm_password);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        inputConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);
        SignUp = (Button) findViewById(R.id.signup_btn);

        dh = new DataHelp(this);
    }

    private void SetData() {
        Email = inputEmail.getText().toString().trim();
        Password = inputPassword.getText().toString().trim();
        ConfirmPass = inputConfirmPassword.getText().toString().trim();
    }

    public void submitForm() {
        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        if (!validateConfirmPassword()) {
            return;
        }

        dh.LoginSbmt(Email, Password);

        inputEmail.setText("");
        inputPassword.setText("");
        inputConfirmPassword.setText("");

        Toast.makeText(getApplicationContext(), "Registration Sucessful...", Toast.LENGTH_LONG).show();
        Intent i = new Intent(NewAccount.this, Login.class);
        startActivity(i);
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
        if (inputPassword.getText().toString().isEmpty() || inputPassword.getText().toString().length() < 6) {
            inputLayoutPassword.setError("Enter the password of minimum 6 digits");
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateConfirmPassword() {
        if (inputConfirmPassword.getText().toString().isEmpty()) {
            inputLayoutConfirmPassword.setError("Enter the confirm password");
            requestFocus(inputConfirmPassword);
            return false;
        } else if (!(inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString()))) {
            inputLayoutConfirmPassword.setError("Password does not match");
            requestFocus(inputConfirmPassword);
            return false;
        } else {
            inputLayoutConfirmPassword.setErrorEnabled(false);
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

    @Override
    public void onBackPressed(){
    }
}
