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
import android.text.Html;
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
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
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
import java.util.List;

import butterknife.OnClick;
import pharamacy.eg.sala.Class.CustomOnItemSelectedListener;
import pharamacy.eg.sala.Class.GlideApp;
import pharamacy.eg.sala.Confirm;
import pharamacy.eg.sala.ConnectivityReceiver;
import pharamacy.eg.sala.ImagePickerActivity;
import pharamacy.eg.sala.R;
import pharamacy.eg.sala.Start_Activity;
import pharamacy.eg.sala.Users;

public class SignUP_ph extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener  {
    private EditText name,  city,  neighborhood,  address, phoneNumber ;
    public String userId,nameU,  cityU,  neighborhoodU,  addressu, phoneNumberu,country_chooseru;
    ImageView profile;
    Spinner country_chooser;
    Button register_btn;
    public static final int REQUEST_IMAGE = 100;
//    public static final String PHARMACIES = "صيدليات";
private InterstitialAd mInterstitialAd;

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Bitmap bitmap;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_ph);
        //////////////////////////////////////////
        //fire base
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        //////////////////////////////////////////////////////
        name= findViewById(R.id.namePh);
        city = findViewById(R.id.cityPh);
        neighborhood = findViewById(R.id.neighborhoodPh);
        country_chooser = findViewById(R.id.spinnerPh);
        address = findViewById(R.id.address);
        register_btn= findViewById(R.id.registerPh_btn);
        phoneNumber = findViewById(R.id.numberPh);
        profile = findViewById(R.id.profilePh_pic);
        checkConnection();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

//        ImageView back = findViewById(R.id.back_arrow);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SignUP_ph.this , SignUpKind.class));
//            }
//        });




        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ads
                mInterstitialAd = new InterstitialAd(SignUP_ph.this);
                mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("BFE5A6AC72AC4A402AFDA3209FDB660A").build());
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                // TODO: Add adView to your view hierarchy.
                /////////////////////////////////////////////////////////////////////////////////////////

                startRegister();
            }
        });
        loadProfileDefault();
        ImagePickerActivity.clearCache(this);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerOptions();
            }
        });
    }
//////////////////////////////////////////////////////////////
    //
private void startRegister() {
        // change main to  mainPh
    Intent intent = new Intent(SignUP_ph.this, Start_Activity.class);
    nameU = name.getText().toString().trim();
    country_chooseru = country_chooser.getSelectedItem().toString().trim();
    cityU = city.getText().toString().trim();
    neighborhoodU = neighborhood.getText().toString().trim();
    phoneNumberu= phoneNumber.getText().toString().trim();
    addressu = address.getText().toString().trim();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    if(currentUser != null ){
        userId = user.getPhoneNumber();
        Toast.makeText(SignUP_ph.this, " مرحبا بك " + nameU, Toast.LENGTH_LONG).show();
    }else{
        Toast.makeText(SignUP_ph.this,"من فضلك تأكد من تسجيل رقم هاتفك",Toast.LENGTH_LONG).show();
        startActivity(new Intent(SignUP_ph.this, Confirm.class));
        finish();
    }
    validN(nameU);
    validc(cityU);
    validnb(neighborhoodU);
    validNo(phoneNumberu);
    writeNewUserpharmacies(userId, nameU, cityU, neighborhoodU, country_chooseru, addressu, phoneNumberu);
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
    private void validnb (String neighborhoodU) {
        if (neighborhoodU.isEmpty()) {
            neighborhood.setError("ادخل المنطقة");
            neighborhood.requestFocus();
            return;
        }
    }
    private void validNo (String numberU) {
        if (numberU.isEmpty()) {
            phoneNumber.setError("ادخل رقم الهاتف");
            phoneNumber.requestFocus();
            return;
        } else if (numberU.length() < 10) {
            phoneNumber.setError("ادخل رقم صحيح ");
            phoneNumber.requestFocus();
            return;
        }
    }

    //////////////////////////////////////////////////////////////
    private void loadProfile (String url){
        GlideApp.with(this).load(url)
                .into(profile);
        profile.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }

    private void loadProfileDefault () {

        GlideApp.with(this)
                .load(R.color.Red)
                .into(profile);
        profile.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
    }


    @OnClick({R.id.profile_pic})
    void onProfileImageClick () {
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


    private void showImagePickerOptions () {
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


    private void launchCameraIntent () {
        Intent intent = new Intent(SignUP_ph.this, ImagePickerActivity.class);
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

    private void launchGalleryIntent () {
        Intent intent = new Intent(SignUP_ph.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        //setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    @Override
    protected void onActivityResult ( int requestCode, int resultCode,
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
                            Toast.makeText(SignUP_ph.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(SignUP_ph.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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


    private void showSettingsDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUP_ph.this);
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
    private void openSettings () {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public void addListenerOnSpinnerItemSelection() {
        country_chooser = findViewById(R.id.spinnerPh);
        country_chooser.setOnItemSelectedListener(new CustomOnItemSelectedListener());

    }

    //get the selected dropdown list value
    public void addListenerOnButton() {

        country_chooser = findViewById(R.id.spinnerPh);
        register_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //ads
                mInterstitialAd = new InterstitialAd(SignUP_ph.this);
                mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("BFE5A6AC72AC4A402AFDA3209FDB660A").build());
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                // TODO: Add adView to your view hierarchy.
                /////////////////////////////////////////////////////////////////////////////////////////

                Toast.makeText(SignUP_ph.this,
                        "OnClickListener : " +
                                "\n Spinner 1 : " + String.valueOf(country_chooser.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void writeNewUserpharmacies(String userId,String nameU, String cityU, String neighborhoodU, String country_chooser, String address,String phoneNumber) {
        Users user = new Users(userId, nameU, cityU, neighborhoodU, country_chooser, address, phoneNumber);

        mDatabase.child("users").child("pharmacies").child(userId).setValue(user);
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

        } else {
            snackbar  = Snackbar.make(findViewById(R.id.sginUp), Html.fromHtml("<font color=\"#D81B60\">Sorry! Not connected to internet</font>") , Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

}
