package fr.Jodge.elementalLibrary.data.matrix;

import io.netty.buffer.ByteBuf;

import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonObject;

import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.log.JLog;


public class FinalMatrix extends ElementalMatrix
{
	protected float currentDamage = 0.0F;
	protected float currentHeal = 0.0F;
	protected boolean asToApplyedDamageEffect = false;
	protected boolean asToApplyedHealEffect = false;
	
	/** you need to initialize this value if you set asToApplyedDamageEffect or asToApplyedHealEffect to true*/
	public DamageMatrix domMatrix;
	
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

	@Override
	public FinalMatrix clone()
	{
		FinalMatrix newMatrix = new FinalMatrix();
		for(Entry<Element, Float> entry : this.matrix.entrySet())
		{
			newMatrix.set(entry.getKey(), entry.getValue());
		}
		return newMatrix;
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
	
	public FinalMatrix useThisAsDamageMatrix()
	{
		JLog.alert("An empty Damage Matrix is initialize...");
		domMatrix = new DamageMatrix();
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
	
	public FinalMatrix setNeedToApplyedEffect(boolean canApplyEffect) 
	{
		asToApplyedHealEffect = canApplyEffect; 
		asToApplyedDamageEffect = canApplyEffect; 
		if(matrix == null)
		{
			useThisAsDamageMatrix();
		}
		return this;
	}
	public FinalMatrix setNeedToApplyedEffect(boolean canApplyEffect, DamageMatrix matrix)
	{
		domMatrix = matrix;
		return setNeedToApplyedEffect(canApplyEffect);
	}
	
	public FinalMatrix setNeedToApplyedHealEffect(){return setNeedToApplyedHealEffect(true);}
	public FinalMatrix setNeedToApplyedHealEffect(boolean value)
	{
		asToApplyedHealEffect = value; 
		if(matrix == null)
		{
			useThisAsDamageMatrix();
		}
		return this;
	}
	public FinalMatrix setNeedToApplyedHealEffect(boolean value, DamageMatrix matrix)
	{
		domMatrix = matrix;
		return setNeedToApplyedHealEffect(value);
	}
	
	public FinalMatrix setNeedToApplyedDamageEffect(){return setNeedToApplyedDamageEffect(true);}
	public FinalMatrix setNeedToApplyedDamageEffect(boolean value)
	{
		asToApplyedDamageEffect = value;
		if(matrix == null)
		{
			useThisAsDamageMatrix();
		}
		return this;
	}
	public FinalMatrix setNeedToApplyedDamageEffect(boolean value, DamageMatrix matrix)
	{
		domMatrix = matrix;
		return setNeedToApplyedDamageEffect(value);
	}
	
	@Override
	public void fromJsonObject(JsonObject j)
	{
		super.fromJsonObject(j);
		asToApplyedHealEffect = j.getAsJsonObject("asToApplyedHealEffect").getAsBoolean();
		asToApplyedDamageEffect = j.getAsJsonObject("asToApplyedDamageEffect").getAsBoolean();
		if(asToApplyedHealEffect || asToApplyedDamageEffect)
		{
			if(j.getAsJsonObject("DamageMatrixExist").getAsBoolean())
			{
				domMatrix = new DamageMatrix();
				domMatrix.fromJsonObject(j.getAsJsonObject("DamageMatrix"));	
			}
		}
	}
	
	@Override
	public JsonObject toJsonObject()
	{
		JsonObject j = super.toJsonObject();
		j.addProperty("asToApplyedHealEffect", asToApplyedHealEffect);
		j.addProperty("asToApplyedDamageEffect", asToApplyedDamageEffect);
		if(asToApplyedHealEffect || asToApplyedDamageEffect)
		{
			j.addProperty("DamageMatrixExist", domMatrix != null);
			if(domMatrix != null)
			{
				j.add("DamageMatrix", domMatrix.toJsonObject());
			}
		}
		return j;
	}
	
	@Override
	public void toByte(ByteBuf buf) 
	{
		super.toByte(buf);
		buf.writeBoolean(asToApplyedHealEffect);
		buf.writeBoolean(asToApplyedDamageEffect);
		if(asToApplyedHealEffect || asToApplyedDamageEffect)
		{
			buf.writeBoolean(domMatrix != null);
			if(domMatrix != null)
			{
				domMatrix.toByte(buf);
			}
		}
	}
	

	@Override
	public void fromByte(ByteBuf buf) 
	{
		super.fromByte(buf);
		asToApplyedHealEffect = buf.readBoolean();
		asToApplyedDamageEffect = buf.readBoolean();
		if(asToApplyedHealEffect || asToApplyedDamageEffect)
		{
			if(buf.readBoolean())
			{
				domMatrix = new DamageMatrix();
				domMatrix.fromByte(buf);	
			}
		}
	}
	
	@Override
	// never used here
	public void autoUpdate(Object obj){}

}
