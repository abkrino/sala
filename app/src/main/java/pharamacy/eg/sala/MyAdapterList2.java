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

public class MyAdapterList2 extends RecyclerView.Adapter<MyAdapterList2.MyViewHolder> {
    ArrayList<String> nameC;
    ArrayList<MyItemList> list;
    String nameProduct;
    String numoo;
    ArrayList<String> PhoneNumber;
    private Context context;

    public String getNumoo() {
        return numoo;
    }


    public MyAdapterList2(ArrayList<MyItemList> list, ArrayList<String> nameC, String nameProduct, ArrayList<String> PhoneNumber, Context context) {
        this.nameC = nameC;
        this.list = list;
        this.nameProduct = nameProduct;
        this.PhoneNumber = PhoneNumber;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapterList2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_child2, null);
        return new MyAdapterList2.MyViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nameCompany.setText(nameC.get(position));
        holder.priceData.setText("السعر :  ");
        holder.phoneNumber.setText(PhoneNumber.get(position));
        holder.price.setText(list.get(position).getPrice());
        holder.callIcon.setImageResource(R.drawable.ic_call_black);
        holder.checkProduct.setImageResource(R.drawable.ic_add_shopping_cart);
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
        public TextView priceData, discountData, price, discount, nameCompany, phoneNumber;
        public ImageView callIcon;
        public ImageView checkProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneNumber = itemView.findViewById(R.id.phoneNumber2);
            priceData = itemView.findViewById(R.id.priceData2);
            nameCompany = itemView.findViewById(R.id.nameCompany2);
            price = itemView.findViewById(R.id.priced2);
            callIcon = itemView.findViewById(R.id.callIcon2);
            checkProduct = itemView.findViewById(R.id.checkProduct2);
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
