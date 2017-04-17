package fr.Jodge.elementalLibrary.data.matrix;

import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import fr.Jodge.elementalLibrary.data.element.Element;

public class EnvironmentalMatrix extends ElementalMatrix
{

	public EnvironmentalMatrix()
	{
		this(1.0F);
	}
	
	public EnvironmentalMatrix(float base)
	{
		super(base);
	}	

	public EnvironmentalMatrix(Map<Element, Float> matrix)
	{
		super(matrix);
	}
	
	@Override
	public void autoUpdate(Object obj)
	{
		if(obj instanceof Entity)
		{
			Entity target = (Entity)obj;
		
			BlockPos currentPos = new BlockPos(target);
			Biome biome = target.worldObj.getBiomeForCoordsBody(currentPos);
			
			adapteMatrixByTemperature(biome.getFloatTemperature(currentPos));
			adapteMatrixByRainfall(biome.getRainfall());
			
			if(target.isBurning())
				addOnFireMatrix();
			if(target.isImmuneToFire())
				addIsImmuneFireMatrix();
			if(target.isInWater())
				addIsOnWaterMatrix();
			if(target.isWet())
				addIsWetMatrix();
			if(target.onGround)
				addOnGroundMatrix();
			else
				addIsFlyingMatrix();
		}
	}
	
	protected void adapteMatrixByRainfall(float rainfall) 
	{
		if(rainfall > 0.85F)
		{
			set(Element.addOrGet("water"), get(Element.addOrGet("water")) * (0.15F + rainfall));
			set(Element.addOrGet("fire"), get(Element.addOrGet("fire")) * (1.75F - rainfall));
		}
	}


	protected void adapteMatrixByTemperature(double temperature) 
	{
		if(temperature < 0.2D)
		{
			set(Element.addOrGet("water"), get(Element.addOrGet("water")) * (float)(1.0F + (0.20F - temperature)));
			set(Element.addOrGet("fire"), get(Element.addOrGet("fire")) * (float)(1.0F - (0.20F - temperature)));

		}
		else if(temperature > 1.0D)
		{
			set(Element.addOrGet("water"), get(Element.addOrGet("water")) * (float)(temperature - 0.20F));
			set(Element.addOrGet("fire"), get(Element.addOrGet("fire")) * (float)(temperature - 0.5F));
		}
		else // 0.2 < T° < 1.0 
		{
			// TODO to change or not to change ? That is the question...
		}
			
	}

	public void addUndeadMatrix()
	{
		set(Element.addOrGet("dark"), get(Element.addOrGet("dark")) * -0.5F);
		set(Element.addOrGet("holy"), get(Element.addOrGet("holy")) * 2.0F);
	}
	
	public void addIsFlyingMatrix()
	{
		set(Element.addOrGet("dirt"), get(Element.addOrGet("wind"))*0.9F);
		set(Element.addOrGet("wind"), get(Element.addOrGet("wind"))*1.5F);
		set(Element.addOrGet("thunder"), get(Element.addOrGet("thunder"))*1.5F);
	}
	
	public void addOnUnderGroundMatrix()
	{
		set(Element.addOrGet("dirt"), get(Element.addOrGet("dirt"))*2.0F);
		set(Element.addOrGet("thunder"), get(Element.addOrGet("wind"))*0.7F);
	}
	
	public void addOnGroundMatrix()
	{
		set(Element.addOrGet("dirt"), get(Element.addOrGet("dirt"))*1.1F);
		set(Element.addOrGet("thunder"), get(Element.addOrGet("wind"))*0.9F);
	}
	
	public void addOnFireMatrix()
	{
		set(Element.addOrGet("fire"), get(Element.addOrGet("fire"))*0.8F);
		set(Element.addOrGet("water"), get(Element.addOrGet("water"))*1.2F);
		set(Element.addOrGet("wind"), get(Element.addOrGet("wind"))*1.2F);
	}
	
	public void addIsOnWaterMatrix()
	{
		set(Element.addOrGet("fire"), 0.0F);
		set(Element.addOrGet("thunder"), get(Element.addOrGet("thunder"))*2.5F);
	}
	
	public void addIsWetMatrix()
	{
		set(Element.addOrGet("fire"), get(Element.addOrGet("fire"))*0.8F);
		set(Element.addOrGet("thunder"), get(Element.addOrGet("thunder"))*1.5F);
	}
	
	public void addIsImmuneFireMatrix()
	{
		set(Element.addOrGet("fire"), 0.0F);
	}
}
