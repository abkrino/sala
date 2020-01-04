package pharamacy.eg.sala;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pharamacy.eg.sala.Class.Product;

public class MainAdapterTwo extends RecyclerView.Adapter<MainAdapterTwo.ViewHolder> implements Filterable {
    LayoutInflater mInflater;
    Context context;
    MainAdapterTwo.ItemClickListener mClickListener;
    ArrayList<Product> list;
    ArrayList<Product> prouductListFiltered = new ArrayList<>();

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
        holder.name.setText(list.get(position).getName());
        holder.price.setText(list.get(position).getPrice());

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


        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameC);
            price = itemView.findViewById(R.id.priceC);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

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

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (product.getName().toLowerCase().contains(charString.toLowerCase())) {
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

}
