package fr.Jodge.elementalLibrary.common.damage;

import java.util.ArrayList;
import java.util.List;

import fr.Jodge.elementalLibrary.common.Element;
import fr.Jodge.elementalLibrary.common.function.JLog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;

public class EnvironmentalHelper 
{

	
	/**
	 * this function will add or subtract to the 1.0F matrix. If entity implement HaveOwnResistMatrix then he will return hit.
	 * 
	 * @param target <i>Entity</i> entity who want is matrix
	 * @return List<Float> return list of damage taken based on entity type. 1.0f = 100% damage taken, 0.5F = 50% -0.5, = 50% heal
	 */
	public static ElementalMatrix getEnvironmentalMatrix(EntityLivingBase target)
	{
		ElementalMatrix matrix;
		
		matrix = new ElementalMatrix(1.0F);
		BlockPos currentPos = new BlockPos(target);
		BiomeGenBase biome = target.worldObj.getBiomeGenForCoordsBody(currentPos);
		
		matrix = adapteMatrixByTemperature(matrix, biome.getFloatTemperature(currentPos));
		matrix = adapteMatrixByRainfall(matrix, biome.getRainfall());
		
		if(target.isBurning())
			matrix = addOnFireMatrix(matrix);
		if(target.isImmuneToFire())
			matrix = addIsImmuneFireMatrix(matrix);
		if(target.isInWater())
			matrix = addIsOnWaterMatrix(matrix);
		if(target.isWet())
			matrix = addIsWetMatrix(matrix);
		if(target.onGround)
			matrix = addOnGroundMatrix(matrix);
		else
			matrix = addIsFlyingMatrix(matrix);

		//if(target.worldObj.)

		
		return matrix;
	}

	
	private static ElementalMatrix adapteMatrixByRainfall(ElementalMatrix matrix, float rainfall) 
	{
		if(rainfall > 0.85F)
		{
			matrix.set(Element.WATER, matrix.get(Element.WATER) * (0.15F + rainfall));
			matrix.set(Element.FIRE, matrix.get(Element.FIRE) * (float)(1.75F - rainfall));
		}
		return matrix;
	}


	private static ElementalMatrix adapteMatrixByTemperature(ElementalMatrix matrix, double temperature) 
	{
		if(temperature < 0.2D)
		{
			matrix.set(Element.WATER, matrix.get(Element.WATER) * (float)(1.0F + (0.20F - temperature)));
			matrix.set(Element.FIRE, matrix.get(Element.FIRE) * (float)(1.0F - (0.20F - temperature)));

		}
		else if(temperature > 1.0D)
		{
			matrix.set(Element.WATER, matrix.get(Element.WATER) * (float)(temperature - 0.20F));
			matrix.set(Element.FIRE, matrix.get(Element.FIRE) * (float)(temperature - 0.5F));
		}
		else // 0.2 < T° < 1.0 
		{
			
		}
			
			
		return matrix;
	}

	public static ElementalMatrix addUndeadMatrix(ElementalMatrix matrix)
	{
		matrix.set(Element.DARK, matrix.get(Element.DARK) * -0.5F);
		matrix.set(Element.HOLY, matrix.get(Element.HOLY) * 2.0F);
		return matrix;
	}
	
	public static ElementalMatrix addIsFlyingMatrix(ElementalMatrix matrix)
	{
		matrix.set(Element.DIRT, matrix.get(Element.WIND)*0.9F);
		matrix.set(Element.WIND, matrix.get(Element.WIND)*1.5F);
		matrix.set(Element.THUNDER, matrix.get(Element.THUNDER)*1.5F);
		return matrix;
	}
	
	public static ElementalMatrix addOnUnderGroundMatrix(ElementalMatrix matrix)
	{
		matrix.set(Element.DIRT, matrix.get(Element.DIRT)*2.0F);
		matrix.set(Element.THUNDER, matrix.get(Element.WIND)*0.7F);
		return matrix;
	}
	
	public static ElementalMatrix addOnGroundMatrix(ElementalMatrix matrix)
	{
		matrix.set(Element.DIRT, matrix.get(Element.DIRT)*1.1F);
		matrix.set(Element.THUNDER, matrix.get(Element.WIND)*0.9F);
		return matrix;
	}
	
	public static ElementalMatrix addOnFireMatrix(ElementalMatrix matrix)
	{
		matrix.set(Element.FIRE, matrix.get(Element.FIRE)*0.8F);
		matrix.set(Element.WATER, matrix.get(Element.WATER)*1.2F);
		matrix.set(Element.WIND, matrix.get(Element.WIND)*1.2F);
		return matrix;
	}
	
	public static ElementalMatrix addIsOnWaterMatrix(ElementalMatrix matrix)
	{
		matrix.set(Element.FIRE, 0.0F);
		matrix.set(Element.THUNDER, matrix.get(Element.THUNDER)*2.5F);
		return matrix;
	}
	
	public static ElementalMatrix addIsWetMatrix(ElementalMatrix matrix)
	{
		matrix.set(Element.FIRE, matrix.get(Element.FIRE)*0.8F);
		matrix.set(Element.THUNDER, matrix.get(Element.THUNDER)*1.5F);
		return matrix;
	}
	
	public static ElementalMatrix addIsImmuneFireMatrix(ElementalMatrix matrix)
	{
		matrix.set(Element.FIRE, 0.0F);
		return matrix;
	}
}
