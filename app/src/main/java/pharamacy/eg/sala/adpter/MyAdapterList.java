package pharamacy.eg.sala.adpter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pharamacy.eg.sala.MyItemList;
import pharamacy.eg.sala.R;
import pharamacy.eg.sala.payment.GetPay;
import pharamacy.eg.sala.sql.DataBaseHelper;

public class MyAdapterList extends RecyclerView.Adapter<MyAdapterList.MyViewHolder> {
    ArrayList<String> nameC;
    ArrayList<MyItemList> list;
    String nameProduct;
    static String resultPay;
    static int datePayDay;
    static int datePayMonth;
    double priceList, discountList;
    String numoo;
    private static Context context;
    private FirebaseUser user;
    static boolean result;
    String nameCompanyS, numberOfficeS, numberOfficeSID, numberpharmS;
    ArrayList<String> prouductname, priceS, discountS;
    private LayoutInflater factory;
    private View deleteDialogView;
    private AlertDialog deleteDialog;
    String date;
    static int month;
    static int day;
    DataBaseHelper mydb;

    public MyAdapterList(ArrayList<MyItemList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_child_header, null);
        factory = LayoutInflater.from(context);
        deleteDialogView = factory.inflate(R.layout.alert_go_app, null);
        deleteDialog = new AlertDialog.Builder(context).create();
        DatePicker pikPicker = new DatePicker(context);
        month = pikPicker.getMonth() + 1;
        day = pikPicker.getDayOfMonth();

        return new MyViewHolder(itemLayoutView);
    }

    public MyAdapterList() {
    }

    public ArrayList<String> getNameC() {
        return nameC;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//nameC.get(position)
        //todo stop here 14\1

//        Collections.sort(list);
        Collections.sort(list);
        holder.nameCompany.setText(" شركة : " + list.get(position).getNameCompany());
        holder.price.setText(list.get(position).getPrice() + "");
        holder.discount.setText(list.get(position).getDiscount() + "");
        holder.phoneNumber.setText(list.get(position).getPhoneNumber());
        holder.phoneNumberIdOffices.setText(list.get(position).getOwnerProduct());
        holder.priceData.setText("السعر :  ");
        holder.discountData.setText("الخصم :  ");
        holder.callIcon.setImageResource(R.drawable.ic_call_black);
        holder.checkProduct.setImageResource(R.drawable.ic_add_shopping_cart);
        nameProduct = list.get(position).getNameInList();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView priceData, discountData, price, discount, nameCompany, phoneNumber, phoneNumberIdOffices;
        public ImageView callIcon;
        public ImageView checkProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            priceData = itemView.findViewById(R.id.priceData);
            discountData = itemView.findViewById(R.id.discountData);
            nameCompany = itemView.findViewById(R.id.nameCompany);
            discount = itemView.findViewById(R.id.discountd);
            price = itemView.findViewById(R.id.priced);
            callIcon = itemView.findViewById(R.id.callIcon);
            checkProduct = itemView.findViewById(R.id.checkProduct);
            phoneNumberIdOffices = itemView.findViewById(R.id.phoneNumberIdOffices);
            prouductname = new ArrayList<>();
            priceS = new ArrayList<>();
            discountS = new ArrayList<>();
            mydb = new DataBaseHelper(context);
            getPharmNumber();
            callIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    numoo = phoneNumber.getText().toString();
                    intent.setData(Uri.parse("tel:" + numoo));
                    context.startActivity(intent);
                }
            });

            checkProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialog.setView(deleteDialogView);
                    deleteDialog.setCanceledOnTouchOutside(false);
                    deleteDialog.setCancelable(false);

                    SharedPreferences preferences = context.getSharedPreferences("order", context.MODE_PRIVATE);

                    if (checkPayment()) {
                        addOrder();
                    } else {
                        deleteDialog.show();
                        deleteDialogView.findViewById(R.id.btPay).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteDialog.dismiss();
                                Toast.makeText(context, "من فضلك أضف بيانات البطاقة ", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(context, GetPay.class).putExtra("phoneNumberPharmacy", numberpharmS).putExtra("type", "pharmacies");
                                context.startActivity(intent);

                            }
                        });
                        deleteDialog.findViewById(R.id.alert_bt).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteDialog.dismiss();
                            }
                        });
                    }

                }
            });

        }


        public void addOrder() {

            nameCompanyS = nameCompany.getText().toString();
            priceList = Double.parseDouble(price.getText().toString());
            discountList = Double.parseDouble(discount.getText().toString());
            numberOfficeSID = phoneNumberIdOffices.getText().toString();
            numberOfficeS = phoneNumber.getText().toString();
            date = "" + month + "\\" +day  + "";
            //            pr*ouductname.add(nameProduct);
//            priceS.add(priceList);
//            discountS.add(discountList);
            Boolean result = mydb.insertData(nameProduct, nameCompanyS, priceList, discountList, numberOfficeS, numberOfficeSID, numberpharmS, date);
            if (result) {
                Toast.makeText(context, "تم اضافة " + nameProduct, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "لم يتم اضافة " + nameProduct, Toast.LENGTH_SHORT).show();
            }

//            Order order = new Order(prouductname, priceS, discountS);

//            new Thread(new Runnable() {
//                @Override
//                public void run() {

//                    reference = FirebaseDatabase.getInstance().getReference();
//                    reference.child("users").child("pharmacies").child(numberpharmS).child("order").child(numberOfficeS).child(date).child("purchase order").child("name product").setValue(nameProduct);
//                    reference.child("users").child("pharmacies").child(numberpharmS).child("order").child(numberOfficeS).child(date).child("purchase order").child("price product").setValue(priceList);
//                    reference.child("users").child("pharmacies").child(numberpharmS).child("order").child(numberOfficeS).child(date).child("purchase order").child("discount product").setValue(discountList);

            //                    reference.child("users").child("pharmacies").child(numberpharmS).child("order").child(numberOfficeS).child("date");


            //                    reference.child("users").child("pharmacies").child(numberpharmS).child("order").child("purchase order").child(numberOfficeS).child("date day").setValue(day);
//                    reference.child("users").child("pharmacies").child(numberpharmS).child("order").child("purchase order").child(numberOfficeS).child("date month").setValue(month);

//                    reference.child("order").child("sales order").child(numberpharmS).setValue(order);
//                    reference.child("order").child("sales order").child(numberpharmS).child("date day").setValue(day);
//                    reference.child("order").child("sales order").child(numberpharmS).child("date month").setValue(month);
//                }
//            }).start();
//            Toast.makeText(context, "تم انشاء امر شراء بعدد" + prouductname.size() + " صنف من شركة " + nameCompanyS, Toast.LENGTH_SHORT).show();

        }

        public void getPharmNumber() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    numberpharmS = user.getPhoneNumber();

                }
            }).start();

        }
    }


    public static boolean checkPayment() {
        String defult = "def";
        SharedPreferences sharedPref = context.getSharedPreferences("my payment", Context.MODE_PRIVATE);
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

                if (datePayMonth == month && datePayDay==day ) {
                    result = false;
                }else if(datePayMonth==month&&datePayDay<day){
                    result = false;
                }else if(datePayMonth==month&&datePayDay>day){
                    result = true;
                } else if(datePayMonth<month){
                    result = false;
                }else if(datePayMonth>month){
                    result = true;
                }
                break;
        }

        return result;
    }


}
