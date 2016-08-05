package fr.Jodge.elementalLibrary.data.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonObject;

import scala.actors.threadpool.Arrays;
import io.netty.buffer.ByteBuf;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import net.minecraft.entity.Entity;

public class FinalMatrix extends ElementalMatrix
{
	protected float currentDamage = 0.0F;
	protected float currentHeal = 0.0F;
	protected boolean asToApplyedDamageEffect = false;
	protected boolean asToApplyedHealEffect = false;
	
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
	/** @return </i>boolean</i> true if need to apply effect. */
	public boolean getNeedToApplyedHealEffect(){return asToApplyedHealEffect;}
	/** @return </i>boolean</i> true if need to apply effect. */
	public boolean getNeedToApplyedDamageEffect(){return asToApplyedDamageEffect;}
	
	public FinalMatrix setNeedToApplyedHealEffect(){return setNeedToApplyedHealEffect(true);}
	public FinalMatrix setNeedToApplyedHealEffect(boolean value){asToApplyedHealEffect = value; return this;}
	
	public FinalMatrix setNeedToApplyedDamageEffect(){return setNeedToApplyedDamageEffect(true);}
	public FinalMatrix setNeedToApplyedDamageEffect(boolean value){asToApplyedDamageEffect = value; return this;}
	
	@Override
	public void fromJsonObject(JsonObject j)
	{
		super.fromJsonObject(j);
		asToApplyedHealEffect = j.getAsJsonObject("asToApplyedHealEffect").getAsBoolean();
		asToApplyedDamageEffect = j.getAsJsonObject("asToApplyedDamageEffect").getAsBoolean();
	}
	
	@Override
	public JsonObject toJsonObject()
	{
		JsonObject j = super.toJsonObject();
		j.addProperty("asToApplyedHealEffect", asToApplyedHealEffect);
		j.addProperty("asToApplyedDamageEffect", asToApplyedDamageEffect);
		return j;
	}
	
	@Override
	public void toByte(ByteBuf buf) 
	{
		super.toByte(buf);
		buf.writeBoolean(asToApplyedHealEffect);
		buf.writeBoolean(asToApplyedDamageEffect);
	}
	

	@Override
	public void fromByte(ByteBuf buf) 
	{
		super.fromByte(buf);
		asToApplyedHealEffect = buf.readBoolean();
		asToApplyedDamageEffect = buf.readBoolean();
	}
	
	@Override
	// never used here
	public void autoUptdate(Object obj){}

}
