package pharamacy.eg.sala.Class;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pharamacy.eg.sala.R;

public class MainAdapterTwo extends RecyclerView.Adapter<MainAdapterTwo.ViewHolder> implements Filterable {
    LayoutInflater mInflater;
    String finalNameproduct,nameproduct;
    String userId;
    String  Specia_workU;
    Context context;
    MainAdapterTwo.ItemClickListener mClickListener;
    ArrayList<Product> list;
    ArrayList<Product> prouductListFiltered = new ArrayList<>();
    int poosition;
    int indx_row = -1;

    // data is passed into the constructor
    public MainAdapterTwo(Context context, ArrayList<Product> list) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
    }

    // inflates the row layout from xml when needed
    @Override
    public MainAdapterTwo.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view2 = mInflater.inflate(R.layout.main_cell2, parent, false);

        return new MainAdapterTwo.ViewHolder(view2);
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MainAdapterTwo.ViewHolder holder, int position) {
        holder.name.setText(list.get(position).getNameProduct());
        holder.price.setText(list.get(position).getPrice());
        holder.row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                indx_row = position;
                nameproduct = holder.name.getText().toString();
                if (position == 0&& (nameproduct.equals("أسم الصنف") || nameproduct.equals("اسم الصنف") || nameproduct.equals("إسم الصنف"))) {
                    Toast.makeText(context, "ممكن ترفع قائمة جديده", Toast.LENGTH_SHORT).show();
                    indx_row = -1;
                }
                notifyDataSetChanged();
                return true;
            }
        });
        if (indx_row == position) {
            holder.btDelete.setVisibility(View.VISIBLE);
            holder.row.setBackgroundResource(R.color.Black_transparent_black_hex_2);
        } else {
            holder.btDelete.setVisibility(View.GONE);
            holder.row.setBackgroundColor(Color.WHITE);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return list.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView price;
        LinearLayout row;
        ImageView btDelete;


        ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameC);
            price = itemView.findViewById(R.id.priceC);
            btDelete = itemView.findViewById(R.id.btDelete);
            row = itemView.findViewById(R.id.row2);
            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     nameproduct = name.getText().toString();
                    if (nameproduct.equals("أسم الصنف") || nameproduct.equals("اسم الصنف") || nameproduct.equals("إسم الصنف")) {
                        Toast.makeText(context, "ممكن ترفع قائمة جديدة ", Toast.LENGTH_LONG).show();
                    }
                 if (nameproduct.contains(".")) {
                    nameproduct = nameproduct.replace('.', ',');
                } else if (nameproduct.contains("#")) {
                    nameproduct = nameproduct.replace('#', '\t');
                } else if (nameproduct.contains("$")) {
                    nameproduct = nameproduct.replace('$', '\t');
                } else if (nameproduct.contains("[")) {
                    nameproduct = nameproduct.replace('[', '\t');
                } else if (nameproduct.contains("]")) {
                    nameproduct = nameproduct.replace(']', '\t');
                } else if (nameproduct.contains("/")) {
                    nameproduct = nameproduct.replace('/', '\\');
                }
                    finalNameproduct = nameproduct;
                    new AlertDialog.Builder(context).setTitle("هل تريد حذف " + nameproduct).setIcon(android.R.drawable.ic_menu_delete).setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            userId = user.getPhoneNumber();
                            Specia_workU = ((Activity)context).getIntent().getStringExtra("Specia_workU");
                            if(userId!=null&&Specia_workU!=null){
                                removeAt(getAdapterPosition());
                                deleteProductFromFireBase(Specia_workU);
                            }

//                            if(getSpecia_work().equals("أدوية مستوردة")){
//                            }else {
//                                deleteProductFromFireBase(getSpecia_work());
//                            }
                        }
                    }).setNegativeButton("لا", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Toast.makeText(context, " تم  الغاء حذف  " + finalNameproduct, Toast.LENGTH_LONG).show();
                        }
                    }).show();

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    indx_row = -1;
                    poosition = getAdapterPosition();
                    btDelete.setVisibility(View.GONE);
                    row.setBackgroundColor(itemView.getResources().getColor(R.color.White_White));

                }
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    int getItem(int position) {
        return position;
    }

    // allows clicks events to be caught
    void setClickListener(MainAdapterTwo.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    prouductListFiltered = list;
                } else {
                    ArrayList<Product> filteredList = new ArrayList<>();
                    for (Product product : list) {

                        // nameProduct match condition. this might differ depending on your requirement
                        // here we are looking for nameProduct or phone number match
                        if (product.getNameProduct().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(product);
                        }
                    }

                    prouductListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = prouductListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                prouductListFiltered = (ArrayList<Product>) filterResults.values;
                list = prouductListFiltered;
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }
    public void updateList(ArrayList<Product> list) {
        notifyDataSetChanged();
    }
    public void removeAt(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }


    public void deleteProductFromFireBase(String typeOfWork){
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        userId = user.getPhoneNumber();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query productQuery = ref.child("product").child(typeOfWork).child(finalNameproduct).child(userId);
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
        Toast.makeText(context, " تم الحذف  " + finalNameproduct, Toast.LENGTH_LONG).show();
        indx_row = -1;

    }
}
