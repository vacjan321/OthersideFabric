package net.vacjan.otherside.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.vacjan.otherside.IMobEntityMixinHelper;
import net.vacjan.otherside.Otherside;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobEntityMixin extends Entity implements IMobEntityMixinHelper {
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Unique
    public long lastWorldChange = -1;

    public void othersideFabric$setLastWorldChange(long n){
        lastWorldChange = n;
    }
    public long othersideFabric$getLastWorldChange(){
        return lastWorldChange;
    }
    public void othersideFabric$incrementLastWorldChange(){
        lastWorldChange++;
    }

    @Inject(at=@At("HEAD"), method = "requiresCustomPersistence()Z", cancellable = true)
    void requiresCustomPersistence(CallbackInfoReturnable<Boolean> cir) {
        if(this.lastWorldChange!=-1 && this.lastWorldChange < Otherside.config.getDespawnCooldown()*20L){
            cir.setReturnValue(true);
            cir.cancel();
            //System.out.println("Prevented despawn"+this.lastWorldChange);
        }
    }



}
