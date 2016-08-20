package fr.Jodge.elementalLibrary.server.data.register;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.Jodge.elementalLibrary.data.DataHelper;
import fr.Jodge.elementalLibrary.data.ItemHelper;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.matrix.AttackMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DamageMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DefenceMatrix;
import fr.Jodge.elementalLibrary.data.matrix.ShieldMatrix;
import fr.Jodge.elementalLibrary.data.register.Register;

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
		Class dam = DamageMatrix.class;
		Class shi = ShieldMatrix.class;
		
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

		// DAM
		normal.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Blocks.TORCH), 0.5F);
		normal.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.MILK_BUCKET), 0.75F);

		fire.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.BLAZE_POWDER), 1.0F);
		fire.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.BLAZE_ROD), 1.0F);
		fire.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.FIRE_CHARGE), 1.0F);
		fire.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.FIREWORK_CHARGE), 1.0F);
		fire.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.FIREWORKS), 1.0F);
		fire.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.MAGMA_CREAM), 1.0F);
		fire.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.FLINT_AND_STEEL), 1.0F);
		fire.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.LAVA_BUCKET), 1.0F);

		water.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.WATER_BUCKET), 1.0F);
		water.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.CLAY_BALL), 0.5F);

		wind.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.FEATHER), 1.0F);
		wind.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.ELYTRA), 1.0F);

		dirt.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.CLAY_BALL), 0.5F);

		thunder.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.REDSTONE), 1.0F);

		holy.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.ENCHANTED_BOOK), 1.0F);

		dark.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.ENDER_PEARL), 1.0F);
		dark.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.ENDER_EYE), 1.0F);
		dark.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.CHORUS_FRUIT), 1.0F);
		dark.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.CHORUS_FRUIT_POPPED), 1.0F);

		poison.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.FERMENTED_SPIDER_EYE), 1.0F);
		poison.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.POISONOUS_POTATO), 1.0F);

		hunger.setDefaultValue(dam, ItemHelper.getUnlocalizedName(Items.MILK_BUCKET), -0.25F);

		
		Item weapons[][] =
			{
				{Items.WOODEN_SWORD, Items.STONE_SWORD, Items.GOLDEN_SWORD, Items.IRON_SWORD, Items.DIAMOND_SWORD},
				{Items.WOODEN_AXE, Items.STONE_AXE, Items.GOLDEN_AXE, Items.IRON_AXE, Items.DIAMOND_AXE},
				{Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.GOLDEN_PICKAXE, Items.IRON_PICKAXE, Items.DIAMOND_PICKAXE},
				{Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, Items.GOLDEN_SHOVEL, Items.IRON_SHOVEL, Items.DIAMOND_SHOVEL},
				{Items.WOODEN_HOE, Items.STONE_HOE, Items.GOLDEN_HOE, Items.IRON_HOE, Items.DIAMOND_HOE}
			};
		for(Item[] i : weapons) // 0:WOODEN,1:STONE,2:GOLDEN,3:IRON,4:DIAMOND
		{
			normal.setDefaultValue(dam, ItemHelper.getUnlocalizedName(i[0]), 0.75F);
			wood.setDefaultValue(dam, ItemHelper.getUnlocalizedName(i[0]), 0.25F);
			normal.setDefaultValue(dam, ItemHelper.getUnlocalizedName(i[1]), 0.75F);
			dirt.setDefaultValue(dam, ItemHelper.getUnlocalizedName(i[1]), 0.25F);
			normal.setDefaultValue(dam, ItemHelper.getUnlocalizedName(i[2]), 0.75F);
			fire.setDefaultValue(dam, ItemHelper.getUnlocalizedName(i[2]), 0.25F);
			normal.setDefaultValue(dam, ItemHelper.getUnlocalizedName(i[3]), 1.0F);
			normal.setDefaultValue(dam, ItemHelper.getUnlocalizedName(i[4]), 1.0F);
		}

		for(Block block : Block.REGISTRY)
		{
			Item item = Item.getItemFromBlock(block);
			if(item != null)
			{
				Material checkedMaterial = block.getMaterial(null);
				Element elementType;
				if(checkedMaterial == Material.FIRE || checkedMaterial == Material.LAVA)
					elementType = fire;
				else if(checkedMaterial == Material.ICE || checkedMaterial == Material.CRAFTED_SNOW || checkedMaterial == Material.SNOW || checkedMaterial == Material.PACKED_ICE)
					elementType = water;
				else if(checkedMaterial == Material.PLANTS || checkedMaterial == Material.WOOD)
					elementType = wood;
				else
					elementType = normal;
					
				elementType.setDefaultValue(dam, ItemHelper.getUnlocalizedName(block), 0.5F);
			}
		}

		// SHI
		// leather
		water.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.LEATHER_BOOTS),
				ShieldMatrix.getDamageReductionByMaterial(Items.LEATHER_BOOTS) * 0.5F);
		water.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.LEATHER_CHESTPLATE),
				ShieldMatrix.getDamageReductionByMaterial(Items.LEATHER_CHESTPLATE) * 0.5F);
		water.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.LEATHER_HELMET),
				ShieldMatrix.getDamageReductionByMaterial(Items.LEATHER_HELMET) * 0.5F);
		water.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.LEATHER_LEGGINGS),
				ShieldMatrix.getDamageReductionByMaterial(Items.LEATHER_LEGGINGS) * 0.5F);
		
		poison.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.LEATHER_BOOTS),
				ShieldMatrix.getDamageReductionByMaterial(Items.LEATHER_BOOTS) * 0.1F);
		poison.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.LEATHER_CHESTPLATE),
				ShieldMatrix.getDamageReductionByMaterial(Items.LEATHER_CHESTPLATE) * 0.1F);
		poison.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.LEATHER_HELMET),
				ShieldMatrix.getDamageReductionByMaterial(Items.LEATHER_HELMET) * 0.1F);
		poison.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.LEATHER_LEGGINGS),
				ShieldMatrix.getDamageReductionByMaterial(Items.LEATHER_LEGGINGS) * 0.1F);

		thunder.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.LEATHER_BOOTS),
				ShieldMatrix.getDamageReductionByMaterial(Items.LEATHER_BOOTS) * 0.5F);
		thunder.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.LEATHER_CHESTPLATE),
				ShieldMatrix.getDamageReductionByMaterial(Items.LEATHER_CHESTPLATE) * 0.5F);
		thunder.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.LEATHER_HELMET),
				ShieldMatrix.getDamageReductionByMaterial(Items.LEATHER_HELMET) * 0.5F);
		thunder.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.LEATHER_LEGGINGS),
				ShieldMatrix.getDamageReductionByMaterial(Items.LEATHER_LEGGINGS) * 0.5F);
		
		// golden
		fire.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.GOLDEN_BOOTS),
				ShieldMatrix.getDamageReductionByMaterial(Items.GOLDEN_BOOTS) * 0.5F);
		fire.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.GOLDEN_CHESTPLATE),
				ShieldMatrix.getDamageReductionByMaterial(Items.GOLDEN_CHESTPLATE) * 0.5F);
		fire.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.GOLDEN_HELMET),
				ShieldMatrix.getDamageReductionByMaterial(Items.GOLDEN_HELMET) * 0.5F);
		fire.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.GOLDEN_LEGGINGS),
				ShieldMatrix.getDamageReductionByMaterial(Items.GOLDEN_LEGGINGS) * 0.5F);
		
		water.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.GOLDEN_BOOTS),
				ShieldMatrix.getDamageReductionByMaterial(Items.GOLDEN_BOOTS) * 1.25F);
		water.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.GOLDEN_CHESTPLATE),
				ShieldMatrix.getDamageReductionByMaterial(Items.GOLDEN_CHESTPLATE) * 1.25F);
		water.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.GOLDEN_HELMET),
				ShieldMatrix.getDamageReductionByMaterial(Items.GOLDEN_HELMET) * 1.25F);
		water.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.GOLDEN_LEGGINGS),
				ShieldMatrix.getDamageReductionByMaterial(Items.GOLDEN_LEGGINGS) * 1.25F);
		
		thunder.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.GOLDEN_BOOTS),
				ShieldMatrix.getDamageReductionByMaterial(Items.GOLDEN_BOOTS) * 2.0F);
		thunder.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.GOLDEN_CHESTPLATE),
				ShieldMatrix.getDamageReductionByMaterial(Items.GOLDEN_CHESTPLATE) * 2.0F);
		thunder.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.GOLDEN_HELMET),
				ShieldMatrix.getDamageReductionByMaterial(Items.GOLDEN_HELMET) * 2.0F);
		thunder.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.GOLDEN_LEGGINGS),
				ShieldMatrix.getDamageReductionByMaterial(Items.GOLDEN_LEGGINGS) * 2.0F);

		// iron
		thunder.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.IRON_BOOTS),
				ShieldMatrix.getDamageReductionByMaterial(Items.IRON_BOOTS) * 2.0F);
		thunder.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.IRON_CHESTPLATE),
				ShieldMatrix.getDamageReductionByMaterial(Items.IRON_CHESTPLATE) * 2.0F);
		thunder.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.IRON_HELMET),
				ShieldMatrix.getDamageReductionByMaterial(Items.IRON_HELMET) * 2.0F);
		thunder.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.IRON_LEGGINGS),
				ShieldMatrix.getDamageReductionByMaterial(Items.IRON_LEGGINGS) * 2.0F);
	
		// chianmail
		fire.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_BOOTS),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_BOOTS) * 0.75F);
		fire.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_CHESTPLATE),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_CHESTPLATE) * 0.75F);
		fire.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_HELMET),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_HELMET) * 0.75F);
		fire.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_LEGGINGS),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_LEGGINGS) * 0.75F);
		
		water.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_BOOTS),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_BOOTS) * 0.75F);
		water.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_CHESTPLATE),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_CHESTPLATE) * 0.75F);
		water.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_HELMET),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_HELMET) * 0.75F);
		water.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_LEGGINGS),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_LEGGINGS) * 0.75F);
		
		dirt.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_BOOTS),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_BOOTS) * 1.25F);
		dirt.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_CHESTPLATE),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_CHESTPLATE) * 1.25F);
		dirt.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_HELMET),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_HELMET) * 1.25F);
		dirt.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_LEGGINGS),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_LEGGINGS) * 1.25F);
	
		wind.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_BOOTS),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_BOOTS) * 1.25F);
		wind.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_CHESTPLATE),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_CHESTPLATE) * 1.25F);
		wind.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_HELMET),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_HELMET) * 1.25F);
		wind.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_LEGGINGS),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_LEGGINGS) * 1.25F);
		
		thunder.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_BOOTS),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_BOOTS) * 0.75F);
		thunder.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_CHESTPLATE),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_CHESTPLATE) * 0.75F);
		thunder.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_HELMET),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_HELMET) * 0.75F);
		thunder.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.CHAINMAIL_LEGGINGS),
				ShieldMatrix.getDamageReductionByMaterial(Items.CHAINMAIL_LEGGINGS) * 0.75F);
		
		// diamond
		fire.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.DIAMOND_BOOTS),
				ShieldMatrix.getDamageReductionByMaterial(Items.DIAMOND_BOOTS) * 1.25F);
		fire.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.DIAMOND_CHESTPLATE),
				ShieldMatrix.getDamageReductionByMaterial(Items.DIAMOND_CHESTPLATE) * 1.25F);
		fire.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.DIAMOND_HELMET),
				ShieldMatrix.getDamageReductionByMaterial(Items.DIAMOND_HELMET) * 1.25F);
		fire.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Items.DIAMOND_LEGGINGS),
				ShieldMatrix.getDamageReductionByMaterial(Items.DIAMOND_LEGGINGS) * 1.25F);
		
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
		
		//Register.addNewWeaponMatrix(Items.IRON_SWORD, new DamageMatrix());

	}
	

}
