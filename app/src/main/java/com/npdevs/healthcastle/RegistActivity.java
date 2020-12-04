package com.npdevs.healthcastle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//import android.support.v7.app.AppCompatActivity;

/**
 * @programName: RegistActivity.java
 * @programFunction: the regiter page
 * @createDate: 2018/09/19
 * @author: AnneHan
 * @version:
 * xx.   yyyy/mm/dd   ver    author    comments
 * 01.   2018/09/19   1.00   AnneHan   New Create
 */
public class RegistActivity extends AppCompatActivity {
    private EditText editPhone;
    private EditText editPwd;
    private Button btnRegist;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState){
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editPwd = (EditText) findViewById(R.id.editPwd);
        btnRegist = (Button) findViewById(R.id.btnRegist);
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * register event
     * @param v
     */
    public void OnMyRegistClick(View v)
    {
        boolean isTrue = true;


        if(isTrue = true){
            //call DBOpenHelper
            mAuth.createUserWithEmailAndPassword(editPhone.getText().toString(),editPwd.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("register", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(RegistActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(RegistActivity.this,LoginActivity.class);
                                //intent.putExtra("info", "No66778899");
                                RegistActivity.this.startActivity(intent);

//                this.finish();


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegistActivity.this, task.getException().toString(),
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });

        }else{
            return;
        }
    }
    public void OnMyLoginClick(View v)  {
        Intent intent=new Intent(RegistActivity.this,LoginActivity.class);
        //intent.putExtra("info", "No66778899");
        RegistActivity.this.startActivity(intent);
    }
}
