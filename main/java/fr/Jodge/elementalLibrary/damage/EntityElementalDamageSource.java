package fr.Jodge.elementalLibrary.damage;

import fr.Jodge.elementalLibrary.data.matrix.FinalMatrix;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

public class EntityElementalDamageSource extends EntityDamageSource
{
	protected FinalMatrix damageMatrix;
	
	public EntityElementalDamageSource(String damageTypeIn, Entity damageSourceEntityIn, FinalMatrix damageMatrix) 
	{
		super(damageTypeIn, damageSourceEntityIn);
		this.damageMatrix = damageMatrix;
	}
	
	public EntityElementalDamageSource(EntityDamageSource oldSource, FinalMatrix damageMatrix) 
	{
		this(oldSource.getDamageType(), oldSource.getEntity(), damageMatrix);
	}

	public FinalMatrix getDamageMatrix() 
	{
		return damageMatrix;
	}
	
    @Override
	public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
    {
        ItemStack itemstack = this.damageSourceEntity instanceof EntityLivingBase ? ((EntityLivingBase)this.damageSourceEntity).getHeldItemMainhand() : null;
        String s = "death.attack." + this.damageType + ".item";
        return itemstack != null && itemstack.hasDisplayName() && I18n.canTranslate(s) ? new TextComponentTranslation(s, new Object[] {entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName(), itemstack.getTextComponent()}): new TextComponentTranslation(s, new Object[] {entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName()});
    }

}
