package pharamacy.eg.sala.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import pharamacy.eg.sala.R;

public class AdapterListOfFile extends ArrayAdapter {
    int Image ;
    private String [] titleArray,pathArray ;
    Context context;

    public AdapterListOfFile(Context context, String [] titleArray ,String[]pathArray) {
    super(context, R.layout.row_of_file,R.id.titleFile,titleArray);
        this.context = context;
        this.titleArray = titleArray;
        this.pathArray = pathArray;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater =(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.row_of_file,parent,false);
        ImageView imageExcel = row.findViewById(R.id.image_excel);
        TextView  title = row.findViewById(R.id.titleFile);
        TextView path = row.findViewById(R.id.pathFile);
        imageExcel.setImageResource(R.drawable.ic_microsoft_excel);
        title.setText(titleArray[position]);
        path.setText(pathArray[position]);
        return row;
    }
}
