package com.spacegdx.game.Enemies;

        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.badlogic.gdx.math.MathUtils;
        import com.badlogic.gdx.math.Rectangle;
        import com.spacegdx.game.Enemy;
        import com.spacegdx.game.Laser;

/**
 * Created by jeremy on 5/23/15.
 */
public class Asteroid extends Enemy{
    double xSpeed, ySpeed;
    float rSpeed, rAngle;
    enum Side{
        left,
        right
    }
    Side side;

    public Asteroid(Texture t, float width, float height, int speed) {
        super(t, width, height, speed);

        side = (MathUtils.randomBoolean()) ? Side.right : Side.left;

        float angle = MathUtils.random(MathUtils.PI / 12, MathUtils.PI / 2  - MathUtils.PI / 12);

        xSpeed = speed * Math.cos(angle);
        ySpeed = speed * Math.sin(angle);
        rSpeed = MathUtils.random(-90, 90);

        if(side == Side.right){
            hitbox = new Rectangle(480,MathUtils.random(200, 800), width - width / 6, height - height / 6);
            hitbox.x = 480;
        }else{
            xSpeed = -xSpeed;
            hitbox = new Rectangle(-width,MathUtils.random(200, 800), width - width / 6, height - height / 6);
        }
    }

    public static Asteroid createAsteroid(int speed){
        if(MathUtils.randomBoolean()){
            return new Asteroid(new Texture("asteroids/asteroid2.png"), 64, 64, speed);
        }else{
            return new Asteroid(new Texture("asteroids/asteroid1.png"), 42, 43, speed);
        }
    }

    @Override
    public void draw(SpriteBatch sb) {
        sb.draw(t, hitbox.x - width / 12, hitbox.y - height / 12, width / 2, height / 2, width, height, 1, 1,
                rAngle, 0, 0, (int)width, (int)height, false, false);
    }

    @Override
    public void move() {
        rAngle += rSpeed * Gdx.graphics.getDeltaTime();
        rAngle = wrap(rAngle);
        hitbox.x -= xSpeed * Gdx.graphics.getDeltaTime();
        hitbox.y -= ySpeed * Gdx.graphics.getDeltaTime();
    }

    @Override
    public boolean isOffScreen() {
        return (hitbox.x < - width || hitbox. y < -height || hitbox.x > 480);
    }

    @Override
    public Laser spawnLaser() {
        return null;
    }

    // lol stealing from robotics code
    public float wrap(float r){
        //return (r > desiredAngle) ? 360 - (r - desiredAngle) : desiredAngle - r;
        //return (r > 180) ? wrap(r - 360) : (r <= -180) ? wrap(r + 360) : r;
        if (Math.abs(r) >= 180) {
            r = r % 360;
            return (r > 180) ? r - 360 : (r <= -180) ? r + 360 : r;
        } else {
            return r;
        }

        //looks like the gyro is +-180, so I copied the code from last year, and modified it for angles instead of radians
    }
}
