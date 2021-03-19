package io.github.lucaargolo.terrarianslimes

import io.github.lucaargolo.terrarianslimes.common.entity.EntityCompendium
import io.github.lucaargolo.terrarianslimes.common.item.ItemCompendium
import io.github.lucaargolo.terrarianslimes.network.PacketCompendium
import io.github.lucaargolo.terrarianslimes.utils.ModIdentifier
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.block.Blocks
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.mob.SlimeEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

class TerrarianSlimes: ModInitializer {

    override fun onInitialize() {
        isCanvasLoaded = FabricLoader.getInstance().isModLoaded("canvas")

        PacketCompendium.onInitialize()
        ItemCompendium.initialize()
        EntityCompendium.initialize()

        FabricDefaultAttributeRegistry.register(EntityCompendium.GREEN_SLIME, HostileEntity.createHostileAttributes())
        FabricDefaultAttributeRegistry.register(EntityCompendium.BLUE_SLIME, HostileEntity.createHostileAttributes())
        FabricDefaultAttributeRegistry.register(EntityCompendium.RED_SLIME, HostileEntity.createHostileAttributes())
        FabricDefaultAttributeRegistry.register(EntityCompendium.PURPLE_SLIME, HostileEntity.createHostileAttributes())
        FabricDefaultAttributeRegistry.register(EntityCompendium.YELLOW_SLIME, HostileEntity.createHostileAttributes())
        FabricDefaultAttributeRegistry.register(EntityCompendium.BLACK_SLIME, HostileEntity.createHostileAttributes())
        FabricDefaultAttributeRegistry.register(EntityCompendium.ICE_SLIME, HostileEntity.createHostileAttributes())
        FabricDefaultAttributeRegistry.register(EntityCompendium.SAND_SLIME, HostileEntity.createHostileAttributes())
        FabricDefaultAttributeRegistry.register(EntityCompendium.JUNGLE_SLIME, HostileEntity.createHostileAttributes())
        FabricDefaultAttributeRegistry.register(EntityCompendium.MOTHER_SLIME, HostileEntity.createHostileAttributes())
        FabricDefaultAttributeRegistry.register(EntityCompendium.BABY_SLIME, HostileEntity.createHostileAttributes())
        FabricDefaultAttributeRegistry.register(EntityCompendium.LAVA_SLIME, HostileEntity.createHostileAttributes())
        FabricDefaultAttributeRegistry.register(EntityCompendium.PINKY, HostileEntity.createHostileAttributes())
        FabricDefaultAttributeRegistry.register(EntityCompendium.UMBRELLA_SLIME, HostileEntity.createHostileAttributes())
        FabricDefaultAttributeRegistry.register(EntityCompendium.CORRUPT_SLIME, HostileEntity.createHostileAttributes())
        FabricDefaultAttributeRegistry.register(EntityCompendium.SLIMELING, HostileEntity.createHostileAttributes())
        FabricDefaultAttributeRegistry.register(EntityCompendium.CRIMSLIME, HostileEntity.createHostileAttributes())
        FabricDefaultAttributeRegistry.register(EntityCompendium.ILLUMINANT_SLIME, HostileEntity.createHostileAttributes())
        FabricDefaultAttributeRegistry.register(EntityCompendium.RAINBOW_SLIME, HostileEntity.createHostileAttributes())

    }

    companion object {
        const val MOD_ID = "terrarianslimes"
        var isCanvasLoaded = false
            private set
        private val creativeTab = FabricItemGroupBuilder.create(ModIdentifier("creative_tab")).icon{ ItemStack(Blocks.SLIME_BLOCK) }.build()
        fun creativeGroupSettings(): Item.Settings = Item.Settings().group(creativeTab)
    }

}