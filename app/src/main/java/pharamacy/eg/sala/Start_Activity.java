package pharamacy.eg.sala;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class Start_Activity extends AppCompatActivity {
    public ImageView logo;
    public TextView titel, tagApp;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Animation animation, animation2, animation3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        startAnimation();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            endAnimation();
            startActivity(new Intent(Start_Activity.this, SignIN.class));
            finish();
        } else {
            DatabaseReference referenceOf = FirebaseDatabase.getInstance().getReference().child("users").child("pharmacies").child(user.getPhoneNumber());
            referenceOf.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        String wellcomeName = dataSnapshot.child("nameU").getValue().toString();
                        Toast.makeText(Start_Activity.this, "اهلا بك " + wellcomeName, Toast.LENGTH_LONG).show();
                        endAnimation();
                        startActivity(new Intent(Start_Activity.this, SearchProduct.class));
                        finish();
                    } else {
                        DatabaseReference referenceOf = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(user.getPhoneNumber());
                        referenceOf.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    String wellcomeName = dataSnapshot.child("nameU").getValue().toString();
                                    String Specia_workU = dataSnapshot.child("Specia_work").getValue().toString();
                                    Toast.makeText(Start_Activity.this, "اهلا بك " + wellcomeName, Toast.LENGTH_LONG).show();
                                    // change main to mainph
                                    Intent intent = new Intent(Start_Activity.this, MainOffices.class);
                                    intent.putExtra("Specia_workU", Specia_workU);

                                    endAnimation();
                                    startActivity(intent);
                                    finish();
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
    public void endAnimation(){
        logo.clearAnimation();
        titel.clearAnimation();
        tagApp.clearAnimation();
    }

}
