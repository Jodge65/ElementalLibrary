package fr.Jodge.elementalLibrary.data.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import scala.actors.threadpool.Arrays;
import io.netty.buffer.ByteBuf;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.register.ElementalConstante;
import net.minecraft.entity.Entity;

public class FinalMatrix extends ElementalMatrix
{
	protected float currentDamage = 0.0F;
	protected float currentHeal = 0.0F;
	
	public FinalMatrix()
	{
		this(0.0F);
	}
	public FinalMatrix(float base)
	{
		super(base);
		doCalculation();
	}	

	public FinalMatrix(Map<Element, Float> matrix)
	{
		super(matrix);
		doCalculation();
	}

	public FinalMatrix updateCalculation()
	{
		currentDamage = 0;
		currentHeal = 0;
		return doCalculation();
	}
	
	protected FinalMatrix doCalculation()
	{
		for(float value : matrix.values())
		{
			if(value > 0.0F)
				currentDamage += value;
			else
				currentHeal += value;
		}
		
		return this;
	}
	
	/** @return </i>float</i> total damage */
	public float getTotalDamage(){return currentDamage;}
	/** @return </i>float</i> total heal */
	public float getTotalHeal(){return currentHeal;}
	
	@Override
	public void autoUptdate(Entity target)
	{
		
	}

}
