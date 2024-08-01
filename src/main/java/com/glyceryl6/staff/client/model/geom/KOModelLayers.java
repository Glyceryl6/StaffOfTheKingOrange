package com.glyceryl6.staff.client.model.geom;

import com.glyceryl6.staff.Main;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class KOModelLayers {

    public static final ModelLayerLocation STAFF_LAYER = new ModelLayerLocation(Main.prefix("staff"), "main");
    public static final ModelLayerLocation BEEPER_LAYER = new ModelLayerLocation(Main.prefix("beeper"), "main");
    public static final ModelLayerLocation PLAYER_HEAD_LAYER = new ModelLayerLocation(Main.prefix("player_head"), "main");
    public static final ModelLayerLocation STALAGMITE_LAYER = new ModelLayerLocation(Main.prefix("stalagmite"), "main");

}