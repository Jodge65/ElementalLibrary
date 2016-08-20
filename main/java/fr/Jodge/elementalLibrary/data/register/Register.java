package fr.Jodge.elementalLibrary.data.register;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.DamageSource;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.DataHelper;
import fr.Jodge.elementalLibrary.data.ItemHelper;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.matrix.AttackMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DamageMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DefenceMatrix;
import fr.Jodge.elementalLibrary.data.stats.AbstractStats;
import fr.Jodge.elementalLibrary.data.stats.ItemStats;
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
	public static void addNewWeaponMatrix(ItemStack stack, ItemStats stats)
	{
		addNewWeaponMatrix(ItemHelper.getUnlocalizedName(stack), stats);
	}
	
	/**
	 * Add new Default Matrix to a key. If you don't use UnlocalizedName, then key will be never call.
	 * @param name <i>String</i> 
	 * @param matrix <i>DamageMatrix</i>
	 */
	public static void addNewWeaponMatrix(String name, ItemStats stats)
	{
		if(!DEFAULT_ITEM_STATS.containsKey(name))
			DEFAULT_ITEM_STATS.put(name, stats);
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
	
	/**
	 * 
	 * @param clazz <i>Class</i> supposed to be entity.getClass()
	 * @param stats <i>AbstractStats</i> default stats.
	 */
	public static void addNewDefaultEntity(Entity entity, AbstractStats stats)
	{
		addNewDefaultEntity(entity.getClass(), stats);
	}
	public static void addNewDefaultEntity(Class clazz, AbstractStats stats)
	{
		DEFAULT_STATS.put(clazz, stats);
	}

	
}
