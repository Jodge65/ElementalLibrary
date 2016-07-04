package fr.Jodge.elementalLibrary.common.damage;

import java.util.ArrayList;
import java.util.List;

import fr.Jodge.elementalLibrary.common.Element;
import fr.Jodge.elementalLibrary.common.ElementalConstante;
import fr.Jodge.elementalLibrary.common.function.JLog;
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


	public static List<Float> calculDamage(Entity attacker, EntityLivingBase targetEntity)
	{

		// init
		List<Float> damageByElement = new ArrayList<Float>();
		ElementalMatrix atkMatrix = attacker.getDataManager().get(ElementalConstante.getDataKeyForEntity((EntityLivingBase)attacker, ElementalConstante.DATA_ATK));
		ElementalMatrix resistMatrix = targetEntity.getDataManager().get(ElementalConstante.getDataKeyForEntity((EntityLivingBase)targetEntity, ElementalConstante.DATA_RES));
		ElementalMatrix environnementMatrix = EnvironmentalHelper.getEnvironmentalMatrix((EntityLivingBase) targetEntity);
		ElementalMatrix damageMatrix;
		
		ItemStack itemStack = null;

		EnumHand hand = ((EntityLivingBase)attacker).getActiveHand();
		if(hand != null )
			itemStack = ((EntityLivingBase)attacker).getHeldItem(hand);
					
		if(itemStack != null)
		{
			Item activeItem = itemStack.getItem();
			
			if(activeItem instanceof IWeaponMatrix)
			{
				damageMatrix = ((IWeaponMatrix)activeItem).getDamageMatrix();
			}
			else
			{
				damageMatrix = new ElementalMatrix(0.0F);
				damageMatrix.autoUpdateDamage((EntityLivingBase)attacker);
			}
		}
		else
		{
			damageMatrix = new ElementalMatrix(0.0F);
			damageMatrix.autoUpdateDamageHand((EntityLivingBase)attacker);
		}
	
		for(int index = 0; index < Element.getNumberOfElement(); index ++)
		{
			float theoriqueDamage = 0.0F;
			float damage = damageMatrix.get(index);
			if(damage != 0.0F)
			{
				//String currentElementName = Element.getKey(index);
				float atkMultiplier = atkMatrix.get(index);
				float resDivider = resistMatrix.get(index);
				float enviMultiplier = environnementMatrix.get(index);
				
				theoriqueDamage = damage * atkMultiplier * resDivider * enviMultiplier;
			}
			damageByElement.add(theoriqueDamage);
		}
		

		if(!attacker.worldObj.isRemote)
		{
			JLog.write("SERVER");
			JLog.write("Attaquant : " + attacker.getName());
			JLog.write("Cible : " + targetEntity.getName());
			JLog.write("Matrix Base  : " + damageMatrix.toString());
			JLog.write("Matrix ATK   : " + atkMatrix.toString());
			JLog.write("Matrix DEF   : " + resistMatrix.toString());
			JLog.write("Matrix ENVIR : " + environnementMatrix.toString());
			JLog.write("Matrix FINAL : " + damageByElement.toString());
		}
		else
		{
			JLog.write("CLIENT");
			JLog.write("Attaquant : " + attacker.getName());
			JLog.write("Cible : " + targetEntity.getName());
			JLog.write("Matrix Base  : " + damageMatrix.toString());
			JLog.write("Matrix ATK   : " + atkMatrix.toString());
			JLog.write("Matrix DEF   : " + resistMatrix.toString());
			JLog.write("Matrix ENVIR : " + environnementMatrix.toString());
			JLog.write("Matrix FINAL : " + damageByElement.toString());
		}

		
		return damageByElement;

	}
	
	/**
	 * Same as the first but whit a list of entity instead
	 * 
	 * @param player <i>Entity</i>
	 * @param entityList
	 */
	public static void dealDamage(Entity player, List entityList, EntityDamageSource oldSource)
	{
		EntityLivingBase entity;
		for (int i = 0; i < entityList.size(); i++)
		{
			if(entityList.get(i) instanceof EntityLivingBase)
			{
				entity = (EntityLivingBase) entityList.get(i);
				DamageHelper.dealDamage(player, entity, oldSource);	
			}
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
	public static void dealDamage(Entity attacker, EntityLivingBase target, EntityDamageSource oldSource, List<Float> calculDamage)
	{
		float currentDamage = 0.0F;
		float currentHeal = 0.0F;

		for(float value : calculDamage)
		{
			if(value > 0.0F)
				currentDamage += value;
			else
				currentHeal += value;
		}
		
		if(currentDamage != 0.0F)
		{
			if(attacker instanceof IRangedAttackMob && oldSource instanceof EntityDamageSourceIndirect )
			{
				distanceDamage((IProjectile)((EntityDamageSourceIndirect)oldSource).getSourceOfDamage(), (EntityLivingBase)attacker, (EntityLivingBase)target, currentDamage);
			}
			else
			{
				
			}
			((EntityLivingBase)target).attackEntityFrom(new EntityElementalDamageSource(oldSource), currentDamage);
		}

		if(currentHeal != 0.0F)
			((EntityLivingBase)target).heal(currentHeal);
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
        double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F);
        
        if(projectile instanceof EntityArrow)
        	d1 -= ((EntityArrow)projectile).posY;
        else
        	d1 -= 1.100000023841858D;

        double d2 = target.posZ - attacker.posZ;
        double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        projectile.setThrowableHeading(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - attacker.worldObj.getDifficulty().getDifficultyId() * 4));
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.power, attacker);
        int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.punch, attacker);

        if(projectile instanceof EntityArrow)
        {
        	((EntityArrow)projectile).setDamage((double)(damage * 2.0F) + attacker.getRNG().nextGaussian() * 0.25D + (double)((float)attacker.worldObj.getDifficulty().getDifficultyId() * 0.11F));

            if (i > 0)
            {
            	((EntityArrow)projectile).setDamage(((EntityArrow)projectile).getDamage() + (double)i * 0.5D + 0.5D);
            }

            if (j > 0)
            {
            	((EntityArrow)projectile).setKnockbackStrength(j);
            }

            if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.flame, attacker) > 0)
            {
            	((EntityArrow)projectile).setFire(100);
            }
        }


        attacker.playSound(SoundEvents.entity_skeleton_shoot, 1.0F, 1.0F / (attacker.getRNG().nextFloat() * 0.4F + 0.8F));
        attacker.worldObj.spawnEntityInWorld((Entity) projectile);
	}
	
	public static void dealDamage(Entity attacker, EntityLivingBase target, EntityDamageSource oldSource)
	{
		dealDamage(attacker, target, oldSource, calculDamage(attacker, target));
	}
	
	public static boolean criticalHit(Entity playerIn, ItemStack itemstack)
	{
		return false;
	}
}
