package fr.Jodge.elementalLibrary.data.matrix;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import baubles.api.IBauble;

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
import fr.Jodge.elementalLibrary.log.JLog;

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
	public void autoUpdate(Object obj)
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
			
			/**
			 * IMPORTANT : Normal item have a matrix "clear". Nobody care about this, cause their was never used.
			 * But Bauble Item can be used (implements IBauble). In this case, we have two possibility :
			 * - (rare) item is an instance of ItemArmor. In this case, function are available, so resistance matrix will be create.
			 * - (common) item is instance of anything else. In this 2nd case, we can't create an custom matrix.
			 * 
			 * 1.0F is default value. It's mean damage * 1.0f (no modification). 
			 * If matrix is generate, then value will be add whit other armor, and final armor can be overpowered.
			 * 
			 * To prevent this, if your IBauble item is an instance of ItemArmor, initialize value manually.
			 * You can also customize value manualy if it's a "normal" item.
			 */
			
			updateItem(stack, this.getClass(), false); // use false to not erase normal resistance if item not modify
		}
	}
	
	/**
	 * Return how many damage will be deal by armor. 
	 * It's work as a percent since 0.0F & 1.0F (may be different whit un-vanilla armor). 
	 * value is based on CombatRules.getDamageAfterAbsorb(1.0f, armorAmount, armorToughness);
	 * 
	 * @param item <i>ItemArmor</i> item armor use
	 * @return <i>float</i> multiplier
	 */
	public static float getDamageReductionByMaterial(ItemArmor item)
	{
		ArmorMaterial material = item.getArmorMaterial();
		float armorAmount = material.getDamageReductionAmount(item.armorType);
	    float armorToughness = material.getToughness();
	    return CombatRules.getDamageAfterAbsorb(1.0F, armorAmount, armorToughness);
	}

	
	/**
	 * Create a new ShieldMatrix based on all shieldMatrix given in list.
	 * It's use on damage calculation to have only on final matrix for damage reduction, instead of 4 (8 if Bauble).
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
