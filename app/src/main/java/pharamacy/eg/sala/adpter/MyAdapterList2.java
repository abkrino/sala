package pharamacy.eg.sala.adpter;

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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pharamacy.eg.sala.MyItemList;
import pharamacy.eg.sala.R;
import pharamacy.eg.sala.payment.GetPay;
import pharamacy.eg.sala.sql.DataBaseHelper;

public class MyAdapterList2 extends RecyclerView.Adapter<MyAdapterList2.MyViewHolder> {
    ArrayList<String> nameC;
    ArrayList<MyItemList> list;
    String nameProduct, resultPay;
    String numoo;
    private Context context;
    String nameCompanyS, numberOfficeS,numberOfficeSId, numberpharmS;
    ArrayList<String> prouductname, priceS, discountS;
    boolean result;
    int datePayDay, datePayMonth;
    double priceList, discountList;
    private LayoutInflater factory;
    private View deleteDialogView;
    private android.app.AlertDialog deleteDialog;
    DataBaseHelper mydb;
    private FirebaseUser user;

    public String getNumoo() {
        return numoo;
    }


    public MyAdapterList2(ArrayList<MyItemList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapterList2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_child2, null);
        factory = LayoutInflater.from(context);
        deleteDialogView = factory.inflate(R.layout.alert_go_app, null);
        deleteDialog = new android.app.AlertDialog.Builder(context).create();

        return new MyAdapterList2.MyViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Collections.sort(list);

        holder.nameCompany.setText(list.get(position).getNameCompany());
        holder.priceData.setText("السعر :  ");
        holder.phoneNumber.setText(list.get(position).getPhoneNumber());
        holder.price.setText(list.get(position).getPrice() + "");
        holder.phoneNumberIdOffices.setText(list.get(position).getOwnerProduct());
        holder.callIcon.setImageResource(R.drawable.ic_call_black);
        holder.checkProduct.setImageResource(R.drawable.ic_add_shopping_cart);
        nameProduct = list.get(position).getNameInList();
    }

    public MyAdapterList2() {
    }

    public ArrayList<String> getNameC() {
        return nameC;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView priceData, nameProuducts, price, discount, nameCompany, phoneNumber, phoneNumberIdOffices;
        public ImageView callIcon;
        public ImageView checkProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneNumber = itemView.findViewById(R.id.phoneNumber2);
            priceData = itemView.findViewById(R.id.priceData2);
            nameCompany = itemView.findViewById(R.id.nameCompany2);
            price = itemView.findViewById(R.id.priced2);
            callIcon = itemView.findViewById(R.id.callIcon2);
            phoneNumberIdOffices = itemView.findViewById(R.id.phoneNumberIdOffices2);
            checkProduct = itemView.findViewById(R.id.checkProduct2);
            mydb = new DataBaseHelper(context);
            prouductname = new ArrayList<>();
            priceS = new ArrayList<>();
            discountS = new ArrayList<>();
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
                    String name = checkNameCompany();
                    SharedPreferences preferences = context.getSharedPreferences("order", context.MODE_PRIVATE);
                    if (checkPayment()) {
                        addOrder(name);
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

        private String checkNameCompany() {
            String fileName = "order";
            SharedPreferences sharedPref = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            final String name = nameCompany.getText().toString();
            editor.putString("nameCompany", name);

            editor.apply();
            return name;
        }

        public void addOrder(String name) {

            nameCompanyS = name;
            priceList = Double.parseDouble(price.getText().toString());
            numberOfficeS = phoneNumber.getText().toString();
            numberOfficeSId = phoneNumberIdOffices.getText().toString();
            DatePicker pikPicker = new DatePicker(context);
            int month = pikPicker.getMonth() +1;
            int day = pikPicker.getDayOfMonth();
            String date = "" + day + "\\" + month + "";
            //            prouductname.add(nameProduct);
//            priceS.add(priceList);
//            discountS.add(discountList);
            Boolean result = mydb.insertData(nameProduct, nameCompanyS, priceList, discountList, numberOfficeS,numberOfficeSId,numberpharmS, date);
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
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        }
    }


    public boolean checkPayment() {
        String defult = "def";
        SharedPreferences sharedPref = context.getSharedPreferences("my payment", Context.MODE_PRIVATE);
        resultPay = sharedPref.getString("resultPay", defult);
        switch (resultPay) {
            case "def":
                result = false;
                checkPayment();
                break;
            case "failed":
                result = false;
                break;
            case "accept":
                result = true;
//                Toast.makeText(context, "انت تستمتع بالمزايا المدفوعة ", Toast.LENGTH_SHORT).show();
                datePayDay = sharedPref.getInt("datePayDay", 0);
                datePayMonth = sharedPref.getInt("datePayMonth", 0);
                break;
        }

        return result;
    }


}
