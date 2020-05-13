package pharamacy.eg.sala.offices.home;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.VisibilityAwareImageButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import pharamacy.eg.sala.ReceyPro;
import pharamacy.eg.sala.adpter.AdapterListOfFile;
import pharamacy.eg.sala.Class.MainAdapter;
import pharamacy.eg.sala.Class.Product;
import pharamacy.eg.sala.Class.MainAdapterTwo;
import pharamacy.eg.sala.R;
import pharamacy.eg.sala.payment.GetPay;

import static androidx.core.content.ContextCompat.checkSelfPermission;
import static com.yalantis.ucrop.UCropFragment.TAG;

public class HomeFragment extends Fragment {
    private String userId, Specia_workU, excelPath, resultPay;
    private String Specia_workUIntent, lastDirectory;
    private View include, include2;
    private int datePayDay, datePayMonth;
    private Bundle bundle;
    private List<String> ignoreString;//
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private ArrayList<Product> list = new ArrayList<Product>();
    private ArrayList<Product> list2 = new ArrayList<Product>();
    private FloatingActionButton btuploadData;
    private RecyclerView recycler;
    private TextView textView;
    private ListView listOfFile;
    private String[] FileNameStrings, FilePathStrings;
    private View view;
    private ArrayList<String> nameProductL;
     ArrayList<File> excelSheet;
    private int cellsCount, month, day;
    private int count = 1;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Activity myActivity;
    private boolean r, result;
    private LayoutInflater factory;
    private View deleteDialogView;
    private android.app.AlertDialog deleteDialog;
    private AdView adView;
    private ImageView akrino;
    private ConstraintLayout mylayout1;
    private LinearLayout try_paid;
    private int count_upload;
    private TextView text_go_pay;
    private InterstitialAd mInterstitialAd;

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPref = getActivity().getSharedPreferences("edue", Context.MODE_PRIVATE);
        if (sharedPref.getString("statu_edue", "yes").equals("yes") || sharedPref.getString("statu_edue", "yes") == null) {
            target();
        } else {
            akrino.setVisibility(View.GONE);
            mylayout1.setVisibility(View.VISIBLE);
        }

    }



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        myActivity = getActivity();


        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        //ads
        checkPer();

        bundle = getActivity().getIntent().getExtras();

        user = FirebaseAuth.getInstance().getCurrentUser();

        getPhoneAndSpwork();
//        r = checkStatues(userId);

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
        nameProductL = new ArrayList<>();
        adView = view.findViewById(R.id.adView);
        adView = new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-1743625796086476/3426647804");
        startAd();
        akrino = view.findViewById(R.id.abkrino);
        mylayout1 = view.findViewById(R.id.mylayout1);
        textView = view.findViewById(R.id.textUpload);
        try_paid = view.findViewById(R.id.try_paid);
        text_go_pay = view.findViewById(R.id.text_go_pay);
        Specia_workUIntent = bundle.getString("Specia_workU");
        count_upload = bundle.getInt("count_upload");
        excelSheet = new ArrayList<>();

        ignoreString = Arrays.asList(getResources().getStringArray(R.array.Ignore_String));
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
//        //TapTarget
//        showProg();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        factory = LayoutInflater.from(getActivity());
        deleteDialogView = factory.inflate(R.layout.alert_go_pay, null);

        deleteDialog = new android.app.AlertDialog.Builder(getActivity()).create();

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        searchView();
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        listOfFile = view.findViewById(R.id.listOfFile);
        recycler = view.findViewById(R.id.recycle_main);
        include = view.findViewById(R.id.titleSheet);
        include2 = view.findViewById(R.id.titleSheet2);
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

