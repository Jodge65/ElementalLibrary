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

public class FinalMatrix extends ElementalMatrix
{
	protected float currentDamage = 0.0F;
	protected float currentHeal = 0.0F;
	
	public FinalMatrix()
	{
		this(1.0F);
	}
	public FinalMatrix(float base)
	{
		super(base);
		doCalculation();
	}	

	public FinalMatrix(List<Float> matrix)
	{
		super(matrix);
		doCalculation();
	}
	
	public FinalMatrix(Float[] valueMatrix)
	{
		super(valueMatrix);
		doCalculation();
	}
	
	protected FinalMatrix doCalculation()
	{
		for(float value : matrix)
		{
			if(value > 0.0F)
				currentDamage += value;
			else
				currentHeal += value;
		}
		
		return this;
	}
	
	/** @return </i>float</i> total damage */
	public float getTotalDamage(){return currentDamage;}
	/** @return </i>float</i> total heal */
	public float getTotalHeal(){return currentHeal;}
	
	@Override
	public void autoUptdate(Entity target)
	{
		
	}

}
