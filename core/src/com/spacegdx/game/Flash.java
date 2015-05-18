package com.spacegdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by Jeremy on 5/17/2015.
 */
public class Flash {
    Rectangle r;
    long time;
    long disTime = 250000000;
    public Flash(Rectangle r) {
        this.r = r;
        time = TimeUtils.nanoTime() + disTime;
    }
}
