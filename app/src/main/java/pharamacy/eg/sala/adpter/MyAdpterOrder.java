package pharamacy.eg.sala.adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import pharamacy.eg.sala.Order;
import pharamacy.eg.sala.ProductOrder;
import pharamacy.eg.sala.R;

public class MyAdpterOrder extends BaseExpandableListAdapter {
    private Context ctx;
    private HashMap<String, List<String>> childTitles;
    private List<String> HeadrerTitles;
    private List<String> nameCompay;


    public MyAdpterOrder(Context ctx, HashMap<String, List<String>> childTitles, List<String> headrerTitles, List<String> nameCompay) {
        this.ctx = ctx;
        this.childTitles = childTitles;
        HeadrerTitles = headrerTitles;
        this.nameCompay = nameCompay;
    }

    @Override
    public int getGroupCount() {
        return HeadrerTitles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childTitles.get(HeadrerTitles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return HeadrerTitles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childTitles.get(HeadrerTitles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = (String) "اوامر شراء بتاريخ  " + this.getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_card, null);
        }
        TextView txt = convertView.findViewById(R.id.idTitle);

        txt.setText(title);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String title = (String) this.getChild(groupPosition, childPosition);
//        nameCompay.get(childPosition)
//        String nameCom = getNameCompany(title);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.name_card, null);

        }

        TextView textView = convertView.findViewById(R.id.idchild);

        textView.setText(nameCompay.get(childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

//    public static String getNameCompany(String number ) {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(number).child("nameU");
//
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                name = dataSnapshot.getValue().toString();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        return name;
//    }
}
