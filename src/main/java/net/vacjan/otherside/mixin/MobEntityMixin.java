package net.vacjan.otherside.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.vacjan.otherside.IMobEntityMixinHelper;
import net.vacjan.otherside.Otherside;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends Entity implements IMobEntityMixinHelper {
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    public long lastWorldChange = 0;

    public void othersideFabric$setLastWorldChange(long n){
        lastWorldChange = n;
    }
    public long othersideFabric$getLastWorldChange(){
        return lastWorldChange;
    }
    public void othersideFabric$incrementLastWorldChange(){
        lastWorldChange++;
    }

    @Shadow
    protected abstract boolean isDisallowedInPeaceful();
    @Shadow
    public abstract boolean isPersistent();
    @Shadow
    public abstract boolean cannotDespawn();

    @Inject(at=@At("HEAD"), method = "checkDespawn()V", cancellable = true)
    void checkDespawnMixin(CallbackInfo ci) {
        if( !(this.getWorld().getDifficulty() == Difficulty.PEACEFUL && this.isDisallowedInPeaceful()) && (!this.isPersistent() && !this.cannotDespawn())){
                if(this.lastWorldChange < Otherside.config.getDespawnCooldown()*20L){
                    ci.cancel();
                }
        }
    }



}
