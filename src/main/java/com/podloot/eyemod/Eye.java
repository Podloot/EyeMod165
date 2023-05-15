package com.podloot.eyemod;

import com.podloot.eyemod.blocks.Charger;
import com.podloot.eyemod.blocks.Router;
import com.podloot.eyemod.blocks.entities.RouterEntity;
import com.podloot.eyemod.config.EyeConfig;
import com.podloot.eyemod.gui.util.messages.EyeMsgHandler;
import com.podloot.eyemod.items.ItemPhone;
import com.podloot.eyemod.network.PacketHandler;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Eye.MODID)
public class Eye
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "eyemod";
	public static final String VERSION = "1";
    // Directly reference a slf4j logger
    //public static final java.util.logging.Logger LOGGER = LogManager.getLogger("eyemod");
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final DeferredRegister<TileEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);
    
     
    public static final RegistryObject<Block> ROUTER = BLOCKS.register("router", () -> new Router(AbstractBlock.Properties.of(Material.STONE)));
    public static final RegistryObject<Item> ROUTER_ITEM = ITEMS.register("router", () -> new BlockItem(ROUTER.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    public static final RegistryObject<Block> CHARGER = BLOCKS.register("charger", () -> new Charger(AbstractBlock.Properties.of(Material.STONE)));
    public static final RegistryObject<Item> CHARGER_ITEM = ITEMS.register("charger", () -> new BlockItem(CHARGER.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    
    public static final RegistryObject<Item> EYEPHONE = ITEMS.register("eyephone", () -> new ItemPhone(new Item.Properties().tab(ItemGroup.TAB_TOOLS).durability(256)));

    public static final RegistryObject<Item> EYECORE = ITEMS.register("eyecore", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> DISPLAY = ITEMS.register("display", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> COVER = ITEMS.register("cover", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> BATTERY = ITEMS.register("battery", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> DRIVE = ITEMS.register("drive", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> WIRE = ITEMS.register("wire", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    
    public static final RegistryObject<TileEntityType<RouterEntity>> ROUTER_ENTITY = BLOCK_ENTITIES.register("router_entity", () -> TileEntityType.Builder.of(RouterEntity::new, ROUTER.get()).build(null));
    
    public Eye()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        
        BLOCK_ENTITIES.register(modEventBus);
        
        ModLoadingContext.get().registerConfig(Type.COMMON, EyeConfig.SPEC, "eyemod-config.toml");

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        event.enqueueWork(PacketHandler::init);
        event.enqueueWork(EyeMsgHandler::init);
       
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event)
    {
    	
        // Do something when the server starts
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
           EyeClient.onInitializeClient();
            
        }
    }
}
