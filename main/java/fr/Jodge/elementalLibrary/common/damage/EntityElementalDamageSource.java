package fr.Jodge.elementalLibrary.common.damage;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSource;

public class EntityElementalDamageSource extends EntityDamageSource
{

	public EntityElementalDamageSource(String damageTypeIn, Entity damageSourceEntityIn) 
	{
		super(damageTypeIn, damageSourceEntityIn);
	}
	
	public EntityElementalDamageSource(EntityDamageSource oldSource) 
	{
		this(oldSource.getDamageType(), oldSource.getEntity());
	}
}
