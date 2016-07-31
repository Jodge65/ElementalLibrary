package fr.Jodge.elementalLibrary.data.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.register.Getter;
import fr.Jodge.elementalLibrary.data.register.Variable;
import fr.Jodge.elementalLibrary.function.JLog;

public class Element
{
	protected static Map<String, Element> LIST_OF_ACTIVE_ELEMENT = new HashMap<String, Element>();
	protected static Map<String, Element> LIST_OF_UNACTIVE_ELEMENT = new HashMap<String, Element>();
	protected static Map<Integer, String> REF_INT_STRING = new HashMap<Integer, String>();
	private static int currentKey = 0;
	
	protected int id;
	protected String name;
	protected boolean isActive;
	protected Map<Class<? extends IElementalWritable>, Map<Class<? extends Entity>, Float>> defaultValue;
	
	protected Element(String name)
	{
		this.id = currentKey;
		this.name = name;
		this.isActive = true;
		currentKey++;
		LIST_OF_ACTIVE_ELEMENT.put(name, this);
		REF_INT_STRING.put(this.id, this.name);
		
		defaultValue = new HashMap<Class<? extends IElementalWritable>, Map<Class<? extends Entity>, Float>>();
		for(Class clazz : Variable.STATS.keySet())
		{
			defaultValue.put(clazz, new HashMap<Class<? extends Entity>, Float>());
		}
	}

	public int getId(){return id;}

	public String getName(){return name;}

	public boolean isActive(){return isActive;}
	public boolean isUnActive(){return !isActive();}

	public void setActive(){this.isActive = true;}
	public void setUnActive(){this.isActive = false;}

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
		if(!lowerName.equalsIgnoreCase("normal"))
		{
			Element value;
			if(LIST_OF_ACTIVE_ELEMENT.containsKey(lowerName))
			{
				value = LIST_OF_ACTIVE_ELEMENT.get(lowerName);
				LIST_OF_ACTIVE_ELEMENT.remove(lowerName);
			}
			else
			{
				value = new Element(name);
			}
			value.setUnActive();
			LIST_OF_UNACTIVE_ELEMENT.put(name, value);
			return value;
		}
		else
		{
			JLog.alert("Can't remove normal element ! It's the main element.");
			return addOrGet("normal");
		}

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


	


}
