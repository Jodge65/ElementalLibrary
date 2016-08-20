package fr.Jodge.elementalLibrary.data.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonObject;

import scala.actors.threadpool.Arrays;
import io.netty.buffer.ByteBuf;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.register.Getter;
import fr.Jodge.elementalLibrary.data.stats.ItemStats;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;

public class DamageMatrix extends ElementalMatrix
{
	protected float totalDamage; // maybe on heal and not damage /!\
	protected boolean isDirty;
	
	public DamageMatrix()
	{
		super(0.0F);
		totalDamage = 0.0F;
		isDirty = true;
	}
	@Override
	public DamageMatrix clone()
	{
		DamageMatrix newMatrix = new DamageMatrix();
		for(Entry<Element, Float> entry : this.matrix.entrySet())
		{
			newMatrix.set(entry.getKey(), entry.getValue());
		}
		return newMatrix;
	}
	
	@Override
	public void autoUptdate(Object obj)
	{
		if(obj instanceof ItemStack)
		{
			updateItem((ItemStack)obj, this.getClass());
		}
		unDirty();
	}
	
	public DamageMatrix autoUpdateHand(EntityLivingBase entity) 
	{
		ElementalMatrix atkMatrix = entity.getDataManager().get(Getter.getDataKeyForEntity(entity, AttackMatrix.class));

		Element bestBonus = Element.findById(0);

		for(Element element : Element.getAllActiveElement())
		{
			if(atkMatrix.get(bestBonus) < atkMatrix.get(element))
				bestBonus = element;
		}
		matrix.put(bestBonus, 1.0F);

		return this;
	}
	
	public void autoUpdate(EntityLivingBase entity, float oldValue) 
	{
		totalDamage = getDamageFromEntity(entity, oldValue);
		unDirty();
	}
	
	@Override
	public String toString()
	{
		return super.toString() + " - (" + totalDamage + ")";
	}
		
	@Override
	public JsonObject toJsonObject()
	{
		JsonObject j = super.toJsonObject();
		j.addProperty("totalDamage", totalDamage);
		return j;
	}
	@Override
	public void fromJsonObject(JsonObject j)
	{
		super.fromJsonObject(j);
		totalDamage = j.get("totalDamage").getAsFloat();
		unDirty();
	}
	
	@Override
	public void toByte(ByteBuf buf) 
	{
		super.toByte(buf);
		buf.writeFloat(totalDamage);
	}
	
	@Override
	public void fromByte(ByteBuf buf) 
	{
		super.fromByte(buf);
		totalDamage = buf.readFloat();
		unDirty();
	}
	
	@Override
	public ElementalMatrix set(int index, float value)
	{
		return set(Element.findById(index), value);
	}
	@Override
	public ElementalMatrix set(Element index, float value)
	{
		super.set(index, value);
		unDirty();
		return this;
	}
	
	/**
	 * adapt matrix to have a total bonus of 1.0F : 
	 * heal can be equal to 0.0F.
	 * If heal is != 0.0F, then damage can be equal to 0.0F.
	 * if nothing is set, then damage = 1.0F.
	 */
	public void unDirty()
	{
		float total = getTotalDamage();
		if(total == 0.0F)
		{
			// if nothing, then damage normal
			matrix.put(Element.findById(0), 1.0F);
		}
		else 
		{
			if(total != 1.0F)
			{
				// here, total damage is over 1.0F
				for(Entry<Element, Float> value : this.matrix.entrySet())
				{
					if(value.getValue() != 0.0F)
					{
						float newValue = (value.getValue() * 1.0F) / total;
						matrix.put(value.getKey(), newValue);
					}
				}
			}
		}

		
		isDirty = false;
	}
	
	/**
	 * Total amount of positive value in matrix
	 * @return <i>float</i>
	 */
	public float getTotalDamage()
	{
		float bonus = 0.0F;
		for(float value : this.matrix.values())
		{
			if(value > 0.0F)
				bonus += value;
			else if(value < 0.0F)
				bonus -= value; // we want a positive total, so each time, we add the negate value that is already negate : - + - = +
		
		}
		return bonus;
	}

	
	@Override
	public float get(int index)
	{
		return super.get(index) * totalDamage;
	}
	@Override
	public float get(Element index)
	{
		return super.get(index) * totalDamage;
	}	
	
}
