package be.geelen.yarr.tools;


import android.view.MotionEvent;
import android.view.View;

public class CollapseViewOnTouchListener implements View.OnTouchListener {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Anim.collapse(view);
        return true;
    }
}