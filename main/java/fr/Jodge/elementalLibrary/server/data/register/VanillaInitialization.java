package fr.Jodge.elementalLibrary.server.data.register;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.matrix.AttackMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DefenceMatrix;
import fr.Jodge.elementalLibrary.data.register.Register;

@SideOnly(Side.SERVER)
public class VanillaInitialization 
{
	/**
	 * Initialize all ElementalLibrary default element
	 * and default resistance
	 */
	public static void initElement()
	{
		/** just for less work */
		Class atk = AttackMatrix.class;
		Class def = DefenceMatrix.class;
		
		/** default element */
		// new PotionEffect(MobEffects.SPEED, 5, 1)
		// <=> PotionEffect(Potion, Duration, Power)
		Element normal = Element.addOrGet("normal");
		Element fire = Element.addOrGet("fire").setFireEffect(0.75F, 2);
		Element water = Element.addOrGet("water").addOnHealEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 2, 1), 1.0F);
		Element wind = Element.addOrGet("wind").addOnHealEffect(new PotionEffect(MobEffects.SPEED, 5, 1), 1.0F);
		Element dirt = Element.addOrGet("dirt");
		Element wood = Element.addOrGet("wood");
		Element thunder = Element.addOrGet("thunder").setFireEffect(0.25F, 2);
		Element holy = Element.addOrGet("holy");
		Element dark = Element.addOrGet("dark");
		Element poison = Element.addOrGet("poison").addOnDamageEffect(new PotionEffect(MobEffects.POISON, 2, 1), 0.75F);
		Element hunger = Element.addOrGet("hunger").addOnDamageEffect(new PotionEffect(MobEffects.HUNGER, 5, 2), 0.9F).addOnHealEffect(new PotionEffect(MobEffects.SATURATION, 5, 2), 0.9F);
		
		// ATK
		normal.setDefaultValue(atk, EntityShulker.class, 1.0F);
		normal.setDefaultValue(atk, EntitySilverfish.class, 1.0F);
		normal.setDefaultValue(atk, EntityPig.class, 1.0F);
		normal.setDefaultValue(atk, EntityRabbit.class, 1.0F);
		normal.setDefaultValue(atk, EntitySheep.class, 1.0F);
		normal.setDefaultValue(atk, EntityVillager.class, 1.0F);
		normal.setDefaultValue(atk, EntityWolf.class, 1.0F);
		
		fire.setDefaultValue(atk, EntityBlaze.class, 1.5F);
		fire.setDefaultValue(atk, EntityCreeper.class, 1.0F);
		fire.setDefaultValue(atk, EntityGhast.class, 1.0F);
		fire.setDefaultValue(atk, EntityMagmaCube.class, 1.0F);
		fire.setDefaultValue(atk, EntityPigZombie.class, 1.0F);
		fire.setDefaultValue(atk, EntityDragon.class, 1.5F);
		
		water.setDefaultValue(atk, EntityGuardian.class, 1.0F);
		water.setDefaultValue(atk, EntitySlime.class, 1.0F);
		water.setDefaultValue(atk, EntitySnowman.class, 1.0F);
		water.setDefaultValue(atk, EntitySquid.class, 1.0F);

		wind.setDefaultValue(atk, EntityOcelot.class, 1.0F);
		wind.setDefaultValue(atk, EntityBat.class, 1.0F);
		wind.setDefaultValue(atk, EntityChicken.class, 1.0F);

		dirt.setDefaultValue(atk, EntityGiantZombie.class, 1.0F);
		dirt.setDefaultValue(atk, EntityIronGolem.class, 1.0F);
		dirt.setDefaultValue(atk, EntityCow.class, 1.0F);
		dirt.setDefaultValue(atk, EntityHorse.class, 1.0F);

		dark.setDefaultValue(atk, EntityEnderman.class, 1.0F);
		dark.setDefaultValue(atk, EntityEndermite.class, 1.0F);
		dark.setDefaultValue(atk, EntitySkeleton.class, 1.0F);
		dark.setDefaultValue(atk, EntityZombie.class, 1.0F);
		
		poison.setDefaultValue(atk, EntityCaveSpider.class, 1.0F);
		poison.setDefaultValue(atk, EntitySpider.class, 1.0F);
		poison.setDefaultValue(atk, EntityWither.class, 1.5F);
		poison.setDefaultValue(atk, EntityWitch.class, 1.0F);
		poison.setDefaultValue(atk, EntityMooshroom.class, 1.0F);
		
		
		// DEF
		fire.setDefaultValue(def, EntityBlaze.class, -1.0F);
		water.setDefaultValue(def, EntityBlaze.class, 2.5F);
		wind.setDefaultValue(def, EntityBlaze.class, 2.0F);
		wood.setDefaultValue(def, EntityBlaze.class, 0.5F);
		thunder.setDefaultValue(def, EntityBlaze.class, 0.5F);
		dirt.setDefaultValue(def, EntityBlaze.class, 0.25F);

		poison.setDefaultValue(def, EntityCaveSpider.class, 0.0F);

		thunder.setDefaultValue(def, EntityCreeper.class, 0.0F);
		
		holy.setDefaultValue(def, EntityEnderman.class, 2.0F);
		dark.setDefaultValue(def, EntityEnderman.class, 0.0F);

		holy.setDefaultValue(def, EntityEndermite.class, 2.0F);
		dark.setDefaultValue(def, EntityEndermite.class, -0.5F);
		
		wind.setDefaultValue(def, EntityGhast.class, 1.5F);
		dirt.setDefaultValue(def, EntityGhast.class, 0.0F);
		holy.setDefaultValue(def, EntityGhast.class, 2.0F);
		hunger.setDefaultValue(def, EntityGhast.class, 0.0F);

		wind.setDefaultValue(def, EntityGiantZombie.class, 1.25F);
		dirt.setDefaultValue(def, EntityGiantZombie.class, 0.75F);
		
		wind.setDefaultValue(def, EntityGolem.class, 0.25F);
		dirt.setDefaultValue(def, EntityGolem.class, -0.25F);
		hunger.setDefaultValue(def, EntityGolem.class, 0.0F);

		fire.setDefaultValue(def, EntityGuardian.class, 0.25F);
		thunder.setDefaultValue(def, EntityGuardian.class, 2.0F);
		
		fire.setDefaultValue(def, EntityMagmaCube.class, 0.0F);
		water.setDefaultValue(def, EntityMagmaCube.class, 2.0F);
		
		fire.setDefaultValue(def, EntityPigZombie.class, 0.0F);
		water.setDefaultValue(def, EntityPigZombie.class, 1.25F);
		dark.setDefaultValue(def, EntityPigZombie.class, -1.0F);
		holy.setDefaultValue(def, EntityPigZombie.class, 1.5F);
		
		dirt.setDefaultValue(def, EntityShulker.class, 2.0F);
		holy.setDefaultValue(def, EntityShulker.class, 1.5F);
		dark.setDefaultValue(def, EntityShulker.class, 0.0F);
		
		dirt.setDefaultValue(def, EntitySilverfish.class, -0.25F);
		
		dark.setDefaultValue(def, EntitySkeleton.class, -1.0F);
		holy.setDefaultValue(def, EntitySkeleton.class, 1.5F);
		hunger.setDefaultValue(def, EntitySkeleton.class, 0.0F);

		fire.setDefaultValue(def, EntitySlime.class, 0.75F);
		water.setDefaultValue(def, EntitySlime.class, -0.25F);

		fire.setDefaultValue(def, EntitySnowman.class, 3.0F);
		water.setDefaultValue(def, EntitySnowman.class, -5.0F);
		hunger.setDefaultValue(def, EntitySnowman.class, 0.0F);

		dirt.setDefaultValue(def, EntitySpider.class, 0.5F);
		poison.setDefaultValue(def, EntitySpider.class, 0.0F);
		
		normal.setDefaultValue(def, EntityWitch.class, 0.75F);
		fire.setDefaultValue(def, EntityWitch.class, 0.5F);
		water.setDefaultValue(def, EntityWitch.class, 0.5F);
		wind.setDefaultValue(def, EntityWitch.class, 0.5F);
		dirt.setDefaultValue(def, EntityWitch.class, 0.5F);
		wood.setDefaultValue(def, EntityWitch.class, 0.5F);
		thunder.setDefaultValue(def, EntityWitch.class, 0.5F);
		holy.setDefaultValue(def, EntityWitch.class, 2.0F);
		dark.setDefaultValue(def, EntityWitch.class, 0.5F);
		poison.setDefaultValue(def, EntityWitch.class, 0.5F);

		dark.setDefaultValue(def, EntityZombie.class, -1.0F);
		holy.setDefaultValue(def, EntityZombie.class, 1.5F);
		hunger.setDefaultValue(def, EntityZombie.class, 0.0F);

		wind.setDefaultValue(def, EntityBat.class, 1.25F);
		dirt.setDefaultValue(def, EntityBat.class, 0.25F);

		wind.setDefaultValue(def, EntityChicken.class, 1.25F);
		dirt.setDefaultValue(def, EntityChicken.class, 0.25F);

		poison.setDefaultValue(def, EntityMooshroom.class, 0.5F);
		
		water.setDefaultValue(def, EntityOcelot.class, 1.5F);
		
		fire.setDefaultValue(def, EntitySheep.class, 1.25F);
		
		fire.setDefaultValue(def, EntitySquid.class, 0.25F);
		water.setDefaultValue(def, EntitySquid.class, 0.75F);
		
		normal.setDefaultValue(def, EntityDragon.class, 0.75F);
		fire.setDefaultValue(def, EntityDragon.class, 0.0F);
		water.setDefaultValue(def, EntityDragon.class, 1.50F);
		wind.setDefaultValue(def, EntityDragon.class, 1.25F);
		dirt.setDefaultValue(def, EntityDragon.class, 0.25F);
		
		normal.setDefaultValue(def, EntityWither.class, 0.75F);
		wind.setDefaultValue(def, EntityWither.class, 1.5F);
		dirt.setDefaultValue(def, EntityWither.class, 0.0F);
		holy.setDefaultValue(def, EntityWither.class, 2.0F);
		dark.setDefaultValue(def, EntityWither.class, -1.0F);
		poison.setDefaultValue(def, EntityWither.class, 0.0F);

		// DAMAGE SOURCES
		Register.addNewElementOnDamageSources(DamageSource.anvil, dirt);
		Register.addNewElementOnDamageSources(DamageSource.cactus, poison);
		Register.addNewElementOnDamageSources(DamageSource.dragonBreath, fire);
		Register.addNewElementOnDamageSources(DamageSource.drown, water);
		Register.addNewElementOnDamageSources(DamageSource.fall, wind);
		Register.addNewElementOnDamageSources(DamageSource.fallingBlock, dirt);
		Register.addNewElementOnDamageSources(DamageSource.field_190095_e, fire); // hotfloor
		Register.addNewElementOnDamageSources(DamageSource.flyIntoWall, wind);
		Register.addNewElementOnDamageSources(DamageSource.generic, normal);
		Register.addNewElementOnDamageSources(DamageSource.inFire, fire);
		Register.addNewElementOnDamageSources(DamageSource.inWall, dirt);
		Register.addNewElementOnDamageSources(DamageSource.lava, fire);
		Register.addNewElementOnDamageSources(DamageSource.lightningBolt, thunder);
		Register.addNewElementOnDamageSources(DamageSource.magic, normal);
		Register.addNewElementOnDamageSources(DamageSource.onFire, fire);
		Register.addNewElementOnDamageSources(DamageSource.outOfWorld, dark);
		Register.addNewElementOnDamageSources(DamageSource.starve, hunger);
		Register.addNewElementOnDamageSources(DamageSource.wither, poison);

		
		
	}
	
}
