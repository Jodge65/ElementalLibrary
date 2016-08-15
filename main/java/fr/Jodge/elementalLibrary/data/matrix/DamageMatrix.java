package fr.Jodge.elementalLibrary.data.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import scala.actors.threadpool.Arrays;
import io.netty.buffer.ByteBuf;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.register.Getter;
import fr.Jodge.elementalLibrary.function.JLog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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

public class DamageMatrix extends ElementalMatrix
{

	public DamageMatrix()
	{
		this(0.0F);
	}
	public DamageMatrix(float base)
	{
		super(base);
	}	

	public DamageMatrix(Map<Element, Float> matrix)
	{
		super(matrix);
	}
	
	@Override
	// TODO change for Item or Entity
	public void autoUptdate(Object obj){}
		
	public void autoUpdateDamage(EntityLivingBase entity, float oldValue) 
	{
		Element normal = Element.findById(0);
		matrix.put(normal, getDamageFromEntity(entity, oldValue));
	}
	
	public void autoUpdateDamageHand(EntityLivingBase entity, float oldValue) 
	{
		float baseDamage = getDamageFromEntity(entity, oldValue);
		
		ElementalMatrix atkMatrix = entity.getDataManager().get(Getter.getDataKeyForEntity(entity, AttackMatrix.class));

		Element bestBonus = Element.findById(0);

		for(Element element : Element.getAllActiveElement())
		{
			if(atkMatrix.get(bestBonus) < atkMatrix.get(element))
				bestBonus = element;
		}

		matrix.put(bestBonus, baseDamage);

	}
	
}
