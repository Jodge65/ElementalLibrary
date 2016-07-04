package fr.Jodge.elementalLibrary.common.data;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.EntityDataManager.DataEntry;
import fr.Jodge.elementalLibrary.common.ElementalConstante;
import fr.Jodge.elementalLibrary.common.damage.ElementalMatrix;


public class DataHelper 
{

	/**
	 * 
	 * @param target <i>EntityLivingBase</i> Entity to modify
	 * @param matrix <i>ElementalMatrix</i>Matrix to add. if not exist, then a standard matrix will be use
	 * @param dataType <i>int</i> constant defined in ElementalConstante
	 * @return <i>DataParameter</i> Current Key
	 */
	public static DataParameter addEntityMatrix(EntityLivingBase target, ElementalMatrix matrix, int dataType)
	{
		EntityDataManager targetData = target.getDataManager();
		
		DataParameter key = ElementalConstante.getDataKeyForEntity(target, dataType);
		
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
			targetData.register(key, matrix);
		
		return key;
	}

	
	public static DataParameter addEntityMatrix(EntityLivingBase target, int dataType)
	{
		return addEntityMatrix(target, new ElementalMatrix(), dataType);
	}
	
	/**
	 * 
	 * @param target <i>EntityLivingBase</i> Entity to modify
	 * @param listOfMatrix <i>ElementalMatrix...</i>one or more ElementalMatrix to add.
	 * @return <i>List<DataParameter></i> list of all key in order
	 */
	public static void initEntityMatrix(EntityLivingBase target, ElementalMatrix atkMatrix, ElementalMatrix resistMatrix)
	{		
		addEntityMatrix(target, atkMatrix, ElementalConstante.DATA_ATK);
		addEntityMatrix(target, resistMatrix, ElementalConstante.DATA_RES);
	}
	
	public static List<ElementalMatrix> getElementalMatrix(EntityLivingBase target)
	{
		List<ElementalMatrix> matrix = new ArrayList<ElementalMatrix>();
		
		EntityDataManager targetData = target.getDataManager();
		
		for(DataEntry<?> data : targetData.getAll())
		{
			if(data.getKey().getSerializer() == ElementalDataSerializers.ELEMENTAL_MATRIX)
			{
				matrix.add((ElementalMatrix) data.getValue());
			}
		}
		
		return matrix;
		
	}
	
}
