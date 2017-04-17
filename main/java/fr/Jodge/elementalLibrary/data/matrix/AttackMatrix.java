package fr.Jodge.elementalLibrary.data.matrix;

import java.util.Map;

import net.minecraft.entity.Entity;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.register.Getter;
import fr.Jodge.elementalLibrary.log.JLog;

public class AttackMatrix extends ElementalMatrix
{

	public AttackMatrix()
	{
		this(0.75F);
	}
	public AttackMatrix(float base)
	{
		super(base);
	}	

	public AttackMatrix(Map<Element, Float> matrix)
	{
		super(matrix);
	}

	@Override
	public void autoUpdate(Object obj)
	{
		if(obj instanceof Entity)
		{
			Entity target = (Entity)obj;
			AttackMatrix defaultMatrix = Getter.getDamagePercentFromEntity(target);
			
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
			} // end of else no default matrix
		}
	} // end of auto update

}
