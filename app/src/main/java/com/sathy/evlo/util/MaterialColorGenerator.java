package com.sathy.evlo.util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by sathy on 28/6/15.
 */
public class MaterialColorGenerator {

    private final List<Integer> colors = Arrays.asList(
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
    );

    private final Random random;

    public MaterialColorGenerator() {
        random = new Random(System.currentTimeMillis());
    }

    public int getRandomColor() {
        return colors.get(random.nextInt(colors.size()));
    }

    public int getColor(Object key) {
        return colors.get(Math.abs(key.hashCode()) % colors.size());
    }
}
