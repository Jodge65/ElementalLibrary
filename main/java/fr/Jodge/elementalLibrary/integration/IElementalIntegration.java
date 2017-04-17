package fr.Jodge.elementalLibrary.integration;

import fr.Jodge.elementalLibrary.data.matrix.AttackMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DamageMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DefenceMatrix;
import fr.Jodge.elementalLibrary.data.matrix.ShieldMatrix;

public interface IElementalIntegration
{
	/** Attack Matrix */
	Class atk = AttackMatrix.class;
	/** Defense Matrix */
	Class def = DefenceMatrix.class;
	/** Damage Matrix */
	Class dam = DamageMatrix.class;
	/** Shield Matrix */
	Class shi = ShieldMatrix.class;
	
	
	/**
	 * Initialize all ElementalLibrary default element
	 * and default resistance
	 */
	public void initElement();
}
