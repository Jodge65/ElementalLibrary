package fr.Jodge.elementalLibrary.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalDamageSource;
import fr.Jodge.elementalLibrary.data.matrix.FinalMatrix;

public class ElementalDamageSource  extends DamageSource implements IElementalDamageSource
{
	protected FinalMatrix damageMatrix;
	
	/**
	 * 
	 * @param damageTypeIn <i>String</i> Type of damage
	 * @param damageMatrix <i>FinalMatrix</i> Final Matrix
	 */
	public ElementalDamageSource(String damageTypeIn, FinalMatrix damageMatrix) 
	{
		super(damageTypeIn);
		this.damageMatrix = damageMatrix;
	}
	
	public ElementalDamageSource(DamageSource oldSource, FinalMatrix damageMatrix) 
	{
		this(oldSource.getDamageType(), damageMatrix);
	}
	

	@Override
	public FinalMatrix getDamageMatrix() 
	{
		return damageMatrix;
	}

	// TODO change death message
    @Override
	public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
    {
       return super.getDeathMessage(entityLivingBaseIn);
    }
}
