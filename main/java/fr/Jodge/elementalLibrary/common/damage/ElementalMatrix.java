package fr.Jodge.elementalLibrary.common.damage;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.base.Optional;

import fr.Jodge.elementalLibrary.common.Element;
import fr.Jodge.elementalLibrary.common.ElementalConstante;
import fr.Jodge.elementalLibrary.common.data.IElementalWritable;
import fr.Jodge.elementalLibrary.common.function.JLog;
import scala.Int;
import scala.actors.threadpool.Arrays;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.math.BlockPos;

/**
 * What is that ?
 * 
 * Matrix is a simple one line tab that give the '+' (or '-') give to an element.
 * They are 2 kind of matrix in each living entity :
 * - resistMatrix
 * - atkMatrix
 * 
 * resist matrix is applied when the entity is hit. 
 * It's a multiplier of damage taken. 
 * That is to say that a monster whit 0.5 will receive only 50% of total damage.
 * More high was this value, more high will be the damage.
 * 
 * 
 * atk matrix is applied when and entity hit.
 * Its also a multiplier but this time about damage given
 * That is to say that an entity whit 1.5 will deal 50% more damage.
 * More this value is around 0, less will be the damage.
 * 
 * 
 * @author Jodge
 * @version 1.0.0
 */
public class ElementalMatrix implements IElementalWritable
{
	/** number of element is unknown or can change during process, so we use a list */
	private List<Float> matrix;
	
	/**
	 * The default constructor will give a basic 1.0 matrix to every entity
	 */
	public ElementalMatrix()
	{
		this(1.0F);
	}
	public ElementalMatrix(float base)
	{
		this.matrix = new ArrayList<Float>();
		completeArray(base);
	}	
	/**
	 * this constructor will use the list given and add 1.0 to each other unknown elements
	 * @param matrix <i>ArrayList<Float></i> matrix that will be use.
	 */
	public ElementalMatrix(List<Float> matrix)
	{
		this.matrix = matrix;
		completeArray();
	}
	
	public ElementalMatrix(Float[] valueMatrix)
	{
		this.matrix = new ArrayList<Float>(Arrays.asList(valueMatrix));
		completeArray();
	}
	
	/**
	 * Don't use this constructor please ! you have like... over 9000 % to bug everything ^^'
	 * @param matrix <i>String</i> (yes seriously)
	 */
	public ElementalMatrix(String matrix)
	{
		this.matrix = new ArrayList<Float>();
		String[] ElementValue = matrix.split(";");
		
		for(String value: ElementValue)
		{
			this.matrix.add(Float.parseFloat(value));
		}
		completeArray();
	}

	/**
	 * private method that finish to initialize array.
	 * It's suppose to be call when entity spawn, so if you add element, their will note need to use it
	 */
	protected ElementalMatrix completeArray()
	{
		return completeArray(1.0F);
	}
	protected ElementalMatrix completeArray(float defaultValue)
	{
		for(int index = matrix.size(); index < Element.getNumberOfElement(); index ++)
		{
			matrix.add(defaultValue);
		}
		return this;
	}
	
	/**
	 * (matrix is supposed already full, so their are not add statement available)
	 * wrong tentative will not delete 
	 * 
	 * @param index <i>int</i> index you want to change. Use Element.getIndex(String) or Element.constante to get the correct index.
	 * @param value <i>float</i> value that you want to add to this matrix
	 * @return itself
	 */
	public ElementalMatrix set(int index, float value)
	{
		if(matrix.size() > index)
			matrix.set(index, value);
		else
			JLog.alert("Index " + index + "does not exist in matrix " + this.toString());
		return this;
	}
	
	public Float get(int index)
	{
		if(matrix.size() < index)
			return null;
		else
			return matrix.get(index);
	}

	public String toString()
	{
		String text = "";
		
		for(int i = 0; i < matrix.size(); i++)
		{
			text += Element.getKey(i);
			text += ":";
			text += matrix.get(i);
			text += ";";
		}
		
		text += "";
		return text;
	}
	
	
// CONDITION ----------------------------------------------------------------------------------------

