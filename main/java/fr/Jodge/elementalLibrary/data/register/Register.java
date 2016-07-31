package fr.Jodge.elementalLibrary.data.register;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.DamageSource;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.matrix.AttackMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DamageMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DefenceMatrix;
import fr.Jodge.elementalLibrary.function.JLog;

public  class Register extends Variable
{

	/**
	 * Add a new kind of Stat that include interface IElementalWritable
	 * @param c <i>Class<? extends IElementalWritable></i>
	 */
	public static void addNewStats(Class<? extends IElementalWritable> c)
	{
		if(!STATS.containsKey(c))
			STATS.put(c, new HashMap<Class, DataParameter>());
		else
			JLog.alert("Class " + c + " is already references under stats...");
	}
	
	/**
	 * Add new Default Matrix to an item. UnlocalizedName will be used as key.
	 * @param item <i>Item</i> 
	 * @param matrix <i>DamageMatrix</i>
	 */
	public static void addNewWeaponMatrix(Item item, DamageMatrix matrix)
	{
		addNewWeaponMatrix(item.getUnlocalizedName(), matrix);
	}
	
	/**
	 * Add new Default Matrix to a key. If you don't use UnlocalizedName, then key will be never call.
	 * @param name <i>String</i> 
	 * @param matrix <i>DamageMatrix</i>
	 */
	public static void addNewWeaponMatrix(String name, DamageMatrix matrix)
	{
		if(!WEAPONS_DAMAGE_BRUTE.containsKey(name))
			WEAPONS_DAMAGE_BRUTE.put(name, matrix);
		else
			JLog.alert("Item " + name + " is already references. ");
	}
	
	/**
	 * Add new Default Element to a DamageSources. Use DamageType.
	 * @param name <i>String</i> 
	 * @param element <i>Element</i>
	 */
	public static void addNewElementOnDamageSources(DamageSource sources, Element element)
	{
		addNewElementOnDamageSources(sources.getDamageType(), element);
	}
	public static void addNewElementOnDamageSources(String name, Element element)
	{
		DEFAULT_ELEMENT_DAMAGE_SOURCES.put(name, element);
	}
	
	public static void addNewWeaponMatrix(String name, List<Float> rawMatrix)
	{
		
	}
	

	
	
}
