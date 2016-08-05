package fr.Jodge.elementalLibrary.data.element;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;

import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.network.BufUtils;
import fr.Jodge.elementalLibrary.data.register.Getter;
import fr.Jodge.elementalLibrary.data.register.Variable;
import fr.Jodge.elementalLibrary.function.JLog;

public class Element implements IElementalWritable
{
	protected static Map<String, Element> LIST_OF_ACTIVE_ELEMENT = new HashMap<String, Element>();
	protected static Map<String, Element> LIST_OF_UNACTIVE_ELEMENT = new HashMap<String, Element>();
	protected static Map<Integer, String> REF_INT_STRING = new HashMap<Integer, String>();
	private static int currentKey = 0;
	
	protected int id;
	protected String name;
	protected boolean isActive;
	protected Map<Class<? extends IElementalWritable>, Map<Class<? extends Entity>, Float>> defaultValue;
	protected Map<PotionEffect, Float> onDamageEffect, onHealEffect;
	
	protected boolean canAppliedFire;
	protected float fireProbability;
	protected int fireDuration;
	
	/**
	 * @see public static Element addOrGet(String name)
	 * @see public static Element findById(int id)
	 * 
	 * Don't use it. Use addOrGet and findById to get instance of element instead of create one new.
	 */
	@Deprecated
	public Element(){}
	
	protected Element(String name)
	{
		while(REF_INT_STRING.containsKey(currentKey))
		{
			currentKey++;
		} 
		construct(currentKey, name, true, false);
	}
	
	protected void construct(int id, String name, boolean isActive, boolean canAppliedFire)
	{
		this.id = id;
		this.name = name;
		this.isActive = isActive;
		this.LIST_OF_ACTIVE_ELEMENT.put(name, this);
		this.REF_INT_STRING.put(this.id, this.name);
		
		this.defaultValue = new HashMap<Class<? extends IElementalWritable>, Map<Class<? extends Entity>, Float>>();
		
		this.onDamageEffect = new HashMap<PotionEffect, Float>();
		this.onHealEffect = new HashMap<PotionEffect, Float>();
		
		for(Class clazz : Variable.STATS.keySet())
		{
			this.defaultValue.put(clazz, new HashMap<Class<? extends Entity>, Float>());
		}
		
		// boolean configuration (default)
		this.canAppliedFire = canAppliedFire;
	}
	
	public int getId(){return id;}

	public String getName(){return name;}

	public boolean isActive(){return isActive;}
	public boolean isUnActive(){return !isActive();}
	public boolean asFireEffect(){return this.canAppliedFire;}
	public int getFireDuration(){return this.fireDuration;}
	public float getFireProbability(){return this.fireProbability;}

	public void setActive(){this.isActive = true;}
	public void setUnActive(){this.isActive = false;}

	public Set<Entry<PotionEffect, Float>> getDamageEffect()
	{
		return this.onDamageEffect.entrySet();
	}

	public Set<Entry<PotionEffect, Float>> getHealEffect()
	{
		return this.onDamageEffect.entrySet();
	}
	
	/**
	 * 
	 * @param potion <i>PotionEffect</i> Add a specific potion effect that can be applied when damaged
	 * @param percent <i> probability to applied effect (1.0F = 100%, 0.5F = 50%)
	 * @return itself
	 */
	public Element addOnDamageEffect(PotionEffect potion, float percent)
	{
		if(!onDamageEffect.containsKey(potion))
			onDamageEffect.put(potion, percent);
		return this;
	}
	/**
	 * 
	 * @param potion <i>PotionEffect</i> remove the specific effect if exist.
	 * @return itself
	 */
	public Element removeOnDamageEffect(PotionEffect potion)
	{
		if(onDamageEffect.containsKey(potion))
			onDamageEffect.remove(potion);
		return this;
	}
	
