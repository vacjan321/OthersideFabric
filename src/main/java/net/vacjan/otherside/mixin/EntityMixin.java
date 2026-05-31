package net.vacjan.otherside.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.portal.TeleportTransition;
import net.vacjan.otherside.IMobEntityMixinHelper;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(at=@At("RETURN"), method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/world/entity/Entity;")
    void teleport(TeleportTransition teleportTarget, CallbackInfoReturnable<Entity> cir) {
        if (cir.getReturnValue()!=null){
            if(Mob.class.isAssignableFrom(cir.getReturnValue().getClass())){
                ((IMobEntityMixinHelper)cir.getReturnValue()).othersideFabric$setLastWorldChange(0);
                //LOGGER.info("Entity changed worlds");
            }
        }
    }

    @Inject(at=@At("TAIL"), method = "baseTick()V")
    void baseTick(CallbackInfo ci) {
        //noinspection ConstantValue
        if(Mob.class.isAssignableFrom(this.getClass())){
            if(((IMobEntityMixinHelper)this).othersideFabric$getLastWorldChange() != -1){
                ((IMobEntityMixinHelper)this).othersideFabric$incrementLastWorldChange();
                //LOGGER.info("Entity tick {}", ((IMobEntityMixinHelper) this).othersideFabric$getLastWorldChange());
            }
        }
    }
}
