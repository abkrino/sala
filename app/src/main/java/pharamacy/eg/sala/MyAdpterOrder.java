package pharamacy.eg.sala;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdpterOrder extends RecyclerView.Adapter<MyAdpterOrder.MyViewHolderOrder> {
    String nameCompanyOrder;
    ArrayList<Order> nameOfProduct;
    private Context context;
    String numberOffice;
    int numberOrder;
    //todo فاضل رقم الاورد ونكمل الباقي
    public MyAdpterOrder(String nameCompanyOrder, ArrayList<Order> nameOfProduct, String numberOffice,Context context) {
        this.nameCompanyOrder = nameCompanyOrder;
        this.nameOfProduct = nameOfProduct;
        this.numberOffice =numberOffice;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolderOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card, null);
        return new MyViewHolderOrder(itemLayoutView);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderOrder holder, int position) {
    holder.nameCompany.setText("أمر شراء من شركة :"+nameCompanyOrder);
    holder.nameProduct.setText("أسم الصنف");
    holder.price.setText("السعر");
    holder.discount.setText("الخصم");
    holder.priceData.setText(nameOfProduct.get(position).getPrice());
    holder.discountData.setText(nameOfProduct.get(position).getDiscount());
    holder.nameProductData.setText(nameOfProduct.get(position).getNameProduct());
    holder.phoneNumber.setText(numberOffice);
    holder.numberOrder.setText(numberOrder);
    }

    @Override
    public int getItemCount() {
        return nameOfProduct.size();
    }
    class MyViewHolderOrder extends RecyclerView.ViewHolder {
        public TextView priceData, discountData, price, discount, nameCompany, phoneNumber,nameProduct,nameProductData,numberOrder;
        public ImageView callIcon;


        public MyViewHolderOrder(@NonNull View itemView) {
            super(itemView);
            nameCompany = itemView.findViewById(R.id.nameCompanyOrder);
            discount = itemView.findViewById(R.id.discountTitel);
            discountData = itemView.findViewById(R.id.discountOrder);
            price = itemView.findViewById(R.id.priceTitel);
            priceData = itemView.findViewById(R.id.priceOrder);
            callIcon = itemView.findViewById(R.id.callIconOrder);
            nameProduct = itemView.findViewById(R.id.nameProuductTitel);
            nameProductData=itemView.findViewById(R.id.nameProuductOrder);
            phoneNumber = itemView.findViewById(R.id.phoneNumberOrder);
            numberOrder= itemView.findViewById(R.id.numberOrder);
            callIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    numberOffice= phoneNumber.getText().toString();
                    intent.setData(Uri.parse("tel:" + numberOffice));
                    context.startActivity(intent);
                }
            });

        }
    }
}
