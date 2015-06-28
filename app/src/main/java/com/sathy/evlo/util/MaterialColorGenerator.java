package com.sathy.evlo.util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by sathy on 28/6/15.
 */
public class MaterialColorGenerator {

    private final List<Integer> colors = Arrays.asList(
            0xffe57373,
            0xfff06292,
            0xffba68c8,
            0xff9575cd,
            0xff7986cb,
            0xff64b5f6,
            0xff4fc3f7,
            0xff4dd0e1,
            0xff4db6ac,
            0xff81c784,
            0xffaed581,
            0xffff8a65,
            0xffd4e157,
            0xffffd54f,
            0xffffb74d,
            0xffa1887f,
            0xff90a4ae
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
