package fr.Jodge.elementalLibrary.data;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.EntityDataManager.DataEntry;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.matrix.ElementalMatrix;
import fr.Jodge.elementalLibrary.data.register.Getter;
import fr.Jodge.elementalLibrary.data.register.Register;
import fr.Jodge.elementalLibrary.data.stats.AbstractStats;
import fr.Jodge.elementalLibrary.data.stats.MonsterStats;
import fr.Jodge.elementalLibrary.log.JLog;

public class DataHelper 
{

	/**
	 * 
	 * @param target <i>EntityLivingBase</i> Entity to modify
	 * @param element <i>ElementalMatrix</i>Matrix to add. if not exist, then a standard matrix will be use
	 * @param dataType <i>int</i> constant defined in ElementalConstante
	 * @return <i>DataParameter</i> Current Key
	 */
	public static DataParameter addEntityElement(Entity target, IElementalWritable element)
	{
		EntityDataManager targetData = target.getDataManager();
		DataParameter key = Getter.getDataKeyForEntity(target, element.getClass());
		
		boolean isNotExisting = true;
		for(DataEntry entry : targetData.getAll())
		{
			if(entry.getKey().getId() == key.getId())
			{
				isNotExisting = false;
				break;
			}
		}
		if(isNotExisting)
		{
			targetData.register(key, element);
		}
		else
		{
			targetData.set(key, element);
			JLog.alert("Entity " + target.getName() + " already have a class " + element.getClass() + " saved. Only one instance can be put in the same time, so it was overwriten.");;
		}
		return key;
	}
	
	/**
	 * 
	 * @param target <i>Entity</i> Entity to modify
	 * @param listOfMatrix <i>IElementalWritable...</i>one or more IElementalWritable to add.
	 */
	public static void initEntityMatrix(Entity target, IElementalWritable... elements)
	{
		for(IElementalWritable element : elements)
		{
			addEntityElement(target, element);
		}
	}
	
	/**
	 * 
	 * @param target <i>Entity</i> Entity to modify
	 * @param stats <i>AbstractStats</i> Instance of AbstractStats (use to send/receive value to/from server)
	 */
	public static void initEntityMatrix(Entity target, AbstractStats stats)
	{	
		if(stats instanceof MonsterStats)
		{
			if(((MonsterStats)stats).isDefaultStats)
			{
				if(!Main.constante.DEFAULT_STATS.containsKey(target.getClass()))
				{
					Register.addNewDefaultEntity(target, stats);
				}
			}
		}
		
		for(Class<? extends IElementalWritable> clazz : stats.listOfAvailableStats)
		{
			addEntityElement(target, stats.getStat(clazz));
		}
	}
	
	public static List<ElementalMatrix> getElementalMatrix(Entity target)
	{
		List<ElementalMatrix> matrix = new ArrayList<ElementalMatrix>();
		
		EntityDataManager targetData = target.getDataManager();
		
		for(DataEntry<?> data : targetData.getAll())
		{
			if(data.getKey().getSerializer() == ElementalDataSerializers.ELEMENTAL_SERIALIZER)
			{
				matrix.add((ElementalMatrix) data.getValue());
			}
		}
		
		return matrix;
		
	}
}
