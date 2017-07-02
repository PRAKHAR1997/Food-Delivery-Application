package com.project.gaurs.tadqa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.gaurs.tadqa.Activities.ForgotPasswordActivity;
import com.project.gaurs.tadqa.Activities.MainScreen;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    TextInputEditText ed1, ed2;
    LoginButton lb;
    String eid, password;
    Button b1, b2, b3;
    private FirebaseAuth auth;
    ProgressDialog loading;
    TextInputLayout email, pass;
    String url;
    DatabaseReference firebaseDatabase;
    CallbackManager mCallbackManager;
    private String facebookname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (firebaseDatabase == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            firebaseDatabase = database.getReference();
        }else{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            firebaseDatabase = database.getReference();
        }
        auth = FirebaseAuth.getInstance();
        FacebookSdk.setApplicationId("216477612202792");
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        mCallbackManager = CallbackManager.Factory.create();
        ed1 = (TextInputEditText)findViewById(R.id.editText7);
        ed2 = (TextInputEditText)findViewById(R.id.password);
        b1 = (Button)findViewById(R.id.button_login);
        b2 = (Button)findViewById(R.id.forgotpass);
        lb = (LoginButton)findViewById(R.id.loginButton);
        b2.setPaintFlags(b2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        b3 = (Button)findViewById(R.id.button6);
        b3.setPaintFlags(b3.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(ed1, InputMethodManager.SHOW_IMPLICIT);
        eid = ed1.getText().toString();
        password = ed2.getText().toString();
        ed1.addTextChangedListener(new MyTextWatcher(ed1));
        ed2.addTextChangedListener(new MyTextWatcher(ed2));
        loading = new ProgressDialog(this);
        email = (TextInputLayout)findViewById(R.id.textInputLayout1);
        pass = (TextInputLayout)findViewById(R.id.textInputLayout);
        auth = FirebaseAuth.getInstance();
        lb.setReadPermissions("email", "public_profile");
        lb.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                JSONObject json = response.getJSONObject();
                                if (response.getError() != null) {
                                } else {
                                    try {
                                        url = json.getString("id").toString();
                                        facebookname = json.getString("name");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).executeAsync();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                // ...
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, ForgotPasswordActivity.class));
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
                loading.setMessage("Login Please Wait...");
                loading.show();
                eid = ed1.getText().toString();
                password = ed2.getText().toString();
                    auth.signInWithEmailAndPassword(eid, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Login.this, "Invalid Email or Password.", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(Login.this, MainScreen.class);
                                startActivity(intent);
                                finish();
                            }
                            loading.dismiss();
                        }
                    });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void handleFacebookAccessToken(final AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveData();
                            Intent intent = new Intent(Login.this, MainScreen.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(Login.this, "Sorry! User with this Email Address already exists.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private  void saveData(){
        String num = "Facebook user";
        UserInformation userInformation = new UserInformation(facebookname, num, url);
        FirebaseUser user = auth.getCurrentUser();

        firebaseDatabase.child("users").child(user.getUid()).setValue(userInformation);
    }
    class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()){
                case R.id.editText7:
                    validateEmail();
                    break;
                case R.id.password:
                    validatePassword();
                    break;
            }
        }
    }
    private boolean validateEmail() {
        String emailt = ed1.getText().toString().trim();

        if (emailt.isEmpty()) {
            email.setError("Email field can't be empty.");
            requestFocus(ed1);
            return false;
        } else if (!isValidEmail(emailt)) {
            email.setError("Invalid email.");
            requestFocus(ed1);
            return false;
        }else{
            email.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (ed2.getText().toString().trim().isEmpty()) {
            pass.setError("Password field can't be empty.");
            requestFocus(ed2);
            return false;
        }else if(ed2.getText().toString().trim().length()<8){
            pass.setError("Password must be more than 8 characters.");
            requestFocus(ed2);
            return false;
        }
        else {
            pass.setErrorEnabled(false);
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
    private void submitForm() {
        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
    }
}
