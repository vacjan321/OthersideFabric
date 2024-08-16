package net.vacjan.otherside.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import net.vacjan.otherside.IMobEntityMixinHelper;
import net.vacjan.otherside.Otherside;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends Entity implements IMobEntityMixinHelper {
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
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

    @Inject(at=@At("HEAD"), method = "cannotDespawn()Z", cancellable = true)
    void cannotDespawn(CallbackInfoReturnable<Boolean> cir) {
        if(this.lastWorldChange!=-1 && this.lastWorldChange < Otherside.config.getDespawnCooldown()*20L){
            cir.setReturnValue(true);
            cir.cancel();
            //System.out.println("Prevented despawn"+this.lastWorldChange);
        }
    }



}
