package fr.Jodge.elementalLibrary.data.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import scala.actors.threadpool.Arrays;
import io.netty.buffer.ByteBuf;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.register.Getter;
import fr.Jodge.elementalLibrary.log.JLog;
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

	public DefenceMatrix(Map<Element, Float> matrix)
	{
		super(matrix);
	}

	@Override
	public void autoUpdate(Object obj)
	{
		
		if(obj instanceof Entity)
		{
			Entity target = (Entity)obj;
			DefenceMatrix defaultMatrix = Getter.getDefencePercentFromEntity(target);
			if(defaultMatrix != null)
			{
				// if defaultMatrix is not null, then we already have a default value.
				
				if(isCorrectMatrix(defaultMatrix.matrix))
					matrix = defaultMatrix.matrix;
				else
					JLog.error("Their are something wrong whit element use in this matrix...");
			}
			else
			{
				// if defaultMatrix is null, we don't have any default value... So we initialize it.
				updateEntity(target, this.getClass());	
			}// end of else no default value.
		}

	} // end of auto update	

}
