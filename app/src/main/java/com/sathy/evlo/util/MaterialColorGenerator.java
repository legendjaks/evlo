package com.sathy.evlo.util;

import java.util.Random;

/**
 * Created by sathy on 28/6/15.
 */
public class MaterialColorGenerator {

    private static final int[] colors = {
            0xFFF44336,
            0xFFE91E63,
            0xFF9C27B0,
            0xFF673AB7,
            0xFF3F51B5,
            0xFF2196F3,
            0xFF03A9F4,
            0xFF00BCD4,
            0xFF009688,
            0xFF4CAF50,
            0xFF8BC34A,
            0xFFCDDC39,
            0xFFFFEB3B,
            0xFFFFC107,
            0xFFFF9800,
            0xFFFF5722
    };

    private static final Random random = new Random(System.currentTimeMillis());

    public static int get() {
        return colors[random.nextInt(colors.length)];
    }

    public static int get(String key) {
        return colors[Math.abs(key.hashCode()) % colors.length];
    }
}
