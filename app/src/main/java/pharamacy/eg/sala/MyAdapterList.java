package pharamacy.eg.sala;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdapterList extends RecyclerView.Adapter<MyAdapterList.MyViewHolder> {
    ArrayList<String> nameC;
    ArrayList<MyItemList> list;
    String nameProduct;
    String numoo;
    ArrayList<String> PhoneNumber;
    private Context context;
    public String getNumoo() {
        return numoo;
    }


    public MyAdapterList(ArrayList<MyItemList> list, ArrayList<String> nameC, String nameProduct, ArrayList<String> PhoneNumber, Context context) {
        this.nameC = nameC;
        this.list = list;
        this.nameProduct = nameProduct;
        this.PhoneNumber = PhoneNumber;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_child_header, null);
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
        holder.nameCompany.setText(" شركة : "+nameC.get(position));
        holder.price.setText(list.get(position).getPrice());
        holder.discount.setText(list.get(position).getDiscount());
        holder.phoneNumber.setText(PhoneNumber.get(position));
        holder.priceData.setText("السعر :  ");
        holder.discountData.setText("الخصم :  ");
        holder.callIcon.setImageResource(R.drawable.ic_call_black);
        holder.checkProduct.setImageResource(R.drawable.ic_add_shopping_cart);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView priceData, discountData, price, discount, nameCompany, phoneNumber;
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
                final String nameCompanyorder, prouductname;
                nameCompanyorder = nameCompany.getText().toString();
                prouductname = nameProduct;
                ArrayList<String> productsOrder = new ArrayList<>();
                if (nameCompany.getText().toString().equals(nameCompanyorder)) {
                    productsOrder.add(prouductname);
                }
                Map<String, ArrayList<String>> order = new HashMap<>();
                order.put(nameCompanyorder, productsOrder);
                LayoutInflater factory = LayoutInflater.from(context);
                final View deleteDialogView = factory.inflate(R.layout.alert_go_app, null);
                final AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
                deleteDialog.setView(deleteDialogView);
                deleteDialog.show();
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.blink);
                deleteDialog.findViewById(R.id.icoon_image).startAnimation(animation);
                deleteDialogView.findViewById(R.id.alert_bt).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDialog.dismiss();
                        Toast.makeText(context, "لسا معملناش تطبيق مدفوع !!", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
}


}


//    public void addToPayCar(View view) {
//        String text1 = "";
//        //todo creathash map and insert it in sqlLite and also get the order from sql to OrderClass
//        for (int i = 0; i < ProductInfo.getChildCount(); i++) {
//            View view1 = ProductInfo.getChildAt(i); // This will give you entire row(child) from RecyclerView
//            int x=    ProductInfo.getChildCount();
//            if (view != null) {
//                TextView textView = (TextView) view1.findViewById(R.id.nameCompany);
//                text1 = textView.getText().toString();
//
//            }
//        }
//        nameComMap = text1;
//
//        productMap.add(nameInList);
//        intent1 = new Intent(SearchProduct.this, ProductOrder.class);
//        intent1.putExtra("name", nameComMap);
//        intent1.putStringArrayListExtra("pro", productMap);
//
//        Toast.makeText(this, "كس ام البرمجة واللي عيزهااااا" + text1, Toast.LENGTH_LONG).show();
//    }