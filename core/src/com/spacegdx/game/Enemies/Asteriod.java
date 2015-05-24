package com.spacegdx.game.Enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.spacegdx.game.Enemy;

/**
 * Created by jeremy on 5/23/15.
 */
public class Asteriod extends Enemy{
    int xSpeed, ySpeed;
    enum Side{
        left,
        right
    }
    Side side;

    public Asteriod(int speed) {
        super(new Texture("asteriod.png"), 48, 48, speed);
        side = (MathUtils.randomBoolean()) ? Side.right : Side.left;
    }

    @Override
    public void draw(SpriteBatch sb) {
        sb.draw(t,hitbox.x, hitbox.y);
    }

    @Override
    public void move() {

    }
}
