package fr.Jodge.elementalLibrary.data.matrix;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.register.Getter;
import fr.Jodge.elementalLibrary.function.JLog;

public class ShieldMatrix extends ElementalMatrix
{
	public ShieldMatrix()
	{
		this(1.0F);
	}
	public ShieldMatrix(float base)
	{
		super(base);
	}	

	public ShieldMatrix(Map<Element, Float> matrix)
	{
		super(matrix);
	}

	@Override
	// TODO finish armor integration, Do shield integration
	public void autoUptdate(Object obj)
	{
		if(obj instanceof ItemStack)
		{
			ItemStack stack = (ItemStack)obj;
			if(stack.getItem() instanceof ItemArmor)
			{
				ItemArmor item = (ItemArmor)stack.getItem();
			    float damage = getDamageReductionByMaterial(item);

			    for(Element element : Element.getAllElement())
			    {
				    // TODO add enchant compatibility
				    matrix.put(element, damage);
			    }
			}
			else if(Main.isBaubleLoaded)
			{
				// TODO add bauble compatibility (if api exist in 1.10)
				/*
				if(stack.getItem() instanceof IBauble)
				{

				}
				*/
			}
			updateItem(stack, this.getClass(), false); // use false to not erase normal resistance
		}
	}
	
	public static float getDamageReductionByMaterial(ItemArmor item)
	{
		ArmorMaterial material = item.getArmorMaterial();
		float armorAmount = material.getDamageReductionAmount(item.armorType);
	    float armorToughness = material.getToughness();
	    return CombatRules.getDamageAfterAbsorb(1.0F, armorAmount, armorToughness);
	}

	
	/**
	 * Create a new ShieldMatrix based on all shieldMatrix given in list.
	 * It's use on damage calculation to have only on final matrix for damage reduction.
	 * 
	 * @param shieldsMatrix <i>List < ShieldMatrix ></i> list of matrix to add
	 * @return <i><ShieldMatrix></i>
	 */
	public static ShieldMatrix calculArmor(List<ShieldMatrix> shieldsMatrix) 
	{
		ShieldMatrix finalMatrix = new ShieldMatrix();
		
		for(Element element : Element.getAllElement())
		{
			float total = 1.0F;
			for(ShieldMatrix matrix : shieldsMatrix)
			{
				total *= matrix.get(element, 1.0F);
			}
			finalMatrix.set(element, total);
		}
		return finalMatrix;
	}
}