	/**
	 * 
	 * @param potion <i>PotionEffect</i> Add a specific potion effect that can be applied when healed
	 * @param percent <i>float</i> probability to applied effect (1.0F = 100%, 0.5F = 50%)
	 * @return itself
	 */
	public Element addOnHealEffect(PotionEffect potion, float percent)
	{
		if(!onHealEffect.containsKey(potion))
			onHealEffect.put(potion, percent);
		return this;
	}
	/**
	 * 
	 * @param potion <i>PotionEffect</i> remove the specific effect if exist.
	 * @return itself
	 */
	public Element removeOnHealEffect(PotionEffect potion)
	{
		if(onHealEffect.containsKey(potion))
			onHealEffect.remove(potion);
		return this;
	}
	
	/**
	 * 
	 * @return <i>boolean</i> return true if their are at least one PotionEffect in list
	 */
	public boolean asDamageEffect()
	{
		return !onDamageEffect.isEmpty();
	}
	
	/**
	 * 
	 * @return <i>boolean</i> return true if their are at least one PotionEffect in list
	 */
	public boolean asHealEffect()
	{
		return !onHealEffect.isEmpty();
	}
	
	/**
	 * 
	 * @param probability <i>float</i> probability to applied fire effect (1.0F = 100%, 0.5F = 50%)
	 * @param duration <i>int</i> burning time in second
	 * @return
	 */
	public Element setFireEffect(float probability, int duration)
	{
		this.canAppliedFire = true;
		this.fireProbability = probability;
		this.fireDuration = duration;
		
		return this;
	}
	
	public Element disabledFireEffect()
	{
		this.canAppliedFire = false;
		return this;
	}
	
	@Override
	public String toString()
	{
		return this.id + ":" + this.name + "(" + this.isActive + ")";
	}
	/**
	 * 
	 * @param stat <i>Class<? extends IElementalWritable></i> MUST BE INITIALIZED IN STATS !!!
	 * @param entity <i>Class<? extends Entity></i> class of entity to initialize
	 * @param value <i>float</i> default value.
	 * @return itself
	 */
	public Element setDefaultValue(Class<? extends IElementalWritable> stat, Class<? extends Entity> entity, float value) 
	{
		defaultValue.get(stat).put(entity, value);
		return this;
	}
	
	/**
	 * 
	 * @param stat <i>Class<? extends IElementalWritable></i> MUST BE INITIALIZED IN STATS !!!
	 * @param entity <i>Class<? extends Entity></i> class of entity to get default value
	 * @param elseFloat <i>Float</i> Value return if not found...
	 * @return <i>Float</i> default value, or 0.0F if absent
	 */
	public float getDefaultValue(Class<? extends IElementalWritable> stat, Class<? extends Entity> entity, float elseFloat)
	{
		if(defaultValue.containsKey(stat))
		{
			Map<Class<? extends Entity>, Float> map = defaultValue.get(stat);
			if(map.containsKey(entity))
			{
				return map.get(entity);
			}
		}
		return elseFloat;
	}
	
	public float getDefaultValue(Class<? extends IElementalWritable> stat, Class<? extends Entity> entity)
	{
		return getDefaultValue(stat, entity, 0.0F);
	}
	

	@Override
	public void fromJsonObject(JsonObject j) 
	{
		this.isActive = j.get("isActive").getAsBoolean();
		this.canAppliedFire = j.get("canAppliedFire").getAsBoolean();	
	}

	@Override
	public JsonObject toJsonObject() 
	{
		JsonObject j = new JsonObject();
		j.addProperty("isActive", this.isActive);
		j.addProperty("canAppliedFire", this.canAppliedFire);
		return j;
	}

	@Override
	public void toByte(ByteBuf buf) 
	{
		buf.writeInt(this.id);
		BufUtils.writeUTF8String(buf, this.name);
		buf.writeBoolean(this.isActive);
		buf.writeBoolean(this.canAppliedFire);
		
		buf.writeInt(getDamageEffect().size());
		for(Entry<PotionEffect, Float> potion : getDamageEffect())
		{
			BufUtils.writeTag(buf, potion.getKey().writeCustomPotionEffectToNBT(new NBTTagCompound()));
			buf.writeFloat(potion.getValue());
		}
		buf.writeInt(getHealEffect().size());
		for(Entry<PotionEffect, Float> potion : getHealEffect())
		{
			BufUtils.writeTag(buf, potion.getKey().writeCustomPotionEffectToNBT(new NBTTagCompound()));
			buf.writeFloat(potion.getValue());
		}
	}

