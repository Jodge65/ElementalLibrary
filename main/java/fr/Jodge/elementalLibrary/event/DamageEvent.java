package fr.Jodge.elementalLibrary.event;

import java.util.List;

import fr.Jodge.elementalLibrary.damage.DamageHelper;
import fr.Jodge.elementalLibrary.damage.EntityElementalDamageSource;
import fr.Jodge.elementalLibrary.data.DataHelper;
import fr.Jodge.elementalLibrary.data.ElementalDataSerializers;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalDamageSource;
import fr.Jodge.elementalLibrary.data.matrix.FinalMatrix;
import fr.Jodge.elementalLibrary.log.ElementalCrashReport;
import fr.Jodge.elementalLibrary.log.JLog;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.EntityDataManager.DataEntry;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.ReportedException;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DamageEvent 
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onAttack(LivingAttackEvent event)
	{
		// we take all available variable
		DamageSource source = event.getSource();
		
		Entity attacker = null;
		if(source instanceof IElementalDamageSource)
		{
			// if event is an instance of IElementalDamageSource, then we don't need to do something.
			event.setCanceled(false);
			return;
		}
		else
		{
			attacker = source.getEntity();
		}

		EntityLivingBase target = event.getEntityLiving();
		boolean needCanceld = false;
		
		// check of attacker
		if(attacker == null)
		{
			try
			{
				DamageHelper.ElementalizeDamageSource(target, source, event.getAmount());
				needCanceld = true;
			}
            catch (Throwable throwable)
            {
            	String text = "A problem occur when " + target.getName() + " is hit by damage sources " + source.getDamageType() + "\n"
        				+	ElementalCrashReport.getDetails(target)
        				;
            	
            	ElementalCrashReport.crashReport(throwable, text);
            }
		}
		else
		{
			// else we have an entity which deal damage
			if(attacker instanceof EntityLivingBase)
			{		
				try
				{
					// If attacker is an instance of living base, then it's something alive, and source came from EntityDamageSource
					FinalMatrix damageMatrix = null;

					if(source instanceof EntityDamageSourceIndirect)
					{
						Entity projectile = ((EntityDamageSourceIndirect)source).getSourceOfDamage();

						if(projectile instanceof IProjectile)
						{
							damageMatrix = DamageHelper.calculDistanceDamage((IProjectile)projectile, attacker, target, event.getAmount());
						}

					}
					
					if(damageMatrix == null)
					{
						damageMatrix = DamageHelper.calculDamage(attacker, target, event.getAmount());
					}
					
					DamageHelper.dealDamage(attacker, target, (EntityDamageSource)source, damageMatrix);
					needCanceld = true;
				}
	            catch (Throwable throwable)
	            {
	            	String text = "A problem occur when " + attacker.getName() + " try to hit " + target.getName() + "\n"
            				+	ElementalCrashReport.getDetails((EntityLivingBase) attacker)
            				+	ElementalCrashReport.getDetails(target)
            				;
	            	
	            	ElementalCrashReport.crashReport(throwable, text);
	            }
			}
			else
			{
				// else is a damage source whitout entity (dispenser)
				if(source instanceof EntityDamageSourceIndirect)
				{
					try
					{
						DamageHelper.ElementalizeDamageSource(target, source, event.getAmount());
						needCanceld = true;
					}
		            catch (Throwable throwable)
		            {
		            	String text = "A problem occur when " + target.getName() + " is hit by damage indirect sources " + source.getDamageType() + "\n"
		        				+	ElementalCrashReport.getDetails(target)
		        				;
		            	
		            	ElementalCrashReport.crashReport(throwable, text);
		            }
				}
				else
				{
					JLog.alert("### Unknown source " + source + ", entity : " + attacker + ". Minecraft damage calculation will be applyed to prevent bug...");
				}

			}
		}
		
		if(source instanceof EntityDamageSourceIndirect)
		{
			needCanceld = false;
		}
		
		event.setCanceled(needCanceld);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onEntityHurt(LivingHurtEvent event)
	{
		if(!(event.getSource() instanceof IElementalDamageSource))
		{
			event.setCanceled(true);
		}
	}
	
	
}