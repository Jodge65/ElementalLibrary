package fr.Jodge.elementalLibrary.data.register;

import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import fr.Jodge.elementalLibrary.data.ElementalDataSerializers;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.matrix.AttackMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DamageMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DefenceMatrix;
import fr.Jodge.elementalLibrary.data.matrix.FinalMatrix;
import fr.Jodge.elementalLibrary.function.JLog;

public class Getter extends Register
{
	/**
	 * return current DamageMatrix
	 * @param item <i>Item</i> Current Item
	 * @return <i>DamageMatrix</i> Default DamageMatrix for current item. May return null if not references
	 */
	public static DamageMatrix getDamageBruteFromItem(Item item)
	{
		return getDamageBruteFromItem(item.getUnlocalizedName());
	}
	
	public static DamageMatrix getDamageBruteFromItem(String name)
	{
		return WEAPONS_DAMAGE_BRUTE.getOrDefault(name, null);
	}
	
	
	public static AttackMatrix getDamagePercentFromEntity(Entity entity)
	{
		return getDamagePercentFromEntity(entity.getClass());
	}
	
	public static AttackMatrix getDamagePercentFromEntity(Class entityClass)
	{
		return ENTITY_DAMAGE_PERCENT.getOrDefault(entityClass, null);
	}
	
	
	public static DefenceMatrix getDefencePercentFromEntity(Entity entity)
	{
		return getDefencePercentFromEntity(entity.getClass());
	}
	
	public static DefenceMatrix getDefencePercentFromEntity(Class entityClass)
	{
		return ENTITY_RESISTANCE_PERCENT.getOrDefault(entityClass, null);
	}
	
	public static DataParameter getDataKeyForEntity(Entity target, Class dataType)
	{
		Class key = target.getClass();
		Map<Class, DataParameter> currentMap;
		
		if(STATS.containsKey(dataType))
		{
			currentMap = STATS.get(dataType);
		}
		else
		{
			JLog.warning("The data type " + dataType + " is undefined...");
			return null;
		}
		
		if(currentMap.containsKey(key))
		{
			return currentMap.get(key);
		}
		else
		{
			DataParameter value = EntityDataManager.createKey(target.getClass(), ElementalDataSerializers.ELEMENTAL_SERIALIZER);
			currentMap.put(key, value);
			
			JLog.info("New key have ben create for entity " + target.getClass() + ". Id use is : " + value.getId() + ".");
			
			return value;
		}	
	}

	/**
	 * Return à FinalMatrix based on DamageSources. 
	 * For example : source onFire is reference as a Fire source of damage. Function will return a matrix whit only fire damage.
	 * @param source <i>DamageSource</i> DamageSources
	 * @param amount <i>float</i> amount of damage
	 * @return <i>FinalMatrix</i>
	 */
	public static FinalMatrix getElementalizeDamageSource(DamageSource source, float amount) 
	{
		FinalMatrix returnValue = new FinalMatrix(0.0F);
		if(DEFAULT_ELEMENT_DAMAGE_SOURCES.containsKey(source.getDamageType()))
		{
			returnValue.set(DEFAULT_ELEMENT_DAMAGE_SOURCES.get(source.getDamageType()), amount);
		}
		else
		{
			JLog.alert("DamageSources " + source.getDamageType() + " is not references... Normal is used...");
			returnValue.set(Element.addOrGet("normal"), amount);
		}
		return returnValue;
	}
}
