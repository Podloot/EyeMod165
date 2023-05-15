package com.podloot.eyemod.network;

import com.podloot.eyemod.Eye;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

	public PacketHandler() {

	}

	public static final String PROT_VERSION = "1";

	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(Eye.MODID, "main"), () -> PROT_VERSION, PROT_VERSION::equals, PROT_VERSION::equals);

	public static void init() {
		int index = 0;
		INSTANCE.messageBuilder(ClientGuiOpen.class, index++, NetworkDirection.PLAY_TO_CLIENT)
		.encoder(ClientGuiOpen::encode).decoder(ClientGuiOpen::new).consumer(ClientGuiOpen::handle).add();
		
		INSTANCE.messageBuilder(ServerNbtReplace.class, index++, NetworkDirection.PLAY_TO_SERVER)
				.encoder(ServerNbtReplace::encode).decoder(ServerNbtReplace::new).consumer(ServerNbtReplace::handle)
				.add();

		INSTANCE.messageBuilder(ServerNbtSet.class, index++, NetworkDirection.PLAY_TO_SERVER)
				.encoder(ServerNbtSet::encode).decoder(ServerNbtSet::new).consumer(ServerNbtSet::handle).add();

		INSTANCE.messageBuilder(ServerSendChat.class, index++, NetworkDirection.PLAY_TO_SERVER)
				.encoder(ServerSendChat::encode).decoder(ServerSendChat::new).consumer(ServerSendChat::handle).add();

		INSTANCE.messageBuilder(ServerSendMessage.class, index++, NetworkDirection.PLAY_TO_SERVER)
				.encoder(ServerSendMessage::encode).decoder(ServerSendMessage::new).consumer(ServerSendMessage::handle)
				.add();

		INSTANCE.messageBuilder(ServerItemRemove.class, index++, NetworkDirection.PLAY_TO_SERVER)
				.encoder(ServerItemRemove::encode).decoder(ServerItemRemove::new).consumer(ServerItemRemove::handle)
				.add();

		INSTANCE.messageBuilder(ServerExplode.class, index++, NetworkDirection.PLAY_TO_SERVER)
				.encoder(ServerExplode::encode).decoder(ServerExplode::new).consumer(ServerExplode::handle).add();

		INSTANCE.messageBuilder(ServerTeleport.class, index++, NetworkDirection.PLAY_TO_SERVER)
				.encoder(ServerTeleport::encode).decoder(ServerTeleport::new).consumer(ServerTeleport::handle).add();

		INSTANCE.messageBuilder(ServerRouterData.class, index++, NetworkDirection.PLAY_TO_SERVER)
				.encoder(ServerRouterData::encode).decoder(ServerRouterData::new).consumer(ServerRouterData::handle)
				.add();

		INSTANCE.messageBuilder(ServerKick.class, index++, NetworkDirection.PLAY_TO_SERVER).encoder(ServerKick::encode)
				.decoder(ServerKick::new).consumer(ServerKick::handle).add();

		INSTANCE.messageBuilder(ServerEffect.class, index++, NetworkDirection.PLAY_TO_SERVER)
				.encoder(ServerEffect::encode).decoder(ServerEffect::new).consumer(ServerEffect::handle).add();

		INSTANCE.messageBuilder(ServerEntity.class, index++, NetworkDirection.PLAY_TO_SERVER)
				.encoder(ServerEntity::encode).decoder(ServerEntity::new).consumer(ServerEntity::handle).add();

		INSTANCE.messageBuilder(ServerWeather.class, index++, NetworkDirection.PLAY_TO_SERVER)
				.encoder(ServerWeather::encode).decoder(ServerWeather::new).consumer(ServerWeather::handle).add();

		INSTANCE.messageBuilder(ServerRouter.class, index++, NetworkDirection.PLAY_TO_SERVER)
				.encoder(ServerRouter::encode).decoder(ServerRouter::new).consumer(ServerRouter::handle).add();

	}

}
