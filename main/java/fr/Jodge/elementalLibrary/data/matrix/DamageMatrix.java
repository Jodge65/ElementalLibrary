package fr.Jodge.elementalLibrary.data.matrix;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.Method;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.google.gson.JsonObject;

import fr.Jodge.elementalLibrary.ElementalConfiguration;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.register.Getter;
import fr.Jodge.elementalLibrary.data.register.Variable;
import fr.Jodge.elementalLibrary.data.stats.ItemStats;
import fr.Jodge.elementalLibrary.log.ElementalCrashReport;
import fr.Jodge.elementalLibrary.log.JLog;


public class DamageMatrix extends ElementalMatrix
{
	protected float totalDamage; // maybe on heal and not damage /!\
	protected boolean isDirty;
	
	public DamageMatrix()
	{
		super(0.0F);
		totalDamage = 0.0F;
		isDirty = true;
	}
	public DamageMatrix(FinalMatrix matrix)
	{
		super(0.0F);
		totalDamage = 0.0F;
		isDirty = true;
		for(Element element : Element.getAllElement())
		{
			set(element, matrix.get(element));
		}
		unDirty();
	}
	
	@Override
	public DamageMatrix clone()
	{
		DamageMatrix newMatrix = new DamageMatrix();
		for(Entry<Element, Float> entry : this.matrix.entrySet())
		{
			newMatrix.set(entry.getKey(), entry.getValue());
		}
		return newMatrix;
	}
	
	@Override
	public void autoUpdate(Object obj)
	{
		if(obj instanceof ItemStack)
		{
			updateItem((ItemStack)obj, this.getClass());
		}
		unDirty();
	}
	
	public DamageMatrix autoUpdateHand(EntityLivingBase entity) 
	{
		ElementalMatrix atkMatrix = (AttackMatrix)entity.getDataManager().get(Getter.getDataKeyForEntity(entity, AttackMatrix.class));

		Element bestBonus = Element.findById(0);

		for(Element element : Element.getAllActiveElement())
		{
			if(atkMatrix.get(bestBonus) < atkMatrix.get(element))
				bestBonus = element;
		}
		matrix.put(bestBonus, 1.0F);

		return this;
	}
	
	public DamageMatrix autoUpdate(Entity entity, float oldValue) 
	{
		if(entity instanceof EntityLivingBase)
		{
			setTotalDamage(getDamageFromEntity((EntityLivingBase)entity, oldValue));
		}
		else if(entity instanceof EntityArrow)
		{
			autoUpdate((EntityArrow) entity, oldValue);
		}
		unDirty();
		return this;
	}
	
	public DamageMatrix autoUpdate(EntityArrow projectile, float oldValue) 
	{
		// base Item Creation
		JLog.info("Try to do a dangerous protected method access.");
		Method m;
		ItemStack stack = null;
		try
		{
			m = projectile.getClass().getDeclaredMethod(ElementalConfiguration.devEnv?"getArrowStack":"func_184550_j"); // getArrowStack
			m.setAccessible(true);
			stack = (ItemStack) m.invoke(projectile);
			
		}
		catch (Throwable throwable)
		{
        	String text = "Something wrong happen while trying to acces protected method getArrowStack (func_184550_j)";
        	ElementalCrashReport.crashReport(throwable, text);
		}

		// Add custom if needed
		NBTTagCompound data = projectile.getEntityData();
		if(data.hasKey(Variable.DEFAULT_MATRIX_KEY))
		{
			String matrixName = data.getString(Variable.DEFAULT_MATRIX_KEY);
			stack.getTagCompound().setString(Variable.DEFAULT_MATRIX_KEY, matrixName);
		}

		ItemStats stats = Getter.getItemStats(stack);
		
		if(stats != null)
		{
			DamageMatrix projectileMatrix = (DamageMatrix) stats.getStat(this.getClass());
			if(projectileMatrix != null)
			{
				for(Element element : Element.getAllElement())
				{
					float newValue = get(element) * projectileMatrix.get(element);
					set(element, newValue);
				}
			}
		}
		setTotalDamage(oldValue);

		unDirty();
		return this;
	}
	
