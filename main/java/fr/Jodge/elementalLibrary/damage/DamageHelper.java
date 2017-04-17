package fr.Jodge.elementalLibrary.damage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumHand;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.matrix.AttackMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DamageMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DefenceMatrix;
import fr.Jodge.elementalLibrary.data.matrix.ElementalMatrix;
import fr.Jodge.elementalLibrary.data.matrix.EnvironmentalMatrix;
import fr.Jodge.elementalLibrary.data.matrix.FinalMatrix;
import fr.Jodge.elementalLibrary.data.matrix.ShieldMatrix;
import fr.Jodge.elementalLibrary.data.register.Getter;
import fr.Jodge.elementalLibrary.data.stats.ItemStats;
import fr.Jodge.elementalLibrary.log.JLog;

public class DamageHelper 
{
	public static FinalMatrix calculDamage(Entity attacker, EntityLivingBase targetEntity, float oldValue)
	{
		return calculDamage(attacker, targetEntity, oldValue, true);
	}
	public static FinalMatrix calculDamage(Entity attacker, EntityLivingBase targetEntity, float oldValue, boolean canApplyeDefaultEffect)
	{
		return calculDamage(attacker, targetEntity, oldValue, canApplyeDefaultEffect, canApplyeDefaultEffect);
	}
	public static FinalMatrix calculDamage(Entity attacker, EntityLivingBase targetEntity, float oldValue, boolean canApplyeDamageEffect, boolean canApplyeHealEffect)
	{
		DataParameter atkKey = Getter.getDataKeyForEntity(attacker, AttackMatrix.class);
		AttackMatrix atkMatrix = (AttackMatrix)attacker.getDataManager().get(atkKey);
		
		DataParameter resistKey = Getter.getDataKeyForEntity(targetEntity, DefenceMatrix.class);
		DefenceMatrix resistMatrix = (DefenceMatrix)targetEntity.getDataManager().get(resistKey);

		EnvironmentalMatrix environnementMatrix = new EnvironmentalMatrix();
		environnementMatrix.autoUpdate(attacker);
		
		// initialize ShieldMatrix
		ShieldMatrix armorMatrix = getShieldMatrix(targetEntity);
		
		// initialize damageMatrix (The most hard variable to initialize...)
		DamageMatrix damageMatrix = getDamageMatrix(attacker, oldValue);
		
		// making final matrix (arg = base, matrix...
		FinalMatrix returnValue = doCalculation(damageMatrix, 	atkMatrix,
																resistMatrix,
																environnementMatrix,
																armorMatrix);
		if(canApplyeDamageEffect)
		{
			returnValue.setNeedToApplyedDamageEffect(canApplyeDamageEffect, damageMatrix);
		}
		if(canApplyeHealEffect)
		{
			returnValue.setNeedToApplyedHealEffect(canApplyeHealEffect, damageMatrix);
		}

		// TODO beautiful log
		if(!attacker.worldObj.isRemote)
		{
			JLog.write("SERVER ----------------------------- ");
			JLog.write("Attaquant : " + attacker.getName());
			JLog.write("Cible : " + targetEntity.getName());
			JLog.write("Matrix Base  : " + damageMatrix.toString());
			JLog.write("Matrix ATK   : " + atkMatrix.toString());
			JLog.write("Matrix DEF   : " + resistMatrix.toString());
			JLog.write("Matrix ENVIR : " + environnementMatrix.toString());
			JLog.write("Matrix ARMOR : " + armorMatrix.toString());
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
			JLog.write("Matrix ARMOR : " + armorMatrix.toString());
			JLog.write("Matrix FINAL : " + returnValue.toString());
		}		
		return returnValue;
	}
	
