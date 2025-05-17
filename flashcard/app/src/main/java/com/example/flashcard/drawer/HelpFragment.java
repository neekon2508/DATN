package com.example.flashcard.drawer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.flashcard.R;

public class HelpFragment extends Fragment {

    @Override
    public void onViewCreated( View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true); // Cho phép Fragment cập nhật menu
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear(); // Xóa menu hiện tại để tránh bị trùng lặp
        inflater.inflate(R.menu.menu_default, menu); // Thêm menu mới
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View layout = inflater.inflate(R.layout.fragment_help, container, false);
        setupButton(layout);
        return layout;
    }


    private void setupButton(View view) {
        TextView btn_use = view.findViewById(R.id.btn_use);
        btn_use.setOnClickListener(v->{
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.ankiweb.net/getting-started.html")));
        });
        TextView btn_community = view.findViewById(R.id.btn_community);
        btn_community.setOnClickListener(v->{
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://forums.ankiweb.net/")));
        });
        TextView btn_privacy = view.findViewById(R.id.btn_privacy);
        btn_privacy.setOnClickListener(v->{
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://ankiweb.net/account/privacy")));
        });
    }
}