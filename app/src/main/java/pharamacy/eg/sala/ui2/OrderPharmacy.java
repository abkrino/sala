package pharamacy.eg.sala.ui2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pharamacy.eg.sala.Product_order;
import pharamacy.eg.sala.R;
import pharamacy.eg.sala.SearchProduct;
import pharamacy.eg.sala.adpter.MyAdpterOrder;
import pharamacy.eg.sala.payment.GetPay;
import pharamacy.eg.sala.sql.DataBaseHelper;

public class OrderPharmacy extends Fragment {

    private ExpandableListView myorderDate;
    DataBaseHelper myDataBase;
    HashMap<String, List<String>> myOrderOwner;
    List<String> numberOffices;
    List<String> nameCompanyy;

    boolean result;

    private LayoutInflater factory;

    private View deleteDialogView;
    private AlertDialog deleteDialog;

    String numberOffice, resultPay;
    Cursor res;
    String date, numberpharmS, nameCompany;
    MyAdpterOrder myAdpterOrder;
    DatabaseReference reference;
    private FirebaseUser user;
    int datePayDay, datePayMonth;
    static String name;
    int month, day;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order_pharmacy, container, false);
        myorderDate = container.findViewById(R.id.myorderDate);
        factory = LayoutInflater.from(getActivity());
        deleteDialogView = factory.inflate(R.layout.alert_go_app, null);
        deleteDialog = new AlertDialog.Builder(getActivity()).create();

        DatePicker pikPicker = new DatePicker(getActivity());
        month = pikPicker.getMonth() + 1;
        day = pikPicker.getDayOfMonth();
        if (!checkPayment()) {
            deleteDialog.setView(deleteDialogView);
            deleteDialog.setCanceledOnTouchOutside(false);
            deleteDialog.setCancelable(false);
            deleteDialog.show();
            deleteDialogView.findViewById(R.id.btPay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialog.dismiss();
                    Toast.makeText(getActivity(), "من فضلك أضف بيانات البطاقة ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), GetPay.class).putExtra("phoneNumberPharmacy", numberpharmS).putExtra("type", "pharmacies");
                    getActivity().startActivity(intent);

                }
            });
            deleteDialog.findViewById(R.id.alert_bt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    deleteDialog.dismiss();

                    startActivity(new Intent(getActivity(), SearchProduct.class));
                    getActivity().finish();
                    //                    Fragment fragment = new Home_pharm();
//
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.fragment_container, fragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();

                }
            });

        } else {

        }

        calling();
        return root;
    }

    private void calling() {

        myDataBase = new DataBaseHelper(getActivity());
        myOrderOwner = new HashMap<>();
        numberOffices = new ArrayList<>();
        nameCompanyy = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        numberpharmS = user.getPhoneNumber();
        res = myDataBase.getAllData();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myorderDate = view.findViewById(R.id.myorderDate);

        checkDateOrderAndCompany();
        numberOffices = new ArrayList<>(myOrderOwner.keySet());

        setReceyOrder(getActivity(), myOrderOwner, numberOffices ,nameCompanyy);
        myorderDate.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String t = (String) myorderDate.getExpandableListAdapter().getGroup(groupPosition);
                String t1 = (String) myorderDate.getExpandableListAdapter().getChild(groupPosition, childPosition);
                startActivity(new Intent(getContext(), Product_order.class).putExtra("dateOrder", t).putExtra("numberOwner", t1).putExtra("numberPharmacy", numberpharmS).putExtra("type","Pharmacy"));


//                showdialog(getProductCompany(t, t1));

                return false;
            }

        });

    }

    public void checkDateOrderAndCompany() {
        if (res != null && res.getCount() > 0) {

            if (res.moveToFirst()) {
                setFirstProduct();

                while (res.moveToNext()) {
                    if (date.equals(res.getString(8))) {
                        if (!numberOffice.equals(res.getString(6))) {
                            //change number
                            numberOffices = myOrderOwner.get(date);
                            numberOffice = res.getString(6);
                            nameCompany = res.getString(2);
                            if (!numberOffices.contains(numberOffice)) {
                                numberOffices.add(numberOffice);
                                nameCompanyy.add(nameCompany);
                                date = res.getString(8);
                                myOrderOwner.put(date, numberOffices);
                            }
                            date = res.getString(8);


                        }
                    } else {

//change date and number
//todo change numberoffices to name offices
                        numberOffices = new ArrayList<>();
                        date = res.getString(8);
                            numberOffice = res.getString(6);
                        numberOffices.add(numberOffice);
                        myOrderOwner.put(date, numberOffices);
                   }

                }

            }

        }
    }

    public void setFirstProduct() {
        nameCompany = res.getString(2);
        numberOffice = res.getString(6);
        date = res.getString(8);
        numberOffices.add(numberOffice);
        nameCompanyy.add(nameCompany);
        myOrderOwner.put(date, numberOffices);

    }

    public void setReceyOrder(Context context, HashMap<String, List<String>> header, List<String> child ,List<String> nameCompany) {

        myAdpterOrder = new MyAdpterOrder(context, header, child , nameCompany);
        myorderDate.setAdapter(myAdpterOrder);
        myorderDate.setVisibility(View.VISIBLE);
    }

    public boolean checkPayment() {
        String defult = "def";
        SharedPreferences sharedPref = getActivity().getSharedPreferences("my payment", Context.MODE_PRIVATE);
        resultPay = sharedPref.getString("resultPay", defult);
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

        return result;
    }
}

