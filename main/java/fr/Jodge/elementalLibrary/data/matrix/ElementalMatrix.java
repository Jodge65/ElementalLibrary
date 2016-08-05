package fr.Jodge.elementalLibrary.data.matrix;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.google.common.base.Optional;
import com.google.gson.JsonObject;

import fr.Jodge.elementalLibrary.data.element.Element;
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
	protected Map<Element, Float> matrix;
	/**
	 * The default constructor will give a basic 1.0 matrix to every entity
	 */
	public ElementalMatrix()
	{
		this(0.0F);
	}
	public ElementalMatrix(float base)
	{
		this.matrix = new HashMap<Element, Float>();
		completeArray(base);
	}	

	public ElementalMatrix(Map<Element, Float> matrix)
	{
		this.matrix = new HashMap<Element, Float>(matrix);
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
		// for each key in Element
		for(Element element : Element.getAllElement())
		{
			if(!matrix.containsKey(element))
				matrix.put(element, defaultValue);
		}
		return this;
	}
	
	public Map<Element, Float> getMatrix()
	{
		return this.matrix;
	}
	
	/**
	 * (matrix is supposed already full, so their are not add statement available)
	 * wrong tentative will not delete 
	 * 
	 * @param index <i>int</i> index you want to change. you can also put and Element instead
	 * @param value <i>float</i> value that you want to add to this matrix
	 * @return itself
	 */
	public ElementalMatrix set(int index, float value)
	{
		return set(Element.findById(index), value);
	}
	public ElementalMatrix set(Element index, float value)
	{
		matrix.put(index, value);
		return this;
	}
	
	
	public Float get(int index)
	{
		return get(Element.findById(index));
	}
	public Float get(Element index)
	{
		return matrix.getOrDefault(index, null);
	}
	
	@Override
	public String toString()
	{
		String text = "";
		
		for(Entry<Element, Float> entry : matrix.entrySet())
		{
			text += entry.getKey();
			text += ":";
			text += entry.getValue();
			text += ";";
		}
		
		text += "";
		return text;
	}
	
	@Override
	public void fromJsonObject(JsonObject j)
	{
		for(Element element : Element.getAllElement())
		{
			String key = element.getName();
			if(j.has(key))
				this.set(element, j.get(key).getAsFloat());
		}
		completeArray();
	}
	
	@Override
	public JsonObject toJsonObject()
	{
		JsonObject j = new JsonObject();
		for(Element element : Element.getAllElement())
		{
			j.addProperty(element.getName(), matrix.get(element));
		}
		
		return j;
	}
	
	@Override
	public void toByte(ByteBuf buf) 
	{
		/* Value write in buffer : 
		 * Int => number of item (length)
		 * ----------------------------------
		 * Int => index for next value
		 * Float => value 
		 * ----------------------------------
		 * repeat 'length' time
		 */
		
		int length = matrix.size();
		buf.writeInt(length);
		for(Entry<Element, Float> value : matrix.entrySet())
		{
			buf.writeInt(value.getKey().getId());
			buf.writeFloat(value.getValue());
		}
	}
	

	@Override
	public void fromByte(ByteBuf buf) 
	{
		int length = buf.readInt();
		for(int i = 0; i < length; i++)
		{
			int key = buf.readInt();
			float value = buf.readFloat();
			this.matrix.put(Element.findById(key), value);
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
	public float getDamageFromEntity(EntityLivingBase entity, float oldValue)
	{
		float baseDamage = 0.0F;
		
		if(entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE) != null)
		{
			baseDamage = (float)entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		}
		else
		{
			JLog.warning("No attribut Damage available... Old value will be used... ");
			//TODO find a better way...
			return oldValue;
		}
		
		return baseDamage;
	}
	
	/**
	 * 
	 * @param target
	 * @param clazz
	 */
	protected void updateEntity(Entity target, Class<? extends ElementalMatrix> clazz) 
	{
		boolean atLeastOne = false;
		for(Element element : Element.getAllElement())
		{
			// Warning : Float is an object, so it can be null.
			Float value = element.getDefaultValue(clazz, target.getClass(), matrix.get(element));
			if(value != null)
			{
				// if we have a value, we put it.
				matrix.put(element, value);
				if(element.isActive())
					atLeastOne = true;
			}
		}
		
		if(!atLeastOne)
		{
			matrix.put(Element.findById(0), 1.0F);
		}
	}
}