	public static FinalMatrix calculIndirectDamage(Entity projectile, Entity attacker, EntityLivingBase targetEntity, float oldValue)
	{
		return calculIndirectDamage(projectile, attacker, targetEntity, oldValue, true);
	}
	public static FinalMatrix calculIndirectDamage(Entity projectile, Entity attacker, EntityLivingBase targetEntity, float oldValue, boolean canApplyeDefaultEffect)
	{
		return calculIndirectDamage(projectile, attacker, targetEntity, oldValue, canApplyeDefaultEffect, canApplyeDefaultEffect);
	}
	public static FinalMatrix calculIndirectDamage(Entity projectile, Entity attacker, EntityLivingBase targetEntity, float oldValue, boolean canApplyeDamageEffect, boolean canApplyeHealEffect)
	{
		DataParameter atkKey = Getter.getDataKeyForEntity(attacker, AttackMatrix.class);
		AttackMatrix atkMatrix = (AttackMatrix)attacker.getDataManager().get(atkKey);
		
		DataParameter resistKey = Getter.getDataKeyForEntity(targetEntity, DefenceMatrix.class);
		DefenceMatrix resistMatrix = (DefenceMatrix)targetEntity.getDataManager().get(resistKey);

		EnvironmentalMatrix environnementMatrix = new EnvironmentalMatrix();
		environnementMatrix.autoUpdate(attacker);
		
		// initialize ShieldMatrix
		ShieldMatrix armorMatrix = getShieldMatrix(targetEntity);
		
		// initialize damageMatrix (The most hard variable to initialize...)
		DamageMatrix damageMatrix = getDamageMatrix(attacker, oldValue);
		damageMatrix.autoUpdate(projectile, oldValue);

		// making final matrix (arg = base, matrix...
		FinalMatrix returnValue = doCalculation(damageMatrix, 	atkMatrix,
																resistMatrix,
																environnementMatrix,
																armorMatrix);
		if(canApplyeDamageEffect)
		{
			returnValue.setNeedToApplyedDamageEffect(canApplyeDamageEffect, damageMatrix);
		}
		if(canApplyeHealEffect)
		{
			returnValue.setNeedToApplyedHealEffect(canApplyeHealEffect, damageMatrix);
		}

		// TODO beautiful log
		JLog.write("PROJECTILE");
		if(!attacker.worldObj.isRemote)
		{
			JLog.write("SERVER ----------------------------- ");
			JLog.write("Attaquant : " + attacker.getName());
			JLog.write("Cible : " + targetEntity.getName());
			JLog.write("Matrix Base  : " + damageMatrix.toString());
			JLog.write("Matrix ATK   : " + atkMatrix.toString());
			JLog.write("Matrix DEF   : " + resistMatrix.toString());
			JLog.write("Matrix ENVIR : " + environnementMatrix.toString());
			JLog.write("Matrix ARMOR : " + armorMatrix.toString());
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
			JLog.write("Matrix ARMOR : " + armorMatrix.toString());
			JLog.write("Matrix FINAL : " + returnValue.toString());
		}
		return returnValue;
	}
	
	public static void ElementalizeDamageSource(EntityLivingBase target, DamageSource source, float amount) 
	{
		DamageMatrix baseMatrix = Getter.getElementalizeDamageSource(source, amount).clone();
		baseMatrix.setTotalDamage(amount);
		
		DataParameter resistKey = Getter.getDataKeyForEntity(target, DefenceMatrix.class);
		DefenceMatrix resistMatrix = (DefenceMatrix)target.getDataManager().get(resistKey);
		
		EnvironmentalMatrix environnementMatrix = new EnvironmentalMatrix();
		environnementMatrix.autoUpdate(target);
		
		// initialize ShieldMatrix
		ShieldMatrix armorMatrix = getShieldMatrix(target);

		FinalMatrix newMatrix = doCalculation(baseMatrix,
				resistMatrix,
				environnementMatrix,
				armorMatrix);
		
		newMatrix.setNeedToApplyedEffect(Getter.canApplyEffect(source));
		newMatrix.updateCalculation();
		appliedDamageEffect(target, newMatrix);
		target.attackEntityFrom(new ElementalDamageSource(source, newMatrix), newMatrix.getTotalDamage());
		
		JLog.write("DAMAGE SOURCES");
		if(!target.worldObj.isRemote)
		{
			JLog.write("SERVER ----------------------------- ");
			JLog.write("Cible : " + target.getName());
			JLog.write("Matrix Base  : " + baseMatrix.toString());
			JLog.write("Matrix DEF   : " + resistMatrix.toString());
			JLog.write("Matrix ENVIR : " + environnementMatrix.toString());
			JLog.write("Matrix ARMOR : " + armorMatrix.toString());
			JLog.write("Matrix FINAL : " + newMatrix.toString());
		}
		else
		{
			JLog.write("CLIENT ----------------------------- ");
			JLog.write("Cible : " + target.getName());
			JLog.write("Matrix Base  : " + baseMatrix.toString());
			JLog.write("Matrix DEF   : " + resistMatrix.toString());
			JLog.write("Matrix ENVIR : " + environnementMatrix.toString());
			JLog.write("Matrix ARMOR : " + armorMatrix.toString());
			JLog.write("Matrix FINAL : " + newMatrix.toString());
		}
	}
	
	
	// Tool
	
	public static boolean criticalHit(Entity playerIn, ItemStack itemstack)
	{
		return false;
	}
	
	/**
	 * Will took Damage matrix an apply each multiplier for creating final matrix
	 * @param base <i>DamageMatrix</i> base matrix
	 * @param listOfMatrix <i>ElementalMatrix...</i> all multiplier matrix
	 * @return
	 */
	public static FinalMatrix doCalculation(DamageMatrix base, ElementalMatrix... listOfMatrix)
	{
		Map<Element, Float> damageByElement = new HashMap<Element, Float>();
		for(Element element : Element.getAllActiveElement() )
		{
			float baseDamage = base.get(element);
			if(baseDamage != 0.0F)
			{
				for(ElementalMatrix matrix : listOfMatrix)
				{
					baseDamage *= matrix.get(element);
				}
			}
			damageByElement.put(element, baseDamage);
		}
		
		// making final matrix
		return new FinalMatrix(damageByElement);
	}
	
