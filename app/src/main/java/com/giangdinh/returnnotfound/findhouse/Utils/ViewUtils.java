package com.giangdinh.returnnotfound.findhouse.Utils;

import android.animation.Animator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by GiangDinh on 06/02/2018.
 */

public class ViewUtils {
    public static void delayAfterPress(final View view, int time) {
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, time);
    }

    public static void showViewAnimate(final View view, int duration) {
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1)
                .scaleX(1)
                .scaleY(1)
                .setDuration(duration)
                .setInterpolator(new BounceInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
//                        view.animate().setInterpolator(new LinearInterpolator());
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }

    public static void hideViewAnimate(final View view, int duration) {
        view.animate()
                .alpha(0)
                .scaleX(0)
                .scaleY(0)
                .setDuration(duration)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        view.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }
}
