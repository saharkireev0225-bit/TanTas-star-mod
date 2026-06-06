package tantasstar.content.bullets;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.entities.Effect;
import mindustry.entities.Units;        // <--- Импорт изменён на правильный!
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Bullet;
import mindustry.gen.Entityc;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;

import static mindustry.Vars.*;

public class BlackHoleBullet extends BulletType {

    public float blackHoleRadius = 80f;
    public float blackHoleDamagePerSecond = 15f; // Урон в секунду
    public float blackHoleDuration = 3f * 60f;
    public float explosionRadius = 120f;
    public int fragmentCount = 8;

    public BlackHoleBullet() {
        super();
        speed = 4f;
        lifetime = 120f;
        damage = 0;
        hitEffect = new Effect(0, e -> {});
        despawnEffect = new Effect(0, e -> {});
        shootEffect = new Effect(0, e -> {});
        smokeEffect = new Effect(0, e -> {});
        collides = true;
        collidesTiles = false;
        collidesAir = true;
        collidesGround = false;
    }

    @Override
    public void init(Bullet b) {
        super.init(b);
        Time.run((long) lifetime, () -> {
            if (b.isAdded()) {
                createBlackHole(b.x, b.y, b.team, b.owner);
                b.remove();
            }
        });
    }

    @Override
    public void hit(Bullet b, float hitx, float hity) {
        createBlackHole(hitx, hity, b.team, b.owner);
        super.hit(b, hitx, hity);
    }

    @Override
    public void despawned(Bullet b) {
        if (b.isAdded()) {
            createBlackHole(b.x, b.y, b.team, b.owner);
        }
        super.despawned(b);
    }

    private void createBlackHole(float x, float y, Team team, Entityc owner) {
        // Эффект чёрной дыры (притяжение и урон)
        Effect blackHoleEffect = new Effect(blackHoleDuration, e -> {
            // Притягиваем врагов с помощью правильного импорта Units
            Units.nearbyEnemies(team, e.x, e.y, blackHoleRadius, unit -> {
                float angle = unit.angleTo(e.x, e.y);
                float dst = unit.dst(e.x, e.y);
                float force = Math.max(0, 1f - (dst / blackHoleRadius));
                unit.vel.add(angle, Math.min(force * 0.5f, 0.5f));
                unit.damage(blackHoleDamagePerSecond * Time.delta);
            });
            // Отрисовка
            float f = e.fout();
            Draw.color(Color.purple, Color.black, f);
            Fill.circle(e.x, e.y, blackHoleRadius * f);
            Draw.color();
        });
        blackHoleEffect.at(x, y);

        // Взрыв в конце
        Time.run((long) blackHoleDuration, () -> {
            // Взрывной эффект
            new Effect(30, e -> {
                Draw.color(Pal.darkMetal);
                Fill.circle(e.x, e.y, e.fin() * explosionRadius);
                Draw.color();
            }).at(x, y);

            // Осколки
            for (int i = 0; i < fragmentCount; i++) {
                float angle = Mathf.random(360f);
                BulletType fragment = new BasicBulletType(5f, 30) {{
                    splashDamageRadius = 20f;
                    splashDamage = 25f;
                    lifetime = 60f;
                    hitEffect = new Effect(0, e -> {});
                }};
                fragment.create(owner, team, x, y, angle);
            }
        });
    }
}