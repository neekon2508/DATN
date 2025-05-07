package com.example.flashcard.drawer;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcard.R;

public class SupportFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_support, container, false);
        setupButton(layout);
        return layout;
    }

    private void setupButton(View view) {
        ImageView donateView = view.findViewById(R.id.donateView);
        TextView btnDonate = view.findViewById(R.id.btn_donate);
        TextView btnFeedback = view.findViewById(R.id.btn_feedback);
        btnDonate.setOnClickListener(v -> {
            donateView.setVisibility(View.VISIBLE);
        });
        btnFeedback.setOnClickListener( v->{
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, new String[] {"nghia250803@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "I want to describe about ...");

            try {
                startActivity(Intent.createChooser(emailIntent, "Choose an email client"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        donateView.setOnClickListener(v->{
            donateView.setVisibility(View.INVISIBLE);
        });
    }
}