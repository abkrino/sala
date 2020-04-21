package pharamacy.eg.sala;


import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import pharamacy.eg.sala.SignUp.SignUpKind;


public class Confirm extends AppCompatActivity {
    private EditText editTextCode;
    private Chronometer chronometer;
    private TextView resendCode;
    private String mVerificationId;

    private Button codeOk;
    String no;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    //commit
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        editTextCode = findViewById(R.id.editTextcode);
        mAuth = FirebaseAuth.getInstance();
        no = getIntent().getStringExtra("meditText");
        sendVerificationCode(no);
        codeOk = findViewById(R.id.buttonCode);
        chronometer = findViewById(R.id.Chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        codeOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextCode.setError("Enter valid code");
                    editTextCode.requestFocus();
                    return;
                }


                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });
    }

    private void sendVerificationCode(String no) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+2" + no,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code;
            code = phoneAuthCredential.getSmsCode();
            Toast.makeText(Confirm.this, "تم التحقق مسبقا", Toast.LENGTH_LONG).show();
            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            signInWithPhoneAuthCredential(phoneAuthCredential);

            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Confirm.this,  "برجاء التاكد من وجود انترنت", Toast.LENGTH_LONG).show();
            Log.d("kharaaa", "onVerificationFailed: "+e);
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Toast.makeText(Confirm.this, "تم ارسال كود التحقق", Toast.LENGTH_LONG).show();
            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Confirm.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            no = getIntent().getStringExtra("meditText");
                            String numberCheck = "+2" + no;
                            DatabaseReference referenceOf = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(numberCheck);
                            //verification successful we will start the profile activity
                            referenceOf.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    //we are get the value from the key reference
                                    if (dataSnapshot.getValue() != null) {
                                        String wellcomeName = dataSnapshot.child("nameU").getValue().toString();
                                        String Specia_workU = dataSnapshot.child("Specia_work").getValue().toString();

                                        Toast.makeText(Confirm.this, "اهلا بك " + wellcomeName, Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(Confirm.this, MainOffices.class).putExtra("Specia_workU", Specia_workU));
                                        finish();
                                    } else {
                                        DatabaseReference referencePh = FirebaseDatabase.getInstance().getReference().child("users").child("pharmacies").child(numberCheck);
                                        referencePh.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.getValue() != null) {
                                                    String wellcomeName = dataSnapshot.child("nameU").getValue().toString();
                                                    Toast.makeText(Confirm.this, "اهلا بك " + wellcomeName, Toast.LENGTH_LONG).show();
                                                    // change main to mainph
                                                    startActivity(new Intent(Confirm.this, SearchProduct.class));
                                                    FirebaseUser user = task.getResult().getUser();
                                                    finish();
                                                }else{
                                                    Toast.makeText(Confirm.this, no + " اهلا بك يا ", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(Confirm.this, SignUpKind.class));
                                                    finish();
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    //verification unsuccessful.. display an error message
                                    String message = "Somthing is wrong, we will fix it soon...";

                                }
                            });

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                String message = "Invalid code entered...";
                            }


                        }
                    }
                });
    }

    public void goMain(View view) {
        Intent intent = new Intent(Confirm.this, SignIN.class);
        startActivity(intent);
        finish();
    }
}


