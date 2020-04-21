package pharamacy.eg.sala.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import pharamacy.eg.sala.Class.Product;
import pharamacy.eg.sala.R;

public class AdapterPro extends ArrayAdapter {
    private ArrayList<Product> productList;

    public AdapterPro(@NonNull Context context, ArrayList<Product> productList) {
        super(context, R.layout.main_cell,R.id.nameProuduct);
        this.productList = productList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.main_cell,parent,false);
        TextView nameProuduct = view.findViewById(R.id.nameProuduct);
        TextView price = view.findViewById(R.id.priceC);
        TextView discountC = view.findViewById(R.id.discountC);
        nameProuduct.setText(productList.get(position).getNameProduct());
        price.setText(productList.get(position).getPrice()+"");
        discountC.setText(productList.get(position).getDiscount()+"");
    return view;
    }
}
