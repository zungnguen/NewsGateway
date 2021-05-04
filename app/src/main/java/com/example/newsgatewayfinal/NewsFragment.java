package com.example.newsgatewayfinal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;


public class NewsFragment extends Fragment {

    final String TAG = "ArticleFragment";
    public static final String a1 = "a1";
    public static final String a2 = "a2";
    public static final String a3 = "a3";
    public static final String a4 = "a4";
    String finalDate;
    public static final String a5 = "a5";
    public static final String a6 = "a6";
    public static final String a7 = "a7";
    String d2 = "";
    private TextView authornametext, title, description, pagecount;
    private ImageView image;
    Fragment mMyFragment;

    public static final NewsFragment newInstance(String message, String message1, String message2, String message3, String message4, String message5, String message6) {
        NewsFragment f = new NewsFragment();

        Bundle bdl = new Bundle(6);
        if (message != null) {
            bdl.putString(a1, message);
        }
        if (message1 != null) {
            bdl.putString(a2, message1);
        }
        if (message2 != null) {
            bdl.putString(a3, message2);
        }
        if (message3 != null) {
            bdl.putString(a4, message3);
        }
        if (message4 != null) {
            bdl.putString(a5, message4);
        }
        if (message5 != null) {
            bdl.putString(NewsFragment.a6, message5);
        }
        if (message6 != null) {
            bdl.putString(a7, message6);
        }
        f.setArguments(bdl);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onRestoreInstanceState(savedInstanceState);
        final String message = getArguments().getString(a1);;
        String message1 = getArguments().getString(a2);
        String message2 = getArguments().getString(a3);
        String message3 = getArguments().getString(a4);
        String message4 = getArguments().getString(a5);
        final String message5 = getArguments().getString(a6);
        String message6 = getArguments().getString(a7);
        message.trim();
        message1.trim();
        message2.trim();
        message3.trim();
        message6.trim();

        setRetainInstance(true);
        View v = inflater.inflate(R.layout.fragmentlayout, container, false);
        authornametext = v.findViewById(R.id.Author);
        image = v.findViewById(R.id.photo);
        title = v.findViewById(R.id.Title);
        description = v.findViewById(R.id.Description);
        pagecount = v.findViewById(R.id.counter);
        finalDate = message4;



        if(message2.trim()!= null && finalDate!= null)
        {
            authornametext.setText(finalDate + "\n\n\n" +message2);
        }
        else if(message2.trim() == null && finalDate!= null)
        {
            authornametext.setText(finalDate);
        }
        else if(message2.trim()!= null && finalDate== null)
        {
            authornametext.setText("");
        }
        else if (message2.trim()== null && finalDate== null)
        {
            authornametext.setText("No Information Available!");
        }
        pagecount.setText(message6);



        if (message1 != null) {

            final String imageurl = message1;

            Picasso picasso = new Picasso.Builder(this.getContext()).listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                {

                    final String changedUrl = imageurl.replace("http:", "https:");
                    picasso.load(changedUrl).error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder).into(image);
                }
            }).build();
            picasso.load(imageurl).error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder).into(image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Uri uri = Uri.parse(message5);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
        if(!message.equals(null))
        {
            title.setText(message);
            title.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Uri uri = Uri.parse(message5);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
//
        }

        else {
            title.setText("No Information Available!");
        }
        if(!message3.equals(null)) {
            description.setText(message3);
            description.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Uri uri = Uri.parse(message5);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
        else {
            description.setText("No Information Available!");
        }
        return v;

    }



    private void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState!=null)
        {

            title.setText(savedInstanceState.getString("title"));
            description.setText(savedInstanceState.getString("description"));
            pagecount.setText(savedInstanceState.getString("pcount"));
            authornametext.setText(savedInstanceState.getString("author"));
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            mMyFragment = (NewsFragment) manager.getFragment(savedInstanceState, "myFragment");

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        FragmentManager manager = getFragmentManager();
        if(mMyFragment != null) {
            manager.putFragment(outState, "myFragment", mMyFragment);
        }
        outState.putString("author", authornametext.getText().toString());
        outState.putString("title", title.getText().toString());
        outState.putString("description", description.getText().toString());
        outState.putString("pcount", pagecount.getText().toString());
    }
}

