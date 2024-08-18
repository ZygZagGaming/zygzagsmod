package io.github.zygzaggaming.zygzagsmod.common.util;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.EntityEvent;

public class ProjectileImpactEntityEvent extends EntityEvent implements ICancellableEvent {
    private final EntityHitResult ray;
    private final Projectile projectile;
    public ProjectileImpactEntityEvent(Projectile projectile, EntityHitResult entityRay) {
        super(projectile);
        this.ray = entityRay;
        this.projectile = projectile;
    }
    public Projectile getProjectile() {
        return projectile;
    }
    public EntityHitResult getRay() {return ray;}
}