	public DamageMatrix setTotalDamage(float value)
	{
		totalDamage = value;
		return this;
	}
	/*
	public DamageMatrix autoUpdate(IProjectile projectile, float oldValue) 
	{
		if(projectile instanceof Entity)
		{
			Entity entityProjectile = ((Entity)projectile);
			
			// base Item Creation
			ItemStack stack = new ItemStack(new Item().setUnlocalizedName(entityProjectile.getName()));
			
			// Add custom if needed
			NBTTagCompound data = entityProjectile.getEntityData();
			if(data.hasKey(Variable.DEFAULT_MATRIX_KEY))
			{
				String matrixName = data.getString(Variable.DEFAULT_MATRIX_KEY);
				stack.getTagCompound().setString(Variable.DEFAULT_MATRIX_KEY, matrixName);
			}

			ItemStats stats = Getter.getItemStats(stack);
			
			if(stats != null)
			{
				DamageMatrix projectileMatrix = (DamageMatrix) stats.getStat(this.getClass());
				if(projectileMatrix != null)
				{
					for(Element element : Element.getAllElement())
					{
						float newValue = get(element) * projectileMatrix.get(element);
						set(element, newValue);
					}
				}
			}
			totalDamage = oldValue;
		} // end of if IProjectile instance of Entity
		unDirty();
		return this;
	}
	*/
	@Override
	public String toString()
	{
		return super.toString() + " - (" + totalDamage + ")";
	}
		
	@Override
	public JsonObject toJsonObject()
	{
		JsonObject j = super.toJsonObject();
		j.addProperty("totalDamage", totalDamage);
		return j;
	}
	@Override
	public void fromJsonObject(JsonObject j)
	{
		super.fromJsonObject(j);
		setTotalDamage(j.get("totalDamage").getAsFloat());
		unDirty();
	}
	
	@Override
	public void toByte(ByteBuf buf) 
	{
		super.toByte(buf);
		buf.writeFloat(totalDamage);
	}
	
	@Override
	public void fromByte(ByteBuf buf) 
	{
		super.fromByte(buf);
		setTotalDamage(buf.readFloat());
		unDirty();
	}
	
	@Override
	public ElementalMatrix set(int index, float value)
	{
		return set(Element.findById(index), value);
	}
	@Override
	public ElementalMatrix set(Element index, float value)
	{
		super.set(index, value);
		unDirty();
		return this;
	}
	
	/**
	 * adapt matrix to have a total bonus of 1.0F : 
	 * heal can be equal to 0.0F.
	 * If heal is != 0.0F, then damage can be equal to 0.0F.
	 * if nothing is set, then damage = 1.0F.
	 */
	public void unDirty()
	{
		float total = getTotalDamage();
		if(total == 0.0F)
		{
			// if nothing, then damage normal
			matrix.put(Element.findById(0), 1.0F);
		}
		else 
		{
			if(total != 1.0F)
			{
				// here, total damage is over 1.0F
				for(Entry<Element, Float> value : this.matrix.entrySet())
				{
					if(value.getValue() != 0.0F)
					{
						float newValue = (value.getValue() * 1.0F) / total;
						matrix.put(value.getKey(), newValue);
					}
				}
			}
		}

		
		isDirty = false;
	}
	
	/**
	 * Total amount of positive value in matrix
	 * @return <i>float</i>
	 */
	public float getTotalDamage()
	{
		float bonus = 0.0F;
		for(float value : this.matrix.values())
		{
			if(value > 0.0F)
				bonus += value;
			else if(value < 0.0F)
				bonus -= value; // we want a positive total, so each time, we add the negate value that is already negate : - + - = +
		
		}
		return bonus;
	}

	
	@Override
	public float get(int index)
	{
		return super.get(index) * totalDamage;
	}
	@Override
	public float get(Element index)
	{
		return super.get(index) * totalDamage;
	}
	

		
	
}
