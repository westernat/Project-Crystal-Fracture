package org.hismeo.nuquest;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Mod(NuQuest.MODID)
public class NuQuest {
    public static final String MODID = "nu_quest";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public NuQuest(){
        IEventBus modBusEvent = FMLJavaModLoadingContext.get().getModEventBus();
    }
}
