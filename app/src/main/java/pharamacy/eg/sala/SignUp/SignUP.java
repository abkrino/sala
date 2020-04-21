package pharamacy.eg.sala.SignUp;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pharamacy.eg.sala.Class.CustomOnItemSelectedListener;
//import pharamacy.eg.sala.Class.GlideApp;
import pharamacy.eg.sala.Class.MultiSelectionSpinner;
import pharamacy.eg.sala.Confirm;
import pharamacy.eg.sala.ImagePickerActivity;
import pharamacy.eg.sala.R;
import pharamacy.eg.sala.Start_Activity;
import pharamacy.eg.sala.Users;

public class SignUP extends AppCompatActivity {
    EditText name, city, neighborhood, number;
    String userId, nameU, cityU, neighborhoodU, numberU, country_chooser, Specia_work;
    ImageView profile;
    Spinner spinner;
    Spinner spinner2;
    Button register;
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
    private Uri uri;
    private InterstitialAd mInterstitialAd;
    AdView adView;
    String[] array = {"اختار محافظة",
            "الأقصر", "الإسكندرية", "الشرقية", "أسيوط", "البحيرة", "القاهرة", "دمياط",
            "الشيخ", "المنوفية", "المنيا", "بورسعيد", "القليوبية", "أسوان", "الإسماعيلية", "سويف", "الدقهلية",
            "الفيوم", "الغربية", "الجيزة", "البحر الأحمر", "قنا", "جنوب سيناء", "شمال سيناء", "السويس", "سوهاج", "الوادي الجديد", "مطروح"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //////////////////////////////////////////
        //fire base
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
        adView = findViewById(R.id.adView);
        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-1743625796086476/3426647804");

        showAd();
        ButterKnife.bind(SignUP.this);

        addListenerOnButton();
        addListenerOnSpinnerItemSelection();
//        ImageView back = findViewById(R.id.back_arrow);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SignUP.this, SignIN.class));
//            }
//        });

        register = findViewById(R.id.register_btn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ads
                mInterstitialAd = new InterstitialAd(SignUP.this);
                mInterstitialAd.setAdUnitId("ca-app-pub-1743625796086476/9244097333");
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                startRegister();

            }
        });


        spinner_multi = findViewById(R.id.multispinner);
        //todo test R.string.array
        spinner_multi.setItems(getResources().getStringArray(R.array.country_arrays));
        loadProfileDefault();

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
        Intent intent = new Intent(SignUP.this, Start_Activity.class);
        nameU = name.getText().toString().trim();
        Specia_work = spinner2.getSelectedItem().toString().trim();
        country_chooser = spinner.getSelectedItem().toString().trim();
        country_work = new ArrayList<>(spinner_multi.getSelectedStrings());

        cityU = city.getText().toString().trim();
        neighborhoodU = neighborhood.getText().toString().trim();
        numberU = number.getText().toString().trim();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userId = user.getPhoneNumber();
            Toast.makeText(SignUP.this, " مرحبا بك " + nameU, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(SignUP.this, "من فضلك تأكد من تسجيل رقم هاتفك", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SignUP.this, Confirm.class));
            finish();
        }
        if(country_work.isEmpty()){
            country_work = getIntent().getStringArrayListExtra("country_work");
            if (country_work==null||country_work.isEmpty()){
                country_work.add(country_chooser);
            }
        }
        validN(nameU);
        validc(cityU);
        validnb(neighborhoodU);
        validNo(numberU);
        writeNewUserOffices(userId, nameU, cityU, neighborhoodU, country_chooser, Specia_work, country_work, numberU);
        uploadImage();
        startActivity(intent);
        finish();
    }

    private void validN(String nameU) {
        if (nameU.isEmpty()) {
            name.setError("ادخل الاسم ");
            name.requestFocus();
            return;
        }
    }

    private void validc(String cityU) {
        if (cityU.isEmpty()) {
            city.setError("ادخل المدينة");
            city.requestFocus();
            return;
        }
    }

    private void validnb(String neighborhoodU) {
        if (neighborhoodU.isEmpty()) {
            neighborhood.setError("ادخل المنطقة");
            neighborhood.requestFocus();
            return;
        }
    }

    private void validNo(String numberU) {
        if (numberU.isEmpty()) {
            number.setError("ادخل رقم الهاتف");
            number.requestFocus();
            return;
        } else if (numberU.length() < 10) {
            number.setError("ادخل رقم صحيح ");
            number.requestFocus();
            return;
        }
    }

    /////////////////////////////////////////////////////////

    private void loadProfile(String url) {
        Picasso.get()
                .load(url)
                .into(profile);
    }

    private void loadProfileDefault() {
        profile = findViewById(R.id.profile_pic);
        Picasso.get()
                .load(R.mipmap.user_foreground)
                .into(profile);

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
        Intent intent = new Intent(SignUP.this, ImagePickerActivity.class);
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
        Intent intent = new Intent(SignUP.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        //setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
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
            progressDialog.setTitle("loading");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + userId);
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(SignUP.this, "تم التسجيل", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(SignUP.this, "فشل التسجيل " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("loading....");
                        }
                    });
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUP.this);
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
        register = findViewById(R.id.register_btn);


    }

    public void writeNewUserOffices(String userId, String nameU, String cityU, String neighborhoodU, String country_chooser, String specia_work, ArrayList<String> country_work, String phoneNumber) {
        Users user = new Users(userId, nameU, cityU, neighborhoodU, country_chooser, specia_work, country_work, phoneNumber);
        mDatabase.child("users").child("Offices").child(userId).setValue(user);
    }
    public void showAd(){
        MobileAds.initialize(this, "ca-app-pub-1743625796086476~4917839514");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}