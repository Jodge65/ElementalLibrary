package fr.Jodge.elementalLibrary.data.matrix;


import java.util.Map;

import net.minecraft.entity.Entity;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.register.Getter;
import fr.Jodge.elementalLibrary.log.JLog;


public class DefenceMatrix extends ElementalMatrix
{
	public DefenceMatrix()
	{
		this(1.0F);
	}
	public DefenceMatrix(float base)
	{
		super(base);
	}	

	public DefenceMatrix(Map<Element, Float> matrix)
	{
		super(matrix);
	}

	@Override
	public void autoUpdate(Object obj)
	{
		
		if(obj instanceof Entity)
		{
			Entity target = (Entity)obj;
			DefenceMatrix defaultMatrix = Getter.getDefencePercentFromEntity(target);
			if(defaultMatrix != null)
			{
				// if defaultMatrix is not null, then we already have a default value.
				
				if(isCorrectMatrix(defaultMatrix.matrix))
					matrix = defaultMatrix.matrix;
				else
					JLog.error("Their are something wrong whit element use in this matrix...");
			}
			else
			{
				// if defaultMatrix is null, we don't have any default value... So we initialize it.
				updateEntity(target, this.getClass());	
			}// end of else no default value.
		}

	} // end of auto update	

}
