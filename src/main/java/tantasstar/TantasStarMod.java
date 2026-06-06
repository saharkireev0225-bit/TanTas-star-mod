package tantasstar;

import mindustry.mod.Mod;
import tantasstar.content.*;
import tantasstar.*;

public class TantasStarMod extends Mod {
    
    @Override
    public void init() {
        TantasTechTree.load();
        TantasBullets.load();
        System.out.println("[Tantas Star] Мод загружен!");
    }
}