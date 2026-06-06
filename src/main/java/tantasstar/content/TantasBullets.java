package tantasstar.content;

import tantasstar.content.bullets.BlackHoleBullet;

public class TantasBullets {
    public static BlackHoleBullet gravityBullet;

    public static void load() {
        gravityBullet = new BlackHoleBullet();
        // Имя не задаём — оно не требуется для доступа через статическое поле.
    }
}