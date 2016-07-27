package fr.Jodge.elementalLibrary.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.EntityDataManager.DataEntry;
import fr.Jodge.elementalLibrary.ElementalConstante;
import fr.Jodge.elementalLibrary.data.entity.AbstractStats;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.matrix.ElementalMatrix;
import fr.Jodge.elementalLibrary.function.JLog;


public class DataHelper 
{

	/**
	 * 
	 * @param target <i>EntityLivingBase</i> Entity to modify
	 * @param element <i>ElementalMatrix</i>Matrix to add. if not exist, then a standard matrix will be use
	 * @param dataType <i>int</i> constant defined in ElementalConstante
	 * @return <i>DataParameter</i> Current Key
	 */
	public static DataParameter addEntityElement(EntityLivingBase target, IElementalWritable element)
	{
		EntityDataManager targetData = target.getDataManager();
		
		DataParameter key = ElementalConstante.getDataKeyForEntity(target, element.getClass());
		
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
			targetData.register(key, element);
		else
			JLog.alert("Entity " + target.getName() + " already have a class " + element.getClass() + " saved. Only one instance can be put in the same time.");;
		
		return key;
	}
	
	/**
	 * 
	 * @param target <i>EntityLivingBase</i> Entity to modify
	 * @param listOfMatrix <i>IElementalWritable...</i>one or more IElementalWritable to add.
	 */
	public static void initEntityMatrix(EntityLivingBase target, IElementalWritable... elements)
	{
		for(IElementalWritable element : elements)
		{
			addEntityElement(target, element);
		}
	}
	
	/**
	 * 
	 * @param target <i>EntityLivingBase</i> Entity to modify
	 * @param stats <i>AbstractStats</i> Instance of AbstractStats (use to send/receive value to/from server)
	 */
	public static void initEntityMatrix(EntityLivingBase target, AbstractStats stats)
	{
		for(Pair<Class, IElementalWritable> coupleOfValue : stats.getListOfAvailableStats())
		{
			addEntityElement(target, coupleOfValue.getValue());
		}
	}
	
	public static List<ElementalMatrix> getElementalMatrix(EntityLivingBase target)
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
