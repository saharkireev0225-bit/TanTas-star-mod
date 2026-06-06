// Создаём новый тип пули - лазер.
const superLaser = extend(ContinuousLaserBulletType, {
    // Урон (должен быть достаточно высоким, чтобы убивать T2 юнитов)
    // Для примера: 1000 урона в секунду.
    damage: 1000,
    // Длина лазера ( -1 означает "на всю карту").
    length: -1,
    // Пробивает ли цели.
    pierce: true,
    // Время затухания лазера (в тиках).
    fadeTime: 5,
    // Количество осколков, которое появится при попадании или исчезновении.
    fragBullets: 10,
    // Тип пули для осколков. Мы создадим её чуть позже.
    fragBullet: fragmentBullet,
    // Эффект попадания.
    hitEffect: Fx.hitLaser,
    // Эффект в момент выстрела.
    shootEffect: Fx.shootLaser,
    // Визуальные эффекты лазера (можно оставить цвета по умолчанию).
    // colors: [Color.white, Color.purple], // Пример смены цветов.
});

// Создаём тип для осколочных снарядов.
const fragmentBullet = extend(BasicBulletType, {
    // Характеристики осколка.
    speed: 5,
    damage: 75,
    lifetime: 60, // Время жизни осколка в тиках.
    hitEffect: Fx.hitFlame,
    despawnEffect: Fx.none,
    // Делаем осколки "грязными", чтобы они наносили урон по площади.
    splashDamageRadius: 20,
    splashDamage: 50,
    collidesAir: true,
    collidesGround: false,
});

// Экспортируем наш лазер, чтобы он был доступен для JSON-конфигурации турели.
global.superLaser = superLaser;