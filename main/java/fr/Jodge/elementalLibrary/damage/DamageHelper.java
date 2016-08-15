package fr.Jodge.elementalLibrary.damage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.matrix.AttackMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DamageMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DefenceMatrix;
import fr.Jodge.elementalLibrary.data.matrix.ElementalMatrix;
import fr.Jodge.elementalLibrary.data.matrix.EnvironmentalMatrix;
import fr.Jodge.elementalLibrary.data.matrix.FinalMatrix;
import fr.Jodge.elementalLibrary.data.register.Getter;
import fr.Jodge.elementalLibrary.data.register.Variable;
import fr.Jodge.elementalLibrary.function.JLog;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;

public class DamageHelper 
{
	public static FinalMatrix calculDamage(EntityLivingBase attacker, EntityLivingBase targetEntity, float oldValue)
	{
		// init
		Map<Element, Float> damageByElement = new HashMap<Element, Float>();
		
		DataParameter atkKey = Getter.getDataKeyForEntity(attacker, AttackMatrix.class);
		AttackMatrix atkMatrix = attacker.getDataManager().get(atkKey);
		
		DataParameter resistKey = Getter.getDataKeyForEntity(targetEntity, DefenceMatrix.class);
		DefenceMatrix resistMatrix = targetEntity.getDataManager().get(resistKey);

		EnvironmentalMatrix environnementMatrix = new EnvironmentalMatrix();
		environnementMatrix.autoUptdate(attacker);
		
		DamageMatrix damageMatrix;
		
		ItemStack itemStack = null; 

		EnumHand hand = attacker.getActiveHand();
		if(hand != null )
			itemStack = attacker.getHeldItem(hand);
		
		if(itemStack != null) 
		{
			Item activeItem = itemStack.getItem();
			
			damageMatrix = Getter.getDamageBruteFromItem(activeItem);
			if(damageMatrix == null)
			{
				damageMatrix = new DamageMatrix(0.0F);
				damageMatrix.autoUpdateDamage(attacker, oldValue);
			}
		}
		else
		{
			damageMatrix = new DamageMatrix(0.0F);
			damageMatrix.autoUpdateDamageHand(attacker, oldValue);
		}
	
		for(Element element : Element.getAllActiveElement() )
		{
			float baseDamage = damageMatrix.get(element);
			float theoriqueDamage = 0.0F;
			if(baseDamage != 0.0F)
			{
				//String currentElementName = Element.getKey(index);
				float atkMultiplier = atkMatrix.get(element);
				if(atkMultiplier == 0.0F)
					JLog.write("TEST : atk = 0... " + element);
				float resDivider = resistMatrix.get(element);
				if(resDivider == 0.0F)
					JLog.write("TEST : def = 0... " + element);
				float enviMultiplier = environnementMatrix.get(element);
				if(enviMultiplier == 0.0F)
				
				theoriqueDamage = baseDamage * atkMultiplier * resDivider * enviMultiplier;
			}
			damageByElement.put(element, theoriqueDamage);
		}
		FinalMatrix returnValue = new FinalMatrix(damageByElement);

		if(!attacker.worldObj.isRemote)
		{
			JLog.write("SERVER ----------------------------- ");
			JLog.write("Attaquant : " + attacker.getName());
			JLog.write("Cible : " + targetEntity.getName());
			JLog.write("Matrix Base  : " + damageMatrix.toString());
			JLog.write("Matrix ATK   : " + atkMatrix.toString());
			JLog.write("Matrix DEF   : " + resistMatrix.toString());
			JLog.write("Matrix ENVIR : " + environnementMatrix.toString());
			JLog.write("Matrix FINAL : " + returnValue.toString());
		}
		else
		{
			JLog.write("CLIENT ----------------------------- ");
			JLog.write("Attaquant : " + attacker.getName());
			JLog.write("Cible : " + targetEntity.getName());
			JLog.write("Matrix Base  : " + damageMatrix.toString());
			JLog.write("Matrix ATK   : " + atkMatrix.toString());
			JLog.write("Matrix DEF   : " + resistMatrix.toString());
			JLog.write("Matrix ENVIR : " + environnementMatrix.toString());
			JLog.write("Matrix FINAL : " + returnValue.toString());
		}		
		return returnValue;

	}
	

	/**
	 * Change Damage Calculation based on oldSource. Applied to a list of entity.
	 * 
	 * @param calculDamage <i>List<Float></i> calculDamage 
	 * @param attacker <i>Entity</i> entity which deal damage (can be anything like falling sand)
	 * @param entityList <i>List<? extends EntityLivingBase></i> target living entity beat
	 * @param oldSource <i>EntityDamageSource</i> oldSource previous source to erase and modify
	 */
	public static void dealDamage(EntityLivingBase player, List<? extends EntityLivingBase> entityList, EntityDamageSource oldSource, float oldDamage)
	{
		EntityLivingBase entity;
		for (int i = 0; i < entityList.size(); i++)
		{
			entity = entityList.get(i);
			FinalMatrix damageMatrix = DamageHelper.calculDamage(player, entity, oldDamage);
			DamageHelper.dealDamage(player, entity, oldSource, damageMatrix);	
		}
	}
		
