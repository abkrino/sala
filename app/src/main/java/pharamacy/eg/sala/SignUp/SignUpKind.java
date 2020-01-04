package pharamacy.eg.sala.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import pharamacy.eg.sala.R;

public class SignUpKind extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kind);


        Button maktab = findViewById(R.id.maktab_btn);
        Button saydalya = findViewById(R.id.saydalya_btn);
        maktab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpKind.this, SignUP.class);
                startActivity(intent);
                finish();

            }
        });

        saydalya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpKind.this, SignUP_ph.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