	/**
	 * 
	 * @param target <i>Entity</i> Entity we want to determine if it's a FUCKING ZOMBIE !!!
	 * @return <i>boolean</i> true if creatureAtrribute is "undead"
	 */
	public static boolean isUndead(Entity target) 
	{
		if(target instanceof EntityLivingBase)
			if(((EntityLivingBase)target).getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)
				return true;

		return false;
	}
	
	
	
// UPTDATE ----------------------------------------------------------------------------------------
	/**
	 * All this function was study for VANILLA mob ! 
	 * if your mod add a water dragon, then don't use dragonMatrix : it add 200% vulnerability to water...
	 * It's better to never use then : their have been create for autoUpdate when no Matrix are available
	 * 
	 * How do it's work :
	 * Atk is use to multiply damage deal by element.
	 * Resist is use to divide damage deal by element.
	 * 
	 * negative value = heal
	 */
	
	
	public ElementalMatrix autoUpdateResist(Entity target)
	{
		// base (most of the monster are ground monster so we add a bonus to dirt element)
		matrix.set(Element.NORMAL, 0.75F);
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
		// FROM HERE BOSS
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
		
		return this;
	}
	public ElementalMatrix autoUpdateAtk(Entity target)
	{
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
	
		return this;
	}
	
	/**
	 * 
	 * @param entity <i>Entity</i>
	 * @return
	 */
	public float getDamageFromEntity(EntityLivingBase entity)
	{
		float baseDamage = 0.0F;
		
		if(((EntityLivingBase) entity).getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE) != null)
		{
			baseDamage = (float)((EntityLivingBase) entity).getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		}
		else
		{
			// here we have some problem...  Damage is write under code, and is unreachable...
			if(entity instanceof EntitySnowman)
			{
				baseDamage = 0.0F;
			}
			else if(entity instanceof EntityIronGolem)
			{
				baseDamage = (float)(7 + new Random().nextInt(15));
			}
			else if(entity instanceof EntityShulker)
			{
				baseDamage = (float)(1 + new Random().nextInt(3));
			}
			else
			{
				baseDamage = (float)(5 + new Random().nextInt(5));
				JLog.alert("Entity " + entity.getClass() + " is not a reference to a know vanilla Golem. A Random value between 5 and 10 will be used.");
			}
		}
		
		return baseDamage;
	}
	
	public void autoUpdateDamage(EntityLivingBase entity) 
	{
		matrix.set(Element.NORMAL, getDamageFromEntity(entity));
	}
	
	public void autoUpdateDamageHand(EntityLivingBase entity) 
	{
		float baseDamage = getDamageFromEntity(entity);
		
		ElementalMatrix atkMatrix = entity.getDataManager().get(ElementalConstante.getDataKeyForEntity((EntityLivingBase)entity, ElementalConstante.DATA_ATK));

		int bestBonus = Element.NORMAL;
		for(int i = 0; i < Element.getNumberOfElement(); i++)
		{
			if(atkMatrix.get(bestBonus) < atkMatrix.get(i))
				bestBonus = i;
		}
		matrix.set(bestBonus, baseDamage);

	}
	
	
	
	// TODO
	
	public String write() 
	{
		String text = "";
		
		for(int i = 0; i < matrix.size(); i++)
		{
			text += matrix.get(i);
			text += ";";
		}
		
		text += "";
		return text;
	}
	
	public IElementalWritable createByString(String value) 
	{
		return new ElementalMatrix(value);
	}
	@Override
	public void toByte(ByteBuf buf) 
	{
		int length = matrix.size();
		buf.writeInt(length);
		for(float value : matrix)
		{
			buf.writeFloat(value);
		}
		
	}
	
	@Override
	public IElementalWritable fromByte(ByteBuf buf) 
	{
		int length = buf.readInt();
		List<Float> tab = new ArrayList(length);
		for(int i = 0; i < length; i++)
		{
			tab.add(i, buf.readFloat());
		}
		
		return new ElementalMatrix(tab);
	}
}