	/**
	 * Change Damage Calculation based on oldSource.
	 * 
	 * @param calculDamage <i>List<Float></i> calculDamage 
	 * @param attacker <i>Entity</i> entity which deal damage (can be anything like falling sand)
	 * @param target <i>EntityLivingBase</i> target living entity beat
	 * @param oldSource <i>EntityDamageSource</i> oldSource previous source to erase and modify
	 */
	public static void dealDamage(Entity attacker, EntityLivingBase target, EntityDamageSource oldSource, FinalMatrix calculDamage)
	{
		if(calculDamage.getTotalDamage() != 0.0F)
		{
			appliedDamageEffect(target, calculDamage);
			if(attacker instanceof IRangedAttackMob && oldSource instanceof EntityDamageSourceIndirect )
			{
				distanceDamage((IProjectile)((EntityDamageSourceIndirect)oldSource).getSourceOfDamage(), (EntityLivingBase)attacker, target, calculDamage.getTotalDamage());
			}
			else
			{
				// TODO change method to work whit IProjectile without rebound effect
			}
			target.attackEntityFrom(new EntityElementalDamageSource(oldSource, calculDamage), calculDamage.getTotalDamage());
		}

		if(calculDamage.getTotalHeal() != 0.0F)
		{
			appliedHealEffect(target, calculDamage);
			target.heal(calculDamage.getTotalHeal());
		}
	}
	
	protected static void appliedHealEffect(EntityLivingBase target, FinalMatrix calculDamage) 
	{
		// for each active element
		for(Element element : Element.getAllActiveElement())
		{
			// if damage < 0 : heal
			if(calculDamage.get(element) < 0)
			{
				if(element.asHealEffect())
				{
					for(Entry<PotionEffect, Float> effect : element.getHealEffect())
					{
						int probability = target.getRNG().nextInt() % 100;
						if(probability < effect.getValue() * 100)
						{
							target.addPotionEffect(effect.getKey());
						}
					}
				}
			}
		}
	}


	protected static void appliedDamageEffect(EntityLivingBase target, FinalMatrix calculDamage) 
	{
		// for each active element
		for(Element element : Element.getAllActiveElement())
		{
			// if damage < 0 : heal
			if(calculDamage.get(element) > 0)
			{
				if(element.asDamageEffect())
				{
					for(Entry<PotionEffect, Float> effect : element.getDamageEffect())
					{
						int probability = target.getRNG().nextInt(100);
						if(probability < effect.getValue() * 100)
						{
							PotionEffect potion = effect.getKey();
							if(target.isPotionApplicable(potion))
							{
								target.addPotionEffect(potion);
								JLog.write("### EFFECT APPLYED : " + effect);
							}
							//target.isPotionActive(potion.getPotion());
						}
					}
				}
				if(element.asFireEffect())
				{
					int probability = target.getRNG().nextInt(100);
					if(probability < element.getFireProbability() * 100)
					{
						target.setFire(element.getFireDuration());
					}
				}
			}
		}
	}



	/**
	 * 
	 * @param projectile <i>projectile</i>
	 * @param attacker <i>EntityLivingBase</i>
	 * @param target <i>EntityLivingBase</i>
	 * @param damage <i>Float</i>
	 */
	public static void distanceDamage (IProjectile projectile, EntityLivingBase attacker, EntityLivingBase target, float damage)
	{
		
        double d0 = target.posX - attacker.posX;
        double d1 = target.getEntityBoundingBox().minY + target.height / 3.0F;
        
        if(projectile instanceof EntityArrow)
        	d1 -= ((EntityArrow)projectile).posY;
        else
        	d1 -= 1.100000023841858D;

        double d2 = target.posZ - attacker.posZ;
        double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        projectile.setThrowableHeading(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, 14 - attacker.worldObj.getDifficulty().getDifficultyId() * 4);
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, attacker);
        int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, attacker);

        if(projectile instanceof EntityArrow)
        {
        	((EntityArrow)projectile).setDamage(damage * 2.0F + attacker.getRNG().nextGaussian() * 0.25D + attacker.worldObj.getDifficulty().getDifficultyId() * 0.11F);

            if (i > 0)
            {
            	((EntityArrow)projectile).setDamage(((EntityArrow)projectile).getDamage() + i * 0.5D + 0.5D);
            }

            if (j > 0)
            {
            	((EntityArrow)projectile).setKnockbackStrength(j);
            }

            if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, attacker) > 0)
            {
            	((EntityArrow)projectile).setFire(100);
            }
        }


        attacker.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (attacker.getRNG().nextFloat() * 0.4F + 0.8F));
        attacker.worldObj.spawnEntityInWorld((Entity) projectile);
	}
		
	public static boolean criticalHit(Entity playerIn, ItemStack itemstack)
	{
		return false;
	}


	public static void ElementalizeDamageSource(EntityLivingBase target, DamageSource source, float amount) 
	{
		FinalMatrix newMatrix = Getter.getElementalizeDamageSource(source, amount);
		newMatrix.updateCalculation();
		appliedDamageEffect(target, newMatrix);
		target.attackEntityFrom(new ElementalDamageSource(source, newMatrix), newMatrix.getTotalDamage());
	}
	
	
}