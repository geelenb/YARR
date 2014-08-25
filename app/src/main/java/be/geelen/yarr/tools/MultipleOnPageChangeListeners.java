package be.geelen.yarr.tools;

import android.support.v4.view.ViewPager;

import java.util.ArrayList;

public class MultipleOnPageChangeListeners implements ViewPager.OnPageChangeListener {
    ArrayList<ViewPager.OnPageChangeListener> listeners = new ArrayList<ViewPager.OnPageChangeListener>();

    public MultipleOnPageChangeListeners(ViewPager.OnPageChangeListener... listeners) {
        for (ViewPager.OnPageChangeListener listener : listeners) {
            this.listeners.add(listener);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        for (ViewPager.OnPageChangeListener listener : listeners) {
            listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        for (ViewPager.OnPageChangeListener listener : listeners) {
            listener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        for (ViewPager.OnPageChangeListener listener : listeners) {
            listener.onPageScrollStateChanged(state);
        }
    }
}
