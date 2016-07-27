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

public class AttackMatrix extends ElementalMatrix
{

	public AttackMatrix()
	{
		this(1.0F);
	}
	public AttackMatrix(float base)
	{
		super(base);
	}	

	public AttackMatrix(List<Float> matrix)
	{
		super(matrix);
	}
	
	public AttackMatrix(Float[] valueMatrix)
	{
		super(valueMatrix);
	}

	@Override
	public void autoUptdate(Entity target)
	{
		if(target instanceof IMonsterMatrix)
		{
			matrix = ((IMonsterMatrix)target).getAtkMatrix().matrix;
			return;
		}
		
		
		matrix.set(Element.NORMAL, 0.75F);

		// FROM HERE MONSTER
		if(target instanceof EntityBlaze)
		{
			matrix.set(Element.FIRE, 1.5F);
		}
		else if(target instanceof EntityCaveSpider)
		{
			
		}
		else if(target instanceof EntityCreeper)
		{
			matrix.set(Element.FIRE, 1.1F);
		}
		else if(target instanceof EntityEnderman)
		{
			matrix.set(Element.DARK, 1.1F);
		}
		else if(target instanceof EntityEndermite)
		{
			matrix.set(Element.DARK, 1.1F);
		}
		else if(target instanceof EntityGhast)
		{
			matrix.set(Element.FIRE, 1.1F);
		}
		else if(target instanceof EntityGiantZombie)
		{
			matrix.set(Element.DIRT, 1.75F);
		}
		else if(target instanceof EntityGolem)
		{
			matrix.set(Element.DIRT, 1.75F);
		}
		else if(target instanceof EntityGuardian)
		{
			matrix.set(Element.WATER, 1.1F);
		}
		else if(target instanceof EntityMagmaCube)
		{
			matrix.set(Element.FIRE, 1.1F);
		}
		else if(target instanceof EntityPigZombie)
		{
			matrix.set(Element.FIRE, 1.1F);
		}
		else if(target instanceof EntityShulker)
		{
		}
		else if(target instanceof EntitySilverfish)
		{
		}
		else if(target instanceof EntitySkeleton)
		{
			matrix.set(Element.DARK, 1.1F);
		}
		else if(target instanceof EntitySlime)
		{
			matrix.set(Element.WATER, 1.1F);
		}
		else if(target instanceof EntitySnowman)
		{
		}
		else if(target instanceof EntitySpider)
		{
		}
		else if(target instanceof EntityWitch)
		{
		}
		else if(target instanceof EntityZombie)
		{
			matrix.set(Element.DARK, 1.1F);
		}
		// FROM HERE BANAL MOB
		else if(target instanceof EntityBat)
		{
		}
		else if(target instanceof EntityChicken)
		{
		}
		else if(target instanceof EntityCow)
		{
			matrix.set(Element.DIRT, 1.1F);
		}
		else if(target instanceof EntityHorse)
		{
			matrix.set(Element.DIRT, 1.1F);
		}
		else if(target instanceof EntityMooshroom)
		{
		}
		else if(target instanceof EntityOcelot)
		{
			matrix.set(Element.WIND, 1.1F);
		}
		else if(target instanceof EntityPig)
		{
		}
		else if(target instanceof EntityRabbit)
		{
		}
		else if(target instanceof EntitySheep)
		{
		}
		else if(target instanceof EntitySquid)
		{
			matrix.set(Element.WATER, 1.1F);
		}
		else if(target instanceof EntityVillager)
		{
		}
		else if(target instanceof EntityWolf)
		{
		}
		// FROM HERE BOSS
		else if (target instanceof EntityDragon)
		{
			matrix.set(Element.FIRE, 1.5F);
			matrix.set(Element.WIND, 0.75F);
		}
		else if (target instanceof EntityWither)
		{
			matrix.set(Element.DARK, 1.5F);
			matrix.set(Element.FIRE, 0.75F);
		}
	}

}
