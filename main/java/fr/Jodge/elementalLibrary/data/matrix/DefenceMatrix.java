package fr.Jodge.elementalLibrary.data.matrix;

import java.util.ArrayList;
import java.util.List;

import scala.actors.threadpool.Arrays;
import io.netty.buffer.ByteBuf;
import fr.Jodge.elementalLibrary.Element;
import fr.Jodge.elementalLibrary.ElementalConstante;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.interfaces.IMonsterMatrix;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;

public class DefenceMatrix extends ElementalMatrix
{

	public DefenceMatrix()
	{
		this(1.0F);
	}
	public DefenceMatrix(float base)
	{
		super(base);
	}	

	public DefenceMatrix(List<Float> matrix)
	{
		super(matrix);
	}
	
	public DefenceMatrix(Float[] valueMatrix)
	{
		super(valueMatrix);
	}
	
	@Override
	public void autoUptdate(Entity target)
	{
		if(target instanceof IMonsterMatrix)
		{
			matrix = ((IMonsterMatrix)target).getResistMatrix().matrix;
			return;
		}
		
		// base (most of the monster are ground monster so we add a bonus to dirt element ans reduce if needed)
		matrix.set(Element.DIRT, 1.1F);
		
		// FROM HERE MONSTER
		if(target instanceof EntityBlaze)
		{
			matrix.set(Element.DIRT, 0.25F);
			matrix.set(Element.WIND, 2.0F);
			matrix.set(Element.WATER, 2.5F);
			matrix.set(Element.FIRE, -1.0F);
			matrix.set(Element.WOOD, 0.5F);
		}
		else if(target instanceof EntityCaveSpider)
		{
			
		}
		else if(target instanceof EntityCreeper)
		{
			matrix.set(Element.THUNDER, 0.0F);
		}
		else if(target instanceof EntityEnderman)
		{
			matrix.set(Element.HOLY, 1.5F);
			matrix.set(Element.DARK, 0.0F);
		}
		else if(target instanceof EntityEndermite)
		{
			matrix.set(Element.DARK, -1.0F);
		}
		else if(target instanceof EntityGhast)
		{
			matrix.set(Element.DIRT, 0.25F);
			matrix.set(Element.WIND, 1.75F);
			matrix.set(Element.HOLY, 1.1F);
			matrix.set(Element.NORMAL, 0.5F);
		}
		else if(target instanceof EntityGiantZombie)
		{
			matrix.set(Element.DIRT, 1.25F);
		}
		else if(target instanceof EntityGolem)
		{
			matrix.set(Element.DIRT, -0.25F);
			matrix.set(Element.WIND, 0.25F);
		}
		else if(target instanceof EntityGuardian)
		{
			matrix.set(Element.DIRT, 1.0F);
			matrix.set(Element.FIRE, 0.0F);
			matrix.set(Element.WATER, -1.0F);
			matrix.set(Element.THUNDER, 2.0F);
		}
		else if(target instanceof EntityMagmaCube)
		{
			matrix.set(Element.WATER, 2.0F);
			matrix.set(Element.FIRE, 0.0F);
		}
		else if(target instanceof EntityPigZombie)
		{
			matrix.set(Element.WATER, 1.1F);
			matrix.set(Element.FIRE, 0.0F);
			matrix.set(Element.DARK, 0.5F);
			matrix.set(Element.HOLY, 1.5F);
		}
		else if(target instanceof EntityShulker)
		{
			matrix.set(Element.HOLY, 1.25F);

		}
		else if(target instanceof EntitySilverfish)
		{
			matrix.set(Element.DIRT, -0.1F);

		}
		else if(target instanceof EntitySkeleton)
		{
			matrix.set(Element.DARK, 0.5F);
			matrix.set(Element.HOLY, 1.5F);
		}
		else if(target instanceof EntitySlime)
		{
			matrix.set(Element.WATER, 0.75F);
		}
		else if(target instanceof EntitySnowman)
		{
			matrix.set(Element.WATER, -5.0F);
			matrix.set(Element.FIRE, 5.0F);
		}
		else if(target instanceof EntitySpider)
		{

		}
		else if(target instanceof EntityWitch)
		{
			matrix.set(Element.HOLY, 0.75F);
			matrix.set(Element.DARK, 0.75F);
		}
		else if(target instanceof EntityZombie)
		{
			matrix.set(Element.DARK, 0.5F);
			matrix.set(Element.HOLY, 1.5F);
		}
		// FROM HERE BANAL MOB
		else if(target instanceof EntityBat)
		{
			matrix.set(Element.DIRT, 0.25F);
			matrix.set(Element.WIND, 1.75F);
		}
		else if(target instanceof EntityChicken)
		{
			matrix.set(Element.DIRT, 0.50F);
			matrix.set(Element.WIND, 1.50F);
		}
		else if(target instanceof EntityCow)
		{
			matrix.set(Element.FIRE, 1.1F);
		}
		else if(target instanceof EntityHorse)
		{
			matrix.set(Element.FIRE, 1.1F);
		}
		else if(target instanceof EntityMooshroom)
		{
			matrix.set(Element.FIRE, 1.1F);
		}
		else if(target instanceof EntityOcelot)
		{
			matrix.set(Element.FIRE, 1.1F);
		}
		else if(target instanceof EntityPig)
		{

		}
		else if(target instanceof EntityRabbit)
		{
			matrix.set(Element.FIRE, 1.1F);
		}
		else if(target instanceof EntitySheep)
		{
			matrix.set(Element.FIRE, 1.25F);
		}
		else if(target instanceof EntitySquid)
		{
			matrix.set(Element.FIRE, 0.0F);
			matrix.set(Element.WATER, 0.5F);
		}
		else if(target instanceof EntityVillager)
		{

		}
		else if(target instanceof EntityWolf)
		{
			matrix.set(Element.FIRE, 1.1F);
		}
		// FROM HERE BOSS // TODO
		else if (target instanceof EntityDragon)
		{
			matrix.set(Element.FIRE, 0.0F);
			matrix.set(Element.WATER, 1.5F);
			matrix.set(Element.DIRT, 0.0F);
			matrix.set(Element.WIND, 1.25F);
			matrix.set(Element.NORMAL, 0.5F);
		}
		else if (target instanceof EntityWither)
		{
			matrix.set(Element.DARK, 0.75F);
			matrix.set(Element.HOLY, 1.25F);
			matrix.set(Element.DIRT, 0.0F);
			matrix.set(Element.WIND, 1.25F);
			matrix.set(Element.NORMAL, 0.5F);
		}
		
		// for resistance we also use entity site
		if(target.height < 1.0F)
			matrix.set(Element.WATER, matrix.get(Element.WATER) * 0.9F );
				
	}

}
