package net.muxi.huashiapp.common.util;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Created by ybao on 16/5/23.
 */
public class AnimationUtil {

    public static int ANIMATION_DURATION_SHORT = 150;
    public static int ANIMATION_DURATION_MEDIUM = 400;
    public static int ANIMATION_DURATION_LONG = 800;

    public interface AnimationListener {
        boolean onAnimationStart(View view);

        boolean onAnimationEnd(View view);

        boolean onAnimationCancel(View view);
    }

    public static void fadeInView(View view) {
        fadeInView(view, ANIMATION_DURATION_SHORT);
    }

    public static void fadeInView(View view, int duration) {
        fadeInView(view, duration, null);
    }

    public static void fadeInView(View view, int duration, final AnimationListener animationListener) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0f);
        ViewPropertyAnimatorListener vpAnimatorListener = null;
        if (vpAnimatorListener == null) {
            vpAnimatorListener = new ViewPropertyAnimatorListener() {
                @Override
                public void onAnimationStart(View view) {
                    animationListener.onAnimationStart(view);
                }

                @Override
                public void onAnimationEnd(View view) {
                    animationListener.onAnimationEnd(view);
                }

                @Override
                public void onAnimationCancel(View view) {
                    animationListener.onAnimationCancel(view);
                }
            };
        }
        ViewCompat.animate(view).alpha(1f).setDuration(duration).setListener(vpAnimatorListener);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void reveal(final View view, final AnimationListener listener) {
        int cx = view.getWidth() - (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 24, view.getResources().getDisplayMetrics());
        int cy = view.getHeight() / 2;
        int finalRadius = Math.max(view.getWidth(),view.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view,cx,cy,0,finalRadius);
        view.setVisibility(View.VISIBLE);
        anim.setDuration(ANIMATION_DURATION_MEDIUM);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                listener.onAnimationStart(view);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onAnimationEnd(view);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                listener.onAnimationCancel(view);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }

}
