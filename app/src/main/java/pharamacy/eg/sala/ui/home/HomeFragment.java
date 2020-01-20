package pharamacy.eg.sala.ui.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import pharamacy.eg.sala.AdapterListOfFile;
import pharamacy.eg.sala.Class.MainAdapter;
import pharamacy.eg.sala.Class.Product;
import pharamacy.eg.sala.Class.MainAdapterTwo;
import pharamacy.eg.sala.R;

import static com.yalantis.ucrop.UCropFragment.TAG;

public class HomeFragment extends Fragment {
    String userId, Specia_workU, excelPath;
    String Specia_workUIntent, lastDirectory;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private InterstitialAd mInterstitialAd;
    public ArrayList<Product> list = new ArrayList<Product>();
    public ArrayList<Product> list2 = new ArrayList<Product>();
    FloatingActionButton btuploadData;
    RecyclerView recycler;
    TextView textView;
    ListView listOfFile;
    private String[] FileNameStrings, FilePathStrings;
    View view;
    ArrayList<File> excelSheet;
    int cellsCount;
    int count = 0;
    ProgressDialog progressDialog;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Activity myActivity;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        myActivity = getActivity();
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        //ads
        startAd();
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        textView = view.findViewById(R.id.textUpload);
        Specia_workUIntent = getActivity().getIntent().getStringExtra("Specia_workU");
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
//        //TapTarget
//        showProg();
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        searchView();
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        listOfFile = view.findViewById(R.id.listOfFile);
        recycler = view.findViewById(R.id.recycle_main);
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
//      mStorage = FirebaseStorage.getInstance().getReference().child("images/").child(userId);
        if (user != null) {
            userId = user.getPhoneNumber();
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
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        excelPath = sharedPref.getString("excelpath", null);
        if (excelPath != null) {
            readExcelData(excelPath);
            if (Specia_workUIntent.equals("أدويةمحلية")) {
                final MainAdapter adapter = new MainAdapter(getActivity(), list);
                recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                listOfFile.setVisibility(View.GONE);
                recycler.setVisibility(View.VISIBLE);
                recycler.setAdapter(adapter);
                startAd();
            } else {
                final MainAdapterTwo adapterTwo = new MainAdapterTwo(getActivity(), list2);
                recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                listOfFile.setVisibility(View.GONE);
                recycler.setVisibility(View.VISIBLE);
                recycler.setAdapter(adapterTwo);
                startAd();
            }
        }
        //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
//        FileListerDialog fileListerDialog = FileListerDialog.createFileListerDialog(getActivity(), R.style.AppTheme);
//        fileListerDialog.setDefaultDir("/storage/emulated/0");
//        fileListerDialog.setFileFilter(FileListerDialog.FILE_FILTER.ALL_FILES);
//        fileListerDialog.setOnFileSelectedListener(new OnFileSelectedListener() {
//            @Override
//            public void onFileSelected(File file, String path) {
//                list.clear();
//                list2.clear();
//                readExcelData(path);
//                ////////////////////////////////
//
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                } else {
//                    Log.d("TAG", "The interstitial wasn't loaded yet.");
//                }
//                /////////////////////////////////
//
//                list.size();
//                if (Specia_workUIntent.equals("أدويةمحلية")) {
//                    MainAdapter adapter = new MainAdapter(getActivity(), list);
//                    adapter.updateList(list);
//                    recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    recycler.setAdapter(adapter);
//                } else {
//                    MainAdapterTwo adapterTwo = new MainAdapterTwo(getActivity(), list2);
//                    adapterTwo.updateList(list2);
//                    recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    recycler.setAdapter(adapterTwo);
//                }
//
//
//                editor.putString("excelpath", path);
//                editor.apply();
//                //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
//                //upload date product to fire base
//                if(Specia_workU.equals("أدويةمحلية")) {
//                    for (int p = 1; p < list.size(); p++) {
//                        databaseReference.child("product");
//                        int finalP = p;
//                        databaseReference.addChildEventListener(new ChildEventListener() {
//                            @Override
//                            //todo try and handel if crash whene discount == null
//                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                                writeNewPost(userId, list.get(finalP).getNameProduct(), list.get(finalP).getPrice(), list.get(finalP).getDiscount());
//                            }
//
//                            @Override
//                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////                            writeNewPost( userId, list.get(finalP ).getNameProduct(),  list.get(finalP).getPrice(), list.get(finalP).getDiscount());
//
//                            }
//
//                            @Override
//                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                            }
//
//                            @Override
//                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//                }else {
//                    for (int p = 1; p < list2.size(); p++) {
//                        databaseReference.child("product");
//                        int finalP = p;
//                        databaseReference.addChildEventListener(new ChildEventListener() {
//                            @Override
//                            //todo try and handel if crash whene discount == null
//                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                                writeNewPost1(userId, list2.get(finalP).getNameProduct(), list2.get(finalP).getPrice());
//
//                            }
//
//                            @Override
//                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////                            writeNewPost( userId, list.get(finalP ).getNameProduct(),  list.get(finalP).getPrice(), list.get(finalP).getDiscount());
//
//                            }
//
//                            @Override
//                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                            }
//
//                            @Override
//                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//                }
//            }
//        });

        ActionFloatingButton();
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
                    MainAdapter adapter = new MainAdapter(getActivity(), list);
                    recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter.getFilter().filter(s);
                    listOfFile.setVisibility(View.GONE);
                    recycle.setAdapter(adapter);
                } else {
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
                startAd();
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
                    //your business logic
                    deleteDialog.dismiss();
                    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
                    showProg("loading", "ثواني لحد ما نجيب الملفات ");
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
            final View deleteDialogView = factory.inflate(R.layout.alert_view2, null);
            final AlertDialog deleteDialog = new AlertDialog.Builder(getActivity()).create();
            deleteDialog.setView(deleteDialogView);
            deleteDialogView.findViewById(R.id.alert_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //your business logic
                    deleteDialog.dismiss();
                    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
                    showProg("loading", "ثواني لحد ما نجيب الملفات ");
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
                    String a = null, b = null, d = null;
                    //inner loop, loops through columns
                    for (int i = 0; i < cellsCount; i++) {
                        //handles if there are to many columns on the excel sheet.
                        if (i > 2) {
                            Log.e(TAG, "readExcelData: ERROR. Excel File Format is incorrect! ");
                            Toast.makeText(getActivity(), "محتوي الملف غير مدعوم ", Toast.LENGTH_LONG).show();
                            break;
                        } else {
                            String value = getCellAsString(row, i, formulaEvaluator);
                            String cellInfo = "r:" + r + "; c:" + i + "; v:" + value;
                            Log.d(TAG, "readExcelData: Data from row: " + cellInfo);
                            // if Conditional to handel excel sheet title or value is Empty or value equals null
                            if (value.equals("")) {
                            } else {
                                switch (i) {
                                    case 0:
                                        a = value;
                                        break;
                                    case 1:
                                        b = value;
                                        list2.add(new Product(a, b));
                                        break;
                                    case 2:
                                        d = value;
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

    public void ListFileClick() {
        listOfFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                showProg("loading", "ثواني بنقرا ملف " + FileNameStrings[i]);
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
                                }
                                list.size();
                                //showProductInList();
                                editor.putString("excelpath", lastDirectory);
                                editor.apply();
                                showProductInList();
//                               todo في مشلكة uploadProduct();
                                uploadProduct();
                            }
                        });

                    }
                }).start();
            }
        });
    }

    public ArrayList<File> readExcleFile(File root) {
        ArrayList<File> arrayList = new ArrayList<File>();
        File file3[] = root.listFiles();
        for (File files : file3) {
            if (files.isDirectory()) {
                arrayList.addAll(readExcleFile(files));
            } else {
                if (files.getName().endsWith(".xlsx")) {
                    arrayList.add(files);
                }
            }
        }
        return arrayList;
    }

    public void showProductInList() {
        if (Specia_workU.equals("أدويةمحلية")) {
            MainAdapter adapter = new MainAdapter(getActivity(), list);
            adapter.updateList(list);
            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            listOfFile.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
            recycler.setAdapter(adapter);
            progressDialog.dismiss();
        } else {
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
        databaseReference.child("product");
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
            for (int p = 1; p < list2.size(); p++) {

                databaseReference.child("product");
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

    private void writeNewPost(String userId, String nameProduct, String price, String discount) {
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
        databaseReference.updateChildren(childUpdates);

    }

    private void writeNewPost1(String userId, String nameProduct, String price) {
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
                databaseReference.updateChildren(childUpdates);
                break;
            case "مستلزمات":
                childUpdates.put("/product/" + "/" + Specia_workU + "/" + "/" + nameProduct + "/" + "/" + userId + "/", productValues1);
                databaseReference.updateChildren(childUpdates);
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
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("BFE5A6AC72AC4A402AFDA3209FDB660A").build());
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
        // TODO: Add adView to your view hierarchy.

    }

    public void showProg(String title, String message) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false); //To prevent the user dismiss progressDialog
        progressDialog.show();

    }

}

