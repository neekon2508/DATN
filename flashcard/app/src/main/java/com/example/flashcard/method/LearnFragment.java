package com.example.flashcard.method;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.flashcard.R;


public class LearnFragment extends Fragment {

    private AnimatorSet front_anim;
    private AnimatorSet back_anim;
    private boolean isFront = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_learn, container, false);
        float scale = layout.getContext().getResources().getDisplayMetrics().density;

        TextView card_front = (TextView) layout.findViewById(R.id.card_front);
        TextView card_back = (TextView) layout.findViewById(R.id.card_back);
        card_front.setCameraDistance(8000 * scale);
        card_back.setCameraDistance(8000 * scale);

        front_anim = (AnimatorSet) AnimatorInflater.loadAnimator(layout.getContext(), R.animator.front_animator);
        back_anim = (AnimatorSet) AnimatorInflater.loadAnimator(layout.getContext(), R.animator.back_animator);

        Button check_btn = (Button) layout.findViewById(R.id.check);
        check_btn.setOnClickListener(v->{
            if(isFront) {
                front_anim.setTarget(card_front);
                back_anim.setTarget(card_back);
                front_anim.start();
                back_anim.start();
                isFront = false;
            } else {
                front_anim.setTarget(card_back);
                back_anim.setTarget(card_front);
                back_anim.start();
                front_anim.start();
                isFront = true;
            }
        });
        return layout;
    }
}