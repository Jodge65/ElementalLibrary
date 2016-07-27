package fr.Jodge.elementalLibrary.data.matrix;

import java.util.ArrayList;
import java.util.List;

import scala.actors.threadpool.Arrays;
import io.netty.buffer.ByteBuf;
import fr.Jodge.elementalLibrary.Element;
import fr.Jodge.elementalLibrary.ElementalConstante;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class EnvironmentalMatrix extends ElementalMatrix
{

	public EnvironmentalMatrix()
	{
		this(1.0F);
	}
	
	public EnvironmentalMatrix(float base)
	{
		super(base);
	}	

	public EnvironmentalMatrix(List<Float> matrix)
	{
		super(matrix);
	}
	
	public EnvironmentalMatrix(Float[] valueMatrix)
	{
		super(valueMatrix);
	}
	
	@Override
	public void autoUptdate(Entity target)
	{
		BlockPos currentPos = new BlockPos(target);
		Biome biome = target.worldObj.getBiomeForCoordsBody(currentPos);
		
		adapteMatrixByTemperature(biome.getFloatTemperature(currentPos));
		adapteMatrixByRainfall(biome.getRainfall());
		
		if(target.isBurning())
			addOnFireMatrix();
		if(target.isImmuneToFire())
			addIsImmuneFireMatrix();
		if(target.isInWater())
			addIsOnWaterMatrix();
		if(target.isWet())
			addIsWetMatrix();
		if(target.onGround)
			addOnGroundMatrix();
		else
			addIsFlyingMatrix();
	}
	
	protected void adapteMatrixByRainfall(float rainfall) 
	{
		if(rainfall > 0.85F)
		{
			set(Element.WATER, get(Element.WATER) * (0.15F + rainfall));
			set(Element.FIRE, get(Element.FIRE) * (1.75F - rainfall));
		}
	}


	protected void adapteMatrixByTemperature(double temperature) 
	{
		if(temperature < 0.2D)
		{
			set(Element.WATER, get(Element.WATER) * (float)(1.0F + (0.20F - temperature)));
			set(Element.FIRE, get(Element.FIRE) * (float)(1.0F - (0.20F - temperature)));

		}
		else if(temperature > 1.0D)
		{
			set(Element.WATER, get(Element.WATER) * (float)(temperature - 0.20F));
			set(Element.FIRE, get(Element.FIRE) * (float)(temperature - 0.5F));
		}
		else // 0.2 < T° < 1.0 
		{
			
		}
			
	}

	public void addUndeadMatrix()
	{
		set(Element.DARK, get(Element.DARK) * -0.5F);
		set(Element.HOLY, get(Element.HOLY) * 2.0F);
	}
	
	public void addIsFlyingMatrix()
	{
		set(Element.DIRT, get(Element.WIND)*0.9F);
		set(Element.WIND, get(Element.WIND)*1.5F);
		set(Element.THUNDER, get(Element.THUNDER)*1.5F);
	}
	
	public void addOnUnderGroundMatrix()
	{
		set(Element.DIRT, get(Element.DIRT)*2.0F);
		set(Element.THUNDER, get(Element.WIND)*0.7F);
	}
	
	public void addOnGroundMatrix()
	{
		set(Element.DIRT, get(Element.DIRT)*1.1F);
		set(Element.THUNDER, get(Element.WIND)*0.9F);
	}
	
	public void addOnFireMatrix()
	{
		set(Element.FIRE, get(Element.FIRE)*0.8F);
		set(Element.WATER, get(Element.WATER)*1.2F);
		set(Element.WIND, get(Element.WIND)*1.2F);
	}
	
	public void addIsOnWaterMatrix()
	{
		set(Element.FIRE, 0.0F);
		set(Element.THUNDER, get(Element.THUNDER)*2.5F);
	}
	
	public void addIsWetMatrix()
	{
		set(Element.FIRE, get(Element.FIRE)*0.8F);
		set(Element.THUNDER, get(Element.THUNDER)*1.5F);
	}
	
	public void addIsImmuneFireMatrix()
	{
		set(Element.FIRE, 0.0F);
	}
}
