package com.spacegdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by Jeremy on 5/17/2015.
 * Allows for the creation of small, short live sprites
 * @author Jeremy
 */
public class Flash {
    Rectangle r;
    long time;
    long disTime = 250000000;

    /**
     * basic flash constructor
     * @param r rectangle to represent location / size of the flash
     */
    public Flash(Rectangle r) {
        this.r = r;
        time = TimeUtils.nanoTime() + disTime;
    }
}