//        mStorage = FirebaseStorage.getInstance().getReference().child("images/").child(userId);
        DatePicker pikPicker = new DatePicker(getActivity());
        month = pikPicker.getMonth() + 1;
        day = pikPicker.getDayOfMonth();

        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

        if (checkStatues(userId)) {
            if (!checkPayment()) {
                recycler.setVisibility(View.GONE);
                try_paid.setVisibility(View.VISIBLE);
            } else {
                getRececlyData();

            }
        } else {
            getRececlyData();

        }


        ActionFloatingButton();
        text_go_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "من فضلك أضف بيانات البطاقة ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), GetPay.class).putExtra("phoneNumberOfficess", userId).putExtra("type", "offices");
                getActivity().startActivity(intent);
            }
        });
        mylayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.clearAnimation();
                btuploadData.clearAnimation();
                textView.setVisibility(View.GONE);
            }
        });
    }
    //end onViewCreated
    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

    public void searchView() {
        final EditText search = view.findViewById(R.id.search_txt);
        ImageView cancel = view.findViewById(R.id.cancelbtn);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String a = s.toString();
                if (!a.isEmpty()) {
                    cancel.setVisibility(View.VISIBLE);
                }
                if (a.isEmpty()) {
                    cancel.setVisibility(View.INVISIBLE);
                }
                RecyclerView recycle = view.findViewById(R.id.recycle_main);
                if (Specia_workUIntent.equals("أدويةمحلية")) {
                    include.setVisibility(View.VISIBLE);
                    include2.setVisibility(View.GONE);

                    MainAdapter adapter = new MainAdapter(getActivity(), list);
                    recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter.getFilter().filter(s);
                    listOfFile.setVisibility(View.GONE);
                    recycle.setAdapter(adapter);
                } else {
                    include.setVisibility(View.GONE);
                    include2.setVisibility(View.VISIBLE);

                    MainAdapterTwo adapterTwo = new MainAdapterTwo(getActivity(), list2);
                    recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapterTwo.getFilter().filter(s);
                    listOfFile.setVisibility(View.GONE);
                    recycle.setAdapter(adapterTwo);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
            }
        });
    }

    public void ActionFloatingButton() {
        btuploadData = view.findViewById(R.id.fab);
        btuploadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
                //ads


                // ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
                startAnimation();
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert();
                    }
                });
            }
        });
    }

    public void alert() {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        if (Specia_workU.equals("أدويةمحلية")) {
            final View deleteDialogView = factory.inflate(R.layout.alert_view, null);
            final AlertDialog deleteDialog = new AlertDialog.Builder(getActivity()).create();
            deleteDialog.setView(deleteDialogView);
            deleteDialogView.findViewById(R.id.alert_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getProductData("أدويةمحلية");

                    //your business logic
                    deleteDialog.dismiss();
                    include.setVisibility(View.GONE);
                    include2.setVisibility(View.GONE);
                    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
                    showProg("loading", "لحظات لتحميل الملفات  ");
                    //read file of excel
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            checkInternalStorage();
                            // to show list in activity because the thread cant access activity
                            myActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AdapterListOfFile adapter = new AdapterListOfFile(getActivity(), FileNameStrings, FilePathStrings);
                                    listOfFile.setAdapter(adapter);
                                    recycler.setVisibility(View.GONE);
                                    listOfFile.setVisibility(View.VISIBLE);
                                    ListFileClick();
//
                                }
                            });
                        }
                    }).start();
                    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
                    textView.setVisibility(View.GONE);
                    btuploadData.clearAnimation();
                }
            });
            deleteDialog.show();
        } else {
            switch (Specia_workU) {
                case "أدوية مستوردة":
                    getProductData("أدوية مستوردة");
                    break;
                case "مستلزمات":
                    getProductData("مستلزمات");
                    break;
            }
            final View deleteDialogView = factory.inflate(R.layout.alert_view2, null);
            final AlertDialog deleteDialog = new AlertDialog.Builder(getActivity()).create();
            deleteDialog.setView(deleteDialogView);
            deleteDialogView.findViewById(R.id.alert_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //your business logic

                    deleteDialog.dismiss();
                    include.setVisibility(View.GONE);
                    include2.setVisibility(View.GONE);
                    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
                    showProg("loading", "لحظات لتحميل الملفات ");
                    //read file of excel
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            checkInternalStorage();
                            // to show list in activity because the thread cant access activity
                            myActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AdapterListOfFile adapter = new AdapterListOfFile(getActivity(), FileNameStrings, FilePathStrings);
                                    listOfFile.setAdapter(adapter);
                                    recycler.setVisibility(View.GONE);
                                    listOfFile.setVisibility(View.VISIBLE);
                                    ListFileClick();
//
                                }
                            });
                        }
                    }).start();
                    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
                    textView.setVisibility(View.GONE);
                    btuploadData.clearAnimation();
                }
            });
            deleteDialog.show();
        }
    }

    public void ListFileClick() {
        listOfFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        list.clear();
                        list2.clear();
                        lastDirectory = FilePathStrings[i];
                        //Execute method for reading the excel data.
                        myActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (lastDirectory != null) {
                                    readExcelData(lastDirectory);
//                                    r = checkStatues(userId);

                                }
                                list.size();
                                //showProductInList();
                                sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                                editor = sharedPref.edit();
                                editor.putString("excelpath", lastDirectory);
                                editor.apply();
                                showProductInList();
//                                r = ;
                                if (checkStatues(userId)) {
                                    if (!checkPayment()) {
                                        showdialog();
                                    } else {
                                        switch (Specia_workU) {
                                            case "أدويةمحلية":
                                                DelateProdut("أدويةمحلية");
                                                break;
                                            case "أدوية مستوردة":
                                                getProductData("أدوية مستوردة");
                                                break;
                                            case "مستلزمات":
                                                getProductData("مستلزمات");
                                                break;
                                        }

                                        uploadProduct();
                                        changeStatue();
                                    }

                                } else {
                                    switch (Specia_workU) {
                                        case "أدويةمحلية":
                                            DelateProdut("أدويةمحلية");
                                            break;
                                        case "أدوية مستوردة":
                                            getProductData("أدوية مستوردة");
                                            break;
                                        case "مستلزمات":
                                            getProductData("مستلزمات");
                                            break;
                                    }
                                    uploadProduct();
                                    changeStatue();
                                }
                            }
                        });

                    }
                }).start();
                showProg("loading", "لحظات لقراءة الملف " + FileNameStrings[i]);
            }
        });
    }

    private void readExcelData(String filePath) {
        //declare input file
        File inputFile = new File(filePath);
        try {
            InputStream inputStream = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            //outer loop, loops through rows
            for (int r = 0; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                // if Conditional to handel excel sheet title or row is Empty or row  equals null
                if (row == null) {
                    row = sheet.getRow(r + 1);
                    cellsCount = row.getPhysicalNumberOfCells();
                    r++;
                } else {
                    cellsCount = row.getPhysicalNumberOfCells();
                }
//                if (cellsCount!=null);
                if (cellsCount == 3 || cellsCount == 2) {
                    String a = null;
                    double b = 0;
                    double d = 0;
                    //inner loop, loops through columns
                    for (int i = 0; i < cellsCount; i++) {
                        //handles if there are to many columns on the excel sheet.
                        if (i > 2) {
                            Log.e(TAG, "readExcelData: ERROR. Excel File Format is incorrect! ");
                            Toast.makeText(getActivity(), "محتوي الملف غير مدعوم ", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), "يجب ان يحتوي الملف علي ثلاث اعمدة بحد اقصي ", Toast.LENGTH_LONG).show();
                            break;
                        } else {
                            String value = getCellAsString(row, i, formulaEvaluator);
                            String cellInfo = "r:" + r + "; c:" + i + "; v:" + value;
                            Log.d(TAG, "readExcelData: Data from row: " + cellInfo);
                            // if Conditional to handel excel sheet title or value is Empty or value equals null
                            //todo edit here to start insert into array list


                            if (value.equals("") || ignoreString.contains(value.replaceAll("\\s+", ""))) {
                            } else {
                                switch (i) {
                                    case 0:
                                        a = value;
                                        break;
                                    case 1:
                                        b = Double.parseDouble(value);
                                        list2.add(new Product(a, b));
                                        break;
                                    case 2:
                                        d = Double.parseDouble(value);
                                        list.add(new Product(a, b, d));
                                        break;
                                }
                            }
                        }
                    }
                }

            }
            Log.d(TAG, "readExcelData: STRINGBUILDER: " + list.size());
            if (list.isEmpty()) {
                Toast.makeText(getActivity(), "عدد الاصناف  " + list2.size(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "عدد الاصناف  " + list.size(), Toast.LENGTH_LONG).show();
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage());
            Toast.makeText(getActivity(), "الملف غير موجود", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(TAG, "readExcelData: Error reading inputstream. " + e.getMessage());
            Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity(), "صغية الملف غير معروفة", Toast.LENGTH_LONG).show();
        }
    }
    //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ

    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("dd/MM/yy", Locale.US);
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = "" + numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "getCellAsString: NullPointerException: " + e.getMessage());
        }
        return value;
    }

    private void checkInternalStorage() {
        excelSheet = readExcleFile(Environment.getExternalStorageDirectory());
        FileNameStrings = new String[excelSheet.size()];
        FilePathStrings = new String[excelSheet.size()];
        for (int i = 0; i < excelSheet.size(); i++) {
            FileNameStrings[i] = excelSheet.get(i).getName().toString().replace(".xlsx", " ");
            FilePathStrings[i] = excelSheet.get(i).getPath();
        }
        progressDialog.dismiss();
        //on click on the item to read file excel
    }

    public ArrayList<File> readExcleFile(File root) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] file3 = root.listFiles();

        if (file3 != null) {
            for (File files : file3) {
                    if (files.isDirectory()) {
                        arrayList.addAll(readExcleFile(files));
                    } else {
                        if (files.getName().endsWith(".xlsx")) {
                            arrayList.add(files);
                        }
                    }
                }
        }else{
            Toast.makeText(getActivity(), "لا توجد ملفات", Toast.LENGTH_SHORT).show();
        }

        return arrayList;
    }

    public void showProductInList() {
        if (Specia_workU.equals("أدويةمحلية")) {
            Collections.sort(list);
            MainAdapter adapter = new MainAdapter(getActivity(), list);
            adapter.updateList(list);
            include.setVisibility(View.VISIBLE);
            include2.setVisibility(View.GONE);

            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            listOfFile.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
            recycler.setAdapter(adapter);
            progressDialog.dismiss();
        } else {
            Collections.sort(list2);
            include.setVisibility(View.GONE);
            include2.setVisibility(View.VISIBLE);

            MainAdapterTwo adapterTwo = new MainAdapterTwo(getActivity(), list2);
            adapterTwo.updateList(list2);
            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            listOfFile.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
            recycler.setAdapter(adapterTwo);
            progressDialog.dismiss();
        }
    }

    public void uploadProduct() {
        Collections.sort(list);


        if (Specia_workU.equals("أدويةمحلية")) {
            for (int p = 1; p < list.size(); p++) {
                int finalP = p;
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    //todo try and handel if crash whene discount == null
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        writeNewPost(userId, list.get(finalP).getNameProduct(), list.get(finalP).getPrice(), list.get(finalP).getDiscount());

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//   writeNewPost( userId, list.get(finalP ).getNameProduct(),  list.get(finalP).getPrice(), list.get(finalP).getDiscount());
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

        } else {
            Collections.sort(list2);

            for (int p = 1; p < list2.size(); p++) {

                int finalP = p;
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        writeNewPost1(userId, list2.get(finalP).getNameProduct(), list2.get(finalP).getPrice());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//  writeNewPost( userId, list.get(finalP ).getNameProduct(),  list.get(finalP).getPrice(), list.get(finalP).getDiscount());
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

        }
    }

    private void writeNewPost(String userId, String nameProduct, double price, double discount) {
        // attention firebase don't support ( '.', '#' ,'$' ,'[',' ]')
        if (nameProduct.contains(".")) {
            nameProduct = nameProduct.replace('.', ',');
        } else if (nameProduct.contains("#")) {
            nameProduct = nameProduct.replace('#', '\t');
        } else if (nameProduct.contains("$")) {
            nameProduct = nameProduct.replace('$', '\t');
        } else if (nameProduct.contains("[")) {
            nameProduct = nameProduct.replace('[', '\t');
        } else if (nameProduct.contains("]")) {
            nameProduct = nameProduct.replace(']', '\t');
        } else if (nameProduct.contains("/")) {
            nameProduct = nameProduct.replace('/', '\\');
        }

        Product product = new Product(nameProduct, price, discount);
        Map<String, Object> productValues = product.toMap();
        databaseReference.child("product").child(Specia_workU).child(nameProduct).child(userId);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/product/" + "/" + Specia_workU + "/" + "/" + nameProduct + "/" + "/" + userId + "/", productValues);
        databaseReference.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(getActivity(), "تم رفع منتجاتك الي خودمنا", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void writeNewPost1(String userId, String nameProduct, double price) {
        // attention firebase don't support ( '.', '#' ,'$' ,'[',' ]')

        if (nameProduct.contains(".")) {
            nameProduct = nameProduct.replace('.', ',');
        } else if (nameProduct.contains("#")) {
            nameProduct = nameProduct.replace('#', '\t');
        } else if (nameProduct.contains("$")) {
            nameProduct = nameProduct.replace('$', '\t');
        } else if (nameProduct.contains("[")) {
            nameProduct = nameProduct.replace('[', '\t');
        } else if (nameProduct.contains("]")) {
            nameProduct = nameProduct.replace(']', '\t');
        } else if (nameProduct.contains("/")) {
            nameProduct = nameProduct.replace('/', '\\');
        }

        Product product1 = new Product(price);
        Map<String, Object> productValues1 = product1.toMap();
        databaseReference.child("product").child(Specia_workU).child(nameProduct).child(userId);
        Map<String, Object> childUpdates = new HashMap<>();
        switch (Specia_workU) {
            case "أدوية مستوردة":
                childUpdates.put("/product/" + "/" + Specia_workU + "/" + "/" + nameProduct + "/" + "/" + userId + "/", productValues1);
                databaseReference.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "تم رفع منتجاتك الي خودمنا", Toast.LENGTH_SHORT).show();

                    }
                });
                break;
            case "مستلزمات":
                childUpdates.put("/product/" + "/" + Specia_workU + "/" + "/" + nameProduct + "/" + "/" + userId + "/", productValues1);
                databaseReference.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "تم رفع منتجاتك الي خودمنا", Toast.LENGTH_SHORT).show();

                    }
                });
                break;
        }

    }

    public void startAnimation() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.moveleft);
        Animation animation3 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        btuploadData.startAnimation(animation);
        textView.startAnimation(animation3);
        textView.setVisibility(View.VISIBLE);


    }

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private void startAd() {
        MobileAds.initialize(getActivity(), "ca-app-pub-1743625796086476~4917839514");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    public void showProg(String title, String message) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false); //To prevent the user dismiss progressDialog
        progressDialog.show();

    }

    public void changeStatue() {

        databaseReference.child("users").child("Offices").child(userId).child("count_upload").setValue(count);
        String fileName = "my payment";

        count_upload = count;
        sharedPref = getActivity().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putInt("count_upload", count);
        editor.apply();

    }

    public boolean checkStatues(String uID) {
        if (count_upload == 0) {

            r = false;
        } else {
            r = true;
        }


        return r;
    }

    public void showdialog() {
        deleteDialog.setView(deleteDialogView);
        deleteDialog.setCanceledOnTouchOutside(false);
        deleteDialog.setCancelable(false);
        deleteDialog.show();
        deleteDialogView.findViewById(R.id.btPay2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
                Toast.makeText(getActivity(), "من فضلك أضف بيانات البطاقة ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), GetPay.class).putExtra("phoneNumberOfficess", userId).putExtra("type", "offices");
                getActivity().startActivity(intent);

            }
        });
        deleteDialogView.findViewById(R.id.alert_bt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
                recycler.setVisibility(View.GONE);
                try_paid.setVisibility(View.VISIBLE);


            }
        });
    }

    public boolean checkPayment() {
        String defult = "def";
        SharedPreferences sharedPref = getActivity().getSharedPreferences("my payment", Context.MODE_PRIVATE);
        resultPay = sharedPref.getString("resultPay", defult);
        if (resultPay != null) {
            switch (resultPay) {

                case "def":
                    result = false;

                    break;
                case "failed":
                    result = false;
                    break;
                case "accept":
                    result = true;
//                Toast.makeText(context, "انت تستمتع بالمزايا المدفوعة ", Toast.LENGTH_SHORT).show();
                    datePayDay = sharedPref.getInt("datePayDay", 0);
                    datePayMonth = sharedPref.getInt("datePayMonth", 0);
                    if (datePayMonth == month && datePayDay == day) {
                        result = false;
                    } else if (datePayMonth == month && datePayDay < day) {
                        result = false;
                    } else if (datePayMonth == month && datePayDay > day) {
                        result = true;
                    } else if (datePayMonth < month) {
                        result = false;
                    } else if (datePayMonth > month) {
                        result = true;
                    }
                    break;
            }
        }


        return result;
    }

    public void checkPer() {

        if (checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.ACCESS_NETWORK_STATE}, 1);

        }


    }

    public void getPhoneAndSpwork() {
        if (user != null) {

            userId = Objects.requireNonNull(user).getPhoneNumber();
            DatabaseReference reference = FirebaseDatabase.getInstance().
                    getReference().child("users").child("Offices").child(userId);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Specia_workU = dataSnapshot.child("Specia_work").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    public void target() {

        final SpannableString spannedDesc = new SpannableString("محتوي ملف الاكسل يجب ان يتكون من عمودين (اسم الصنف - السعر) اوثلاث (اسم الصنف - السعر -الخصم)");
        final SpannableString spannedDesc2 = new SpannableString(" يمكنك ايضا البحث باسم الصنف علي الصنف الذي تريده");
        final SpannableString spannedDesc3 = new SpannableString("هذا الزر يمكنك من الدخول علي جهازك وتحميل ملفات الاكسل ومن ثم رفعها الي خوادمنا ");

        final TapTargetSequence sequence = new TapTargetSequence(getActivity())
                .targets(
                        TapTarget.forView(getActivity().findViewById(R.id.view_title), " ملف الاكسل ", spannedDesc)
                                // All options below are optional
                                .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.70f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.White_transparent_white_hex_5)   // Specify a color for the target circle
                                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.White_White)      // Specify the color of the title text
                                .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.White_transparent_white_hex_13)  // Specify the color of the description text
                                .textColor(R.color.Blue_Lavender)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.Black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(false)                   // Whether to tint the target view's color
                                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(167)
                                .id(1),          // Specify the target radius (in dp)

                        TapTarget.forView(getActivity().findViewById(R.id.search), " البحث ", spannedDesc2)
                                // All options below are optional
                                .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.70f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.White_transparent_white_hex_5)   // Specify a color for the target circle
                                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.White_White)      // Specify the color of the title text
                                .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.White_transparent_white_hex_13)  // Specify the color of the description text
                                .textColor(R.color.Blue_Lavender)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.Black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(false)                   // Whether to tint the target view's color
                                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(167)
                                .id(2),            // Specify the target radius (in dp)
                        TapTarget.forView(getActivity().findViewById(R.id.fab), " زر رفع القائمة  ", spannedDesc3)
                                // All options below are optional
                                .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.70f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.White_transparent_white_hex_5)   // Specify a color for the target circle
                                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.White_White)      // Specify the color of the title text
                                .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.White_transparent_white_hex_13)  // Specify the color of the description text
                                .textColor(R.color.Blue_Lavender)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.Black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(false)                   // Whether to tint the target view's color
                                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(60)
                                .id(3)

                )
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        final View deleteDialogView = factory.inflate(R.layout.dialog_edu, null);
                        final AlertDialog deleteDialog = new AlertDialog.Builder(getActivity()).create();
                        deleteDialog.setView(deleteDialogView);
                        deleteDialog.setCancelable(false);
                        deleteDialog.show();
                        deleteDialogView.findViewById(R.id.alert_btn_no).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                sharedPref = getActivity().getSharedPreferences("edue", Context.MODE_PRIVATE);
                                editor = sharedPref.edit();
                                editor.putString("statu_edue", "no");
                                editor.apply();
                                deleteDialog.dismiss();
                                Toast.makeText(myActivity, "لن يتم عرض الشرح في كل مرة ", Toast.LENGTH_SHORT).show();

                            }
                        });
                        deleteDialogView.findViewById(R.id.alert_yes).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                sharedPref = getActivity().getSharedPreferences("edue", Context.MODE_PRIVATE);
                                editor = sharedPref.edit();
                                editor.putString("statu_edue", "yes");
                                editor.apply();
                                deleteDialog.dismiss();
                                Toast.makeText(myActivity, "سيتم عرض الشرح في كل مرة ", Toast.LENGTH_SHORT).show();

                            }
                        });


                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {

                    }
                });


        final SpannableString spannedDesc1 = new SpannableString("تم تطوير التطبيق من شركة عبقرينو لنظم المعلومات و تطوير وادارة المشروعات " +
                "تطبيق sala يتيح لك نشر منتجاتك عن طريق " +
                "رفع منتجاتك علي خودمنا في صورة sheet excel ومن ثم ظهوره الي جميع الصيدليات لدينا " +
                " هيا بينا ");
