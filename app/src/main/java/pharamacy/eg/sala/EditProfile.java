package pharamacy.eg.sala;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pharamacy.eg.sala.Class.CustomOnItemSelectedListener;
import pharamacy.eg.sala.Class.GlideApp;
import pharamacy.eg.sala.Class.MultiSelectionSpinner;


public class EditProfile extends AppCompatActivity {
    ImageView profile;
    private StorageReference mStorage;
    Spinner spinner;
    Spinner spinner2;
    Button savebtn;
    EditText name, city, neighborhood, number;
    String userId, nameU, cityU, neighborhoodU, numberU, country_chooser, Specia_work;
    ArrayList<String> country_work;
    //    public static final String OFFICES = "مكتب";
    MultiSelectionSpinner spinner_multi;
    public static final int REQUEST_IMAGE = 100;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private Bitmap bitmap;
    private Uri uri;

    String[] array = {"اختار محافظة",
            "الأقصر", "الإسكندرية", "الشرقية", "أسيوط", "البحيرة", "القاهرة", "دمياط",
            "كفر الشيخ", "المنوفية", "المنيا", "بورسعيد", "القليوبية", "أسوان", "الإسماعيلية", "بني سويف", "الدقهلية",
            "الفيوم", "الغربية", "الجيزة", "البحر الأحمر", "قنا", "جنوب سيناء", "شمال سيناء", "السويس", "سوهاج", "الوادي الجديد", "مطروح"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        //////////////////////////////////////////////////////
        name = findViewById(R.id.name);
        city = findViewById(R.id.city);
        neighborhood = findViewById(R.id.neighborhood);
        number = findViewById(R.id.number);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getPhoneNumber();
        }
        mStorage = FirebaseStorage.getInstance().getReference().child("images/").child(userId);

        setHint();

        ButterKnife.bind(this);

        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

//        ImageView back = findViewById(R.id.back_arrow);
////        back.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
//                startActivity(new Intent(EditProfile.this, Profile.class));
//            }
//        });



        savebtn = findViewById(R.id.register_btn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //henaa
                startRegister();

            }
        });


        spinner_multi = findViewById(R.id.multispinner);
        spinner_multi.setItems(array);


        ImagePickerActivity.clearCache(this);


        profile = findViewById(R.id.profile_pic);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerOptions();
            }
        });

    }

    private void startRegister() {

        Intent intent = new Intent(EditProfile.this, MainOffices.class);
        nameU = name.getText().toString().trim();
        Specia_work = spinner2.getSelectedItem().toString().trim();
        if(Specia_work.equals("النشاط")){
            Specia_work = getIntent().getStringExtra("Specia_workU");
        }
        country_chooser = spinner.getSelectedItem().toString().trim();
        if(country_chooser.equals(array[0])){
            country_chooser = getIntent().getStringExtra( "country_chooser");
        }
        country_work = new ArrayList<>(spinner_multi.getSelectedStrings());
        if(country_work.isEmpty()){
            country_work = getIntent().getStringArrayListExtra("country_work");
        }
        cityU = city.getText().toString().trim();
        neighborhoodU = neighborhood.getText().toString().trim();
        numberU = number.getText().toString().trim();
        FirebaseUser currentUser = mAuth.getCurrentUser();

            userId = user.getPhoneNumber();
            Toast.makeText(EditProfile.this, " مرحبا بك " + nameU, Toast.LENGTH_LONG).show();

        writeNewUserOffices(userId, nameU, cityU, neighborhoodU, country_chooser, Specia_work, country_work, numberU);
        uploadImage();
        Toast.makeText(this,"نحتاج الي اعادة تشغيل التطبيق ",Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    private String validN(String nameU) {
        if (nameU.isEmpty()){

            }
        return nameU;
    }

    private void validc(String cityU) {
        if (cityU.isEmpty()) {
            cityU = city.getHint().toString();
        }
    }

    private void validnb(String neighborhoodU) {
        if (neighborhoodU.isEmpty()) {
            neighborhoodU = neighborhood.getHint().toString();
        }
    }

    private void validNo(String numberU) {
        if (numberU.isEmpty()) {
            numberU = number.getHint().toString();
        } else if (numberU.length() < 10) {
            number.setError("ادخل رقم صحيح ");
            number.requestFocus();

        }
    }

    /////////////////////////////////////////////////////////

    private void loadProfile(String url) {
        GlideApp.with(this).load(url)
                .into(profile);
        profile.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }



    @OnClick({R.id.profile_pic})
    void onProfileImageClick() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }


    private void launchCameraIntent() {
        Intent intent = new Intent(EditProfile.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        //setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(EditProfile.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        //setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    @Override
    protected void
    onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    //henaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
                    // loading profile image from local cache
                    loadProfile(uri.toString());


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
//upload image user in fire base
    //https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934
    private void uploadImage() {

        if (uri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + userId);
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfile.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfile.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
        builder.setTitle("wa7ed");
        builder.setMessage("etnen");
        builder.setPositiveButton(getString(R.string.following), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    //navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    public void addListenerOnSpinnerItemSelection() {
        spinner = findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        spinner2 = findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    //get the selected dropdown list value
    public void addListenerOnButton() {

        spinner = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        savebtn = findViewById(R.id.register_btn);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(EditProfile.this,
                        "OnClickListener : " +
                                "\n Spinner 1 : " + String.valueOf(spinner.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void writeNewUserOffices(String userId, String nameU, String cityU, String neighborhoodU, String country_chooser, String specia_work, ArrayList<String> country_work, String phoneNumber) {
        Users user = new Users(userId, nameU, cityU, neighborhoodU, country_chooser, specia_work, country_work, phoneNumber);

        mDatabase.child("users").child("Offices").child(userId).setValue(user);
    }

    public void setHint(){
        TextView address = findViewById(R.id.textView2);
        address.setText("المحافظة: " + getIntent().getStringExtra(  "country_chooser" ));

        TextView province = findViewById(R.id.textView);
         ArrayList<String> s = getIntent().getStringArrayListExtra("country_work");
        String str = TextUtils.join(", ", s);
        province.setText("محافظات العمل: "+str);

        TextView work = findViewById(R.id.textView3);
        work.setText("النشاط: "+getIntent().getStringExtra("Specia_workU"));

        name.setText(getIntent().getStringExtra("name"));
        city.setText(getIntent().getStringExtra("city"));
        neighborhood.setText(getIntent().getStringExtra("neighborhood"));
        number.setText(getIntent().getStringExtra("phoneNumber"));

        mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                GlideApp.with(EditProfile.this)
                        .load(uri)
                        .into(profile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }


}