	public static DamageMatrix getDamageMatrix(Entity attacker, float oldValue)
	{
		DamageMatrix damageMatrix = null;
		ItemStack itemStack = null; 
		if(attacker instanceof EntityLivingBase)
		{
			EnumHand hand = ((EntityLivingBase)attacker).getActiveHand();
			if(hand != null)
				itemStack = ((EntityLivingBase)attacker).getHeldItem(hand);
			
			if(itemStack != null)
			{
				JLog.info("Current Item Stack : " + itemStack.getDisplayName());
				ItemStats baseItem = Getter.getItemStats(itemStack);
				damageMatrix = ((DamageMatrix) baseItem.getStat(DamageMatrix.class)).clone();
			}
			else
			{
				damageMatrix = new DamageMatrix().autoUpdateHand(((EntityLivingBase)attacker));
			}
			damageMatrix.autoUpdate(attacker, oldValue);
		}
		return damageMatrix;
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
			FinalMatrix damageMatrix = DamageHelper.calculDamage(player, entity, oldDamage, (oldSource instanceof EntityDamageSourceIndirect));
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
		/* TODO add potion effect... Maybe...
		if (this.isPotionActive(MobEffects.RESISTANCE) && source != DamageSource.outOfWorld)
        {
            int i = (this.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
            int j = 25 - i;
            float f = damage * (float)j;
            damage = f / 25.0F;
        }
		 */
		
		if(calculDamage.getTotalDamage() != 0.0F)
		{
			if(calculDamage.getNeedToApplyedDamageEffect())
			{
				JLog.info("Damage effect can be applyed.");
				appliedDamageEffect(target, calculDamage);
			}
			target.attackEntityFrom(new EntityElementalDamageSource(oldSource, calculDamage), calculDamage.getTotalDamage());
		}

		if(calculDamage.getTotalHeal() != 0.0F)
		{
			if(calculDamage.getNeedToApplyedHealEffect())
			{
				JLog.info("Heal effect can be applyed.");
				appliedHealEffect(target, calculDamage);
			}
			target.heal(calculDamage.getTotalHeal());
		}
	}
	
	protected static void appliedHealEffect(EntityLivingBase target, FinalMatrix calculDamage) 
	{
		// for each active element
		for(Element element : Element.getAllActiveElement())
		{
			// if damage < 0 : heal
			if(calculDamage.get(element) > 0 && calculDamage.domMatrix != null && element.asHealEffect())
			{
				for(Entry<PotionEffect, Float> effect : element.getHealEffect())
				{
					int randomValue = target.getRNG().nextInt(100);
					int probability = (int)(effect.getValue() * 100 * calculDamage.domMatrix.get(element));
					if(randomValue < probability)
					{
						target.addPotionEffect(effect.getKey());
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
			if(calculDamage.get(element) > 0 && calculDamage.domMatrix != null && element.asDamageEffect())
			{
				// For each possible effect.
				for(Entry<PotionEffect, Float> effect : element.getDamageEffect())
				{
					int randomValue = target.getRNG().nextInt(100);
					int probability = (int)(effect.getValue() * 100 );//* calculDamage.domMatrix.get(element));
					if(randomValue < probability)
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
				if(element.asFireEffect())
				{
					int randomValue = target.getRNG().nextInt(100);
					int probability = (int)(element.getFireProbability() * 100 * calculDamage.domMatrix.get(element));
					if(randomValue < probability)
					{
						target.setFire(element.getFireDuration());
					}
				}
			}
		}
	}

	public static ShieldMatrix getShieldMatrix(EntityLivingBase targetEntity)
	{
		List<ShieldMatrix> shieldsMatrix = new ArrayList<ShieldMatrix>();
		for(ItemStack stack : targetEntity.getArmorInventoryList())
		{
			if(stack != null)
			{
				ShieldMatrix tempMatrix = (ShieldMatrix) Getter.getItemStats(stack).getStat(ShieldMatrix.class);
				shieldsMatrix.add(tempMatrix);
			}
		}
		
		if(Main.isBaubleLoaded)
		{
			if(targetEntity instanceof EntityPlayer)
			{
				InventoryBaubles inventory = PlayerHandler.getPlayerBaubles((EntityPlayer)targetEntity);
				
				for(ItemStack stack : inventory.stackList)
				{
					if(stack != null)
					{
						ShieldMatrix tempMatrix = (ShieldMatrix) Getter.getItemStats(stack).getStat(ShieldMatrix.class);
						shieldsMatrix.add(tempMatrix);
					}
				}
			}
		}
		
		// instead of have between 0 and infinite matrix, we create one final shieldMatrix.
		// Work is the same, but on calculation we have only one instance for each kind of matrix, so it's more easy to deal whit
		ShieldMatrix armorMatrix = ShieldMatrix.calculArmor(shieldsMatrix);
		return armorMatrix;
	}
	
	
}