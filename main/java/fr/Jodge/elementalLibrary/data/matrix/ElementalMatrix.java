package fr.Jodge.elementalLibrary.data.matrix;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.base.Optional;
import com.google.gson.JsonObject;

import fr.Jodge.elementalLibrary.Element;
import fr.Jodge.elementalLibrary.ElementalConstante;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.function.JLog;
import scala.Int;
import scala.actors.threadpool.Arrays;
import scala.util.parsing.json.JSON;
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
public abstract class ElementalMatrix implements IElementalWritable
{

	/** number of element is unknown or can change during process, so we use a list */
	protected List<Float> matrix;
	
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
	
	public List<Float> getMatrix()
	{
		return this.matrix;
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

	@Override
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
	
	@Override
	public void fromJsonObject(JsonObject j)
	{
		for(String key: Element.getKeyName())
		{
			if(j.has(key))
				this.set(Element.getIndex(key), j.get(key).getAsFloat());
		}
		completeArray();
	}
	
	@Override
	public JsonObject toJsonObject()
	{
		JsonObject j = new JsonObject();
		for(int i = 0; i < matrix.size(); i++)
		{
			j.addProperty(Element.getKey(i), matrix.get(i));
		}
		
		return j;
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
	public void fromByte(ByteBuf buf) 
	{
		int length = buf.readInt();
		for(int i = 0; i < length; i++)
		{
			this.matrix.add(i, buf.readFloat());
		}
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
	 * 
	 * @param entity <i>Entity</i>
	 * @return
	 */
	public float getDamageFromEntity(EntityLivingBase entity)
	{
		float baseDamage = 0.0F;
		
		if(entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE) != null)
		{
			baseDamage = (float)entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
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
				baseDamage = 7 + new Random().nextInt(15);
			}
			else if(entity instanceof EntityShulker)
			{
				baseDamage = 1 + new Random().nextInt(3);
			}
			else
			{
				baseDamage = 5 + new Random().nextInt(5);
				JLog.alert("Entity " + entity.getClass() + " is not a reference to a know vanilla Golem. A Random value between 5 and 10 will be used.");
			}
		}
		
		return baseDamage;
	}
}
