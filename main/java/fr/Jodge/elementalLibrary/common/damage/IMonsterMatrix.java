package fr.Jodge.elementalLibrary.common.damage;

public interface IMonsterMatrix 
{
	/**
	 * Return an object Matrix use for calculation
	 * 
	 * @return ElementalMatrix return the resist matrix of entity.
	 */
	public ElementalMatrix getResistMatrix();
	
	public ElementalMatrix getAtkMatrix();
}
