package pharamacy.eg.sala.ui.slideshow;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pharamacy.eg.sala.R;

public class SlideshowFragment extends Fragment {
    TextView header, titleEmail, email, titleNumber, number;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        header = view.findViewById(R.id.header);
        titleEmail = view.findViewById(R.id.titleEmail);
        email = view.findViewById(R.id.email);
        titleNumber = view.findViewById(R.id.titleNumber);
        number = view.findViewById(R.id.Number);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("email"));
                String [] s ={"sala.pharmacy@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL,s);
                intent.putExtra(Intent.EXTRA_SUBJECT,"Enter subject");
                intent.putExtra(Intent.EXTRA_TEXT,"Enter your email body");
                intent.setType("massage/rfc822");
                Intent choser = Intent.createChooser(intent,"launch Email");
                startActivity(choser);

            }
        });
        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);

                intent.setData(Uri.parse("tel:" + "01554046584"));

                startActivity(intent);
            }
        });
    }
}