package fr.Jodge.elementalLibrary.data.register;

import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.DataHelper;
import fr.Jodge.elementalLibrary.data.ElementalDataSerializers;
import fr.Jodge.elementalLibrary.data.ItemHelper;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.matrix.AttackMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DamageMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DefenceMatrix;
import fr.Jodge.elementalLibrary.data.matrix.FinalMatrix;
import fr.Jodge.elementalLibrary.data.stats.ItemStats;
import fr.Jodge.elementalLibrary.log.JLog;

public class Getter extends Register
{
	@Nullable
	public static ItemStats getItemStatsIfExist(ItemStack stack)
	{
		String itemName = ItemHelper.getMatrixName(stack);
		return DEFAULT_ITEM_STATS.getOrDefault(itemName, null);
	}

	@Nullable
	public static ItemStats getItemStats(ItemStack stack)
	{
		if(stack != null)
		{
			String itemName = ItemHelper.getMatrixName(stack);
			
			if(!DEFAULT_ITEM_STATS.containsKey(itemName))
			{
				ItemHelper.initItem(stack);
				JLog.warning("Value not exist. Waiting for Server to send value...");
			}
			
			int waitingTime = 0;
			while(!DEFAULT_ITEM_STATS.containsKey(itemName))
			{
				waitingTime++;
				if(waitingTime % 1000 == 0)
				{
					JLog.info("Waiting... " + waitingTime / 1000 + "/10");
				}
				if(waitingTime >= 10000)
				{
					JLog.error("Value not receive for ItemName : " + itemName);
					return null;
				}
			}

			return DEFAULT_ITEM_STATS.get(itemName);
		}
		else
		{
			JLog.error("You ask stats for null ItemStack... Are you crazy ?");
			return null;
		}
		
	}

	@Nullable
	public static IElementalWritable get(Class<? extends IElementalWritable> clazz, Class value)
	{
		if(VALUE_REGISTER.containsKey(clazz))
		{
			return VALUE_REGISTER.get(AttackMatrix.class).getOrDefault(value, null);
		}
		return null;
	}
	
	public static AttackMatrix getDamagePercentFromEntity(Entity entity)
	{
		return getDamagePercentFromEntity(entity.getClass());
	}
	
	public static AttackMatrix getDamagePercentFromEntity(Class entityClass)
	{
		return (AttackMatrix) get(AttackMatrix.class, entityClass);
	}
	
	
	public static DefenceMatrix getDefencePercentFromEntity(Entity entity)
	{
		return getDefencePercentFromEntity(entity.getClass());
	}
	
	public static DefenceMatrix getDefencePercentFromEntity(Class entityClass)
	{
		return (DefenceMatrix) get(DefenceMatrix.class, entityClass);
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
			
			JLog.info("New key have ben create for entity " + target.getClass() + ", and for stat " + dataType + ". Id use is : " + value.getId() + ".");
			
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
			JLog.alert("DamageSources " + source.getDamageType() + " is not references... Element whit ID 0 is used...");
			returnValue.set(Element.findById(0), amount);
		}
		return returnValue;
	}

	public static boolean canApplyEffect(DamageSource source) 
	{
		if(DEFAULT_EFFECT_DAMAGE_SOURCES.containsKey(source.getDamageType()))
			return DEFAULT_EFFECT_DAMAGE_SOURCES.get(source.getDamageType());
		else
		{
			JLog.alert("DamageSources " + source.getDamageType() + " is not references... Additional Effect set to true.");
			return true;
		}
	}
}
