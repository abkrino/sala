package pharamacy.eg.sala;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import pharamacy.eg.sala.offices.home.HomeFragment;

public class Start_Activity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    public ImageView logo;
    public TextView titel, tagApp;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    String wellcomeName, Specia_workU;
    private Animation animation, animation2, animation3;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    String userId;
    int count_upload;
    Intent intent ;
    @Override
    protected void onStart() {
        super.onStart();
        checkPer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        setContentView(R.layout.activity_start);
        checkConnection();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        startAnimation();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            endAnimation();
            startActivity(new Intent(Start_Activity.this, SignIN.class));
            finish();
        } else {
            userId = user.getPhoneNumber();
            DatabaseReference referenceOf = FirebaseDatabase.getInstance().getReference().child("users").child("pharmacies").child(user.getPhoneNumber());
            referenceOf.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        String wellcomeName = dataSnapshot.child("nameU").getValue().toString();
                        checkPayment("pharmacies");
                        Toast.makeText(Start_Activity.this, "اهلا بك " + wellcomeName, Toast.LENGTH_LONG).show();
                        endAnimation();
                        startActivity(new Intent(Start_Activity.this, SearchProduct.class));
                        finish();
                    } else {
                        DatabaseReference referenceOf = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(userId);
                        referenceOf.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    wellcomeName = dataSnapshot.child("nameU").getValue().toString();
                                    Specia_workU = dataSnapshot.child("Specia_work").getValue().toString();
                                    checkPayment("Offices");
                                    checkStatues(userId);
                                    Toast.makeText(Start_Activity.this, "اهلا بك " + wellcomeName, Toast.LENGTH_LONG).show();
                                    // change main to mainph
                                     intent = new Intent(Start_Activity.this, MainOffices.class);
                                    intent.putExtra("Specia_workU", Specia_workU);
                                    endAnimation();

                                } else {
                                    Toast.makeText(Start_Activity.this, "انت لست مسجل لدينا", Toast.LENGTH_LONG).show();
                                    endAnimation();
                                    startActivity(new Intent(Start_Activity.this, SignIN.class));
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

                }
            });
        }
        // [START fcm_runtime_enable_auto_init]
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        // [END fcm_runtime_enable_auto_init]
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("tag", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = "msg of fire base is enable";
                        Log.d("tag", msg);
                    }
                });
    }

    public void startAnimation() {
        logo = findViewById(R.id.imageViewStart);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        logo.startAnimation(animation);
        titel = findViewById(R.id.textLog);
        animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rolate);
        titel.startAnimation(animation2);
        tagApp = findViewById(R.id.textLog2);
        animation3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rolate);
        tagApp.startAnimation(animation3);
    }

    public void endAnimation() {
        logo.clearAnimation();
        titel.clearAnimation();
        tagApp.clearAnimation();
    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        Snackbar snackbar;
        if (isConnected) {
            snackbar = Snackbar.make(findViewById(android.R.id.content), Html.fromHtml("<font color=\"#FFFFFF\">جيد!انت متصل بالأنترنت </font>"), Snackbar.LENGTH_SHORT);
            snackbar.show();
        } else {
            snackbar = Snackbar.make(findViewById(android.R.id.content), Html.fromHtml("<font color=\"#D81B60\">نأسف ! لا يوجد اتصال بالأنترنت</font>"), Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    public void checkPayment(String typeWork) {

        String fileName = "my payment";
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(typeWork).child(userId).child("resultPay");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String resultPay = dataSnapshot.getValue().toString();

                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("users").child(typeWork).child(userId).child("datePayment");
                    reference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                            if (dataSnapshot2.getValue() != null) {
                                if (resultPay.equals("accept")) {
                                    int datePayDay = Integer.parseInt(dataSnapshot2.child("end day").getValue().toString());
                                    int datePayMonth = Integer.parseInt(dataSnapshot2.child("end month").getValue().toString());
                                    sharedPref = getSharedPreferences(fileName, Context.MODE_PRIVATE);
                                    editor = sharedPref.edit();
                                    editor.putString("resultPay", resultPay);
                                    editor.putInt("datePayDay", datePayDay);
                                    editor.putInt("datePayMonth", datePayMonth);
                                    editor.apply();

                                } else {
                                    int datePayDay = Integer.parseInt(dataSnapshot2.child("day").getValue().toString());
                                    int datePayMonth = Integer.parseInt(dataSnapshot2.child("month").getValue().toString());
                                    sharedPref = getSharedPreferences(fileName, Context.MODE_PRIVATE);
                                    editor = sharedPref.edit();
                                    editor.putString("resultPay", resultPay);
                                    editor.putInt("datePayDay", datePayDay);
                                    editor.putInt("datePayMonth", datePayMonth);
                                    editor.apply();
                                }

                            } else {
                                sharedPref = getPreferences(Context.MODE_PRIVATE);
                                editor = sharedPref.edit();
                                editor.putString("resultPay", "failed");
                                editor.putInt("datePayDay", 0);
                                editor.putInt("datePayMonth", 0);
                                editor.apply();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    sharedPref = getPreferences(Context.MODE_PRIVATE);
                    editor = sharedPref.edit();
                    editor.putString("resultPay", "failed");
                    editor.putInt("datePayDay", 0);
                    editor.putInt("datePayMonth", 0);
                    editor.apply();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void checkPer() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.ACCESS_NETWORK_STATE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            checkPer();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    public void checkStatues(String uID) {
//todo بيطلع بترووو علي تبلت نورا
        DatabaseReference databaseReference20 = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(uID).child("count_upload");
        databaseReference20.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    count_upload =0 ;
                    sharedPref = getPreferences(Context.MODE_PRIVATE);
                    editor = sharedPref.edit();
                    editor.putInt("count_upload", count_upload);
                    editor.apply();
                    intent.putExtra("count_upload", count_upload);

                    startActivity(intent);
                    finish();
                } else {
                        count_upload =1 ;
                    sharedPref = getPreferences(Context.MODE_PRIVATE);
                    editor = sharedPref.edit();
                    editor.putInt("count_upload", count_upload);
                    editor.apply();
                    intent.putExtra("count_upload", count_upload);

                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
