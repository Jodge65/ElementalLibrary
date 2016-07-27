package fr.Jodge.elementalLibrary.data.interfaces;

import fr.Jodge.elementalLibrary.data.matrix.AttackMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DefenceMatrix;

public interface IMonsterMatrix 
{
	/**
	 * Return an object Matrix use for calculation
	 * 
	 * @return ElementalMatrix return the resist matrix of entity.
	 */
	public DefenceMatrix getResistMatrix();
	
	public AttackMatrix getAtkMatrix();
}
