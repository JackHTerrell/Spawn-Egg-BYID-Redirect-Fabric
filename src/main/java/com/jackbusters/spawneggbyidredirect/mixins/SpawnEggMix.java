package com.jackbusters.spawneggbyidredirect.mixins;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

/**
 * <p>Jack Terrell, 2024</p>
 * <p>These mixins essentially make the Spawn Egg items compatible with spawn eggs of duplicate types.</p>
 */
@Mixin(SpawnEggItem.class)
public abstract class SpawnEggMix extends Item{

    public SpawnEggMix(Properties properties) {
        super(properties);
    }

    /*
        Makes SpawnEggItem call putIfAbsent instead of put.
        This resolves the issue where the last registered spawn egg of the same
        entityType would overwrite the colors for the first registered spawn egg.

        Highly unlikely that any other mod is going to be Redirecting this function call for another purpose.
     */
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private Object put(Map<EntityType<? extends Mob>, SpawnEggItem> instance, Object k, Object v) {
        if(k instanceof EntityType && v instanceof SpawnEggItem) {
            EntityType<? extends Mob> entityType = (EntityType<? extends Mob>) k;
            SpawnEggItem spawnEggItem = (SpawnEggItem) v;
            instance.putIfAbsent(entityType,spawnEggItem);
        }
        return instance;
    }
}
