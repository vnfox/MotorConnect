

package com.motor.connect.feature.details;

import com.feature.area.R;

import java.util.Random;

public class BackDrop {

    private static final Random RANDOM = new Random();

    public static int getRandomBackDrop() {
        switch (RANDOM.nextInt(7)) {
            default:
            case 0:
                return R.mipmap.backdrop_1;
            case 1:
                return R.mipmap.backdrop_2;
            case 2:
                return R.mipmap.backdrop_3;
            case 3:
                return R.mipmap.backdrop_4;
            case 4:
                return R.mipmap.backdrop_5;
            case 5:
                return R.mipmap.backdrop_6;
            case 6:
                return R.mipmap.backdrop_7;
        }
    }
}