//        spannedDesc.setSpan(new UnderlineSpan(), spannedDesc1.length() - "TapTargetView".length(), spannedDesc.length(), 0);
        TapTargetView.showFor(getActivity(), TapTarget.forView(getActivity().findViewById(R.id.abkrino), " فكرة التطبيق", spannedDesc1)
                // All options below are optional
                .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                .outerCircleAlpha(0.70f)            // Specify the alpha amount for the outer circle
                .targetCircleColor(R.color.White_transparent_white_hex_5)   // Specify a color for the target circle
                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                .titleTextColor(R.color.White_White)      // Specify the color of the title text
                .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                .descriptionTextColor(R.color.White_transparent_white_hex_13)  // Specify the color of the description text
                .textColor(R.color.Blue_Lavender)            // Specify a color for both the title and description text
                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                .dimColor(R.color.Black)            // If set, will dim behind the view with 30% opacity of the given color
                .drawShadow(true)                   // Whether to draw a drop shadow or not
                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                .tintTarget(false)                   // Whether to tint the target view's color
                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                .targetRadius(120), new TapTargetView.Listener() {
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
                // .. which evidently starts the sequence we defined earlier
                akrino.setVisibility(View.GONE);
                mylayout1.setVisibility(View.VISIBLE);
                sequence.start();
            }

            @Override
            public void onOuterCircleClick(TapTargetView view) {
                super.onOuterCircleClick(view);
                Toast.makeText(view.getContext(), "اضغط علي العنصر ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                Log.d("TapTargetViewSample", "You dismissed me :(");
            }
        });

    }

    public void getRececlyData() {
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        excelPath = sharedPref.getString("excelpath", null);
        if (excelPath != null) {
            readExcelData(excelPath);
            if (Specia_workUIntent != null) {
                if (Specia_workUIntent.equals("أدويةمحلية")) {
                    include.setVisibility(View.VISIBLE);
                    include2.setVisibility(View.GONE);
                    Collections.sort(list);
                    final MainAdapter adapter = new MainAdapter(getActivity(), list);
                    recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                    listOfFile.setVisibility(View.GONE);
                    recycler.setVisibility(View.VISIBLE);
                    recycler.setAdapter(adapter);

                } else {
                    include.setVisibility(View.GONE);
                    include2.setVisibility(View.VISIBLE);
                    Collections.sort(list2);
                    final MainAdapterTwo adapterTwo = new MainAdapterTwo(getActivity(), list2);
                    recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                    listOfFile.setVisibility(View.GONE);
                    recycler.setVisibility(View.VISIBLE);
                    recycler.setAdapter(adapterTwo);

                }
            } else {
                getPhoneAndSpwork();
                Specia_workUIntent = Specia_workU;
                if (Specia_workUIntent.equals("أدويةمحلية")) {
                    include.setVisibility(View.VISIBLE);
                    include2.setVisibility(View.GONE);
                    Collections.sort(list);
                    final MainAdapter adapter = new MainAdapter(getActivity(), list);
                    recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                    listOfFile.setVisibility(View.GONE);
                    recycler.setVisibility(View.VISIBLE);
                    recycler.setAdapter(adapter);

                } else {
                    include.setVisibility(View.GONE);
                    include2.setVisibility(View.VISIBLE);
                    Collections.sort(list2);
                    final MainAdapterTwo adapterTwo = new MainAdapterTwo(getActivity(), list2);
                    recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                    listOfFile.setVisibility(View.GONE);
                    recycler.setVisibility(View.VISIBLE);
                    recycler.setAdapter(adapterTwo);

                }
            }

        }
    }

    public void getProductData(String type) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("product").child(type);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                nameProductL.add(dataSnapshot.getKey());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });

    }

    public void DelateProdut(String type) {

        for (int i = 0; i < nameProductL.size(); i++) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Query productQuery = ref.child("product").child(type).child(nameProductL.get(i)).child(userId);
            productQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        productSnapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("databaseErrorRemove", "onCancelled", databaseError.toException());
                }
            });
        }

    }

}