	@Override
	public void fromByte(ByteBuf buf) 
	{
		id = buf.readInt();
		name = BufUtils.readUTF8String(buf);
		isActive = buf.readBoolean();
		canAppliedFire = buf.readBoolean();
		construct(id, name, isActive, canAppliedFire);
		
		int damageSize = buf.readInt();
		for(int i = 0; i < damageSize; i++)
		{
			NBTTagCompound tag = BufUtils.readTag(buf);
			PotionEffect potion = PotionEffect.readCustomPotionEffectFromNBT(tag);
			float probability = buf.readFloat();
			addOnDamageEffect(potion, probability);
		}
		int heal = buf.readInt();
		for(int i = 0; i < heal; i++)
		{
			NBTTagCompound tag = BufUtils.readTag(buf);
			PotionEffect potion = PotionEffect.readCustomPotionEffectFromNBT(tag);
			float probability = buf.readFloat();
			addOnHealEffect(potion, probability);
		}
	}

	@Override
	// never Used here
	public void autoUptdate(Object obj){}

	
	/**
	 * Get element if exist. If not, he will be generated as active
	 * Warning : don't forget to check if element is active before used !
	 * @param name <i>String</i> Name of Element
	 * @return <i>Element</i> asked element
	 */
	public static Element addOrGet(String name)
	{
		String lowerName = name.toLowerCase();
		
		if(LIST_OF_ACTIVE_ELEMENT.containsKey(lowerName))
			return LIST_OF_ACTIVE_ELEMENT.get(lowerName);
		else if(LIST_OF_UNACTIVE_ELEMENT.containsKey(lowerName))
			return LIST_OF_UNACTIVE_ELEMENT.get(lowerName);
		else
			return new Element(lowerName);
	}
	
	/**
	 * return element based on id, else return null;
	 * @param id <i>int</i> id of element
	 * @return <i>Element</i> asked element
	 */
	public static Element findById(int id)
	{
		if(REF_INT_STRING.containsKey(id))
			return addOrGet(REF_INT_STRING.get(id));
		else
			return null;
	}
		
	/**
	 * Remove element if exist. If not, he will be generate as inactive.
	 * @param name <i>String</i> Name of Element
	 * @return <i>Element</i> asked element
	 */
	public static Element remove(String name)
	{
		String lowerName = name.toLowerCase();

		Element value;
		if(LIST_OF_ACTIVE_ELEMENT.containsKey(lowerName))
		{
			value = LIST_OF_ACTIVE_ELEMENT.get(lowerName);
			if(value.id != 0)
			{
				LIST_OF_ACTIVE_ELEMENT.remove(lowerName);
			}
			else
			{
				JLog.info("Can't remove Element whit ID 0 (" + value.name + ")");
			}
		}
		else
		{
			value = new Element(name);
		}
		if(value.id != 0)
		{
			value.setUnActive();
			LIST_OF_UNACTIVE_ELEMENT.put(name, value);		
		}
		else
		{
			JLog.error("You try to create an Element whit ID 0 that is unactive !");
		}
		
		return value;

	}
	
	public static int numberOfElement()
	{
		// we use this for easy way
		return REF_INT_STRING.size();
	}
	
	public static Collection<Element> getAllElement()
	{
		Collection<Element> returnCollection = new ArrayList(LIST_OF_UNACTIVE_ELEMENT.values());
		returnCollection.addAll(getAllActiveElement());
		return returnCollection;
	}
	
	public static Collection<Element> getAllActiveElement()
	{
		return LIST_OF_ACTIVE_ELEMENT.values();
	}

	public static void clear()
	{
		LIST_OF_ACTIVE_ELEMENT.clear();
		LIST_OF_UNACTIVE_ELEMENT.clear();
		REF_INT_STRING.clear();
	}
	

}
