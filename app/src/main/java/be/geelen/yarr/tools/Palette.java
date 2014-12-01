package be.geelen.yarr.tools;

import android.content.res.Resources;
import android.content.res.TypedArray;

import be.geelen.yarr.R;

/**
 * Created by bram on 30/08/2014.
 */
public class Palette {
    public static int get(Resources r, int darkness) {
        TypedArray ta = r.obtainTypedArray(R.array.steel);
        ta.recycle();
        return getFromArray(ta, darkness);
    }

    public static int getGrey(Resources r, int darkness) {
        TypedArray ta = r.obtainTypedArray(R.array.grey);
        ta.recycle();
        return getFromArray(ta, darkness);
    }

    private static int getFromArray (TypedArray ta, int darkness) {
        int index = 0;
        switch(darkness) {
            case 0:
            case 50:
                index = 0;
                break;
            case 1:
            case 100:
                index = 1;
                break;
            case 2:
            case 200:
                index = 2;
                break;
            case 300:
            case 3:
                index = 3;
                break;
            case 400:
            case 4:
                index = 4;
                break;
            case 500:
            case 5:
                index = 5;
                break;
            case 600:
            case 6:
                index = 6;
                break;
            case 700:
            case 7:
                index = 7;
                break;
            case 800:
            case 8:
                index = 8;
                break;
            case 900:
            case 9:
                index = 9;
                break;
        }
        return ta.getColor(index, 0);
    }
}
