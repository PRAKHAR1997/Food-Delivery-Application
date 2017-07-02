package com.project.gaurs.tadqa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Signup extends AppCompatActivity {

   // boolean isPressed = false;
    private DatabaseReference firebaseDatabase;
    ProgressDialog loading;
   // ImageButton ib;
    String namepattern = "^[a-zA-Z ]*$";
    private FirebaseAuth firebaseAuth;
     EditText name, number, email, password;
     TextInputLayout nameLayout, phoneLayout, editLayout, passLayout;
    Button done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebaseDatabase = database.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        nameLayout = (TextInputLayout)findViewById(R.id.namelayout);
        phoneLayout = (TextInputLayout)findViewById(R.id.phonelayout);
        editLayout = (TextInputLayout)findViewById(R.id.emaillayout);
        passLayout = (TextInputLayout)findViewById(R.id.passwordLayout);
        name = (EditText)findViewById(R.id.editText);
        //ib = (ImageButton) findViewById(R.id.imageButton2);
        number = (EditText)findViewById(R.id.editText2);
        email = (EditText)findViewById(R.id.editText3);
        password = (EditText)findViewById(R.id.editText4);
        done = (Button)findViewById(R.id.button4);
        loading = new ProgressDialog(this);

        name.addTextChangedListener(new MyTextWatcher(name));
        number.addTextChangedListener(new MyTextWatcher(number));
        email.addTextChangedListener(new MyTextWatcher(email));
        password.addTextChangedListener(new MyTextWatcher(password));

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
                registerUser();
            }
        });
        /*ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPressed) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                    ib.setImageResource(R.drawable.ic_visibility_white_18dp );
                }else{
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ib.setImageResource(R.drawable.ic_visibility_off_white_18dp);
                }
                isPressed = !isPressed;
            }
        });*/
    }
    private void registerUser(){

        String email1 = email.getText().toString();
        String pass = password.getText().toString();
        String name1 = name.getText().toString().trim();
        String num = number.getText().toString().trim();
        loading.setMessage("Registering Please Wait...");
        loading.show();
        firebaseAuth.createUserWithEmailAndPassword(email1, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            saveUserInformation();
                            startActivity(new Intent(Signup.this, Login.class));
                        }else{
                            Toast.makeText(Signup.this, "Error in Registration!", Toast.LENGTH_SHORT).show();
                        }
                        loading.dismiss();
                    }
                });
    }
    private void saveUserInformation() {
        String name1 = name.getText().toString().trim();
        String num = number.getText().toString().trim();
        UserInformation userInformation = new UserInformation(num,name1);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseDatabase.child("users").child(user.getUid()).setValue(userInformation);
        }

        class MyTextWatcher implements android.text.TextWatcher {

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
                switch (view.getId()) {
                    case R.id.editText:
                        validateName();
                        break;
                    case R.id.editText2:
                        validateNumber();
                        break;
                    case R.id.editText3:
                        validateEmail();
                        break;
                    case R.id.editText4:
                        validatePassword();
                        break;
                }
            }
        }
    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
    }

    private boolean validateName() {
        if (name.getText().toString().trim().isEmpty()) {
            nameLayout.setError("Name field can't be empty.");
            requestFocus(name);
            return false;
        } else if(!(name.getText().toString().trim()).matches(namepattern)){
            nameLayout.setError("Invalid Name");
            requestFocus(name);
            return false;
        }else{
            nameLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateNumber() {
        if (number.getText().toString().trim().isEmpty()) {
            phoneLayout.setError("Phone Number field can't be empty.");
            requestFocus(number);
            return false;
        }else if(number.getText().toString().trim().length()<10){
            phoneLayout.setError("Please enter valid phone number.");
            requestFocus(number);
            return false;
        }
            else
         {
            phoneLayout.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateEmail() {
        String emailt = email.getText().toString().trim();

        if (emailt.isEmpty()) {
            editLayout.setError("Email field can't be empty.");
            requestFocus(email);
            return false;
        } else if (!isValidEmail(emailt)) {
            editLayout.setError("Invalid email.");
            requestFocus(email);
            return false;
        }else{
            editLayout.setErrorEnabled(false);
            }

        return true;
    }

    private boolean validatePassword() {
        if (password.getText().toString().trim().isEmpty()) {
            passLayout.setError("Password field can't be empty.");
            requestFocus(password);
            return false;
        }else if(password.getText().toString().trim().length()<8){
            passLayout.setError("Password must be more than 8 characters.");
            requestFocus(password);
            return false;
        }
        else {
            passLayout.setErrorEnabled(false);
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
    }

