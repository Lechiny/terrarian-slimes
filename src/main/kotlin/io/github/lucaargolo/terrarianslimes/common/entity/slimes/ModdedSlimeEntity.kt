package io.github.lucaargolo.terrarianslimes.common.entity.slimes

import io.github.lucaargolo.terrarianslimes.mixin.AccessorLootContextTypes
import io.github.lucaargolo.terrarianslimes.utils.ModIdentifier
import net.minecraft.entity.EntityData
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.SlimeEntity
import net.minecraft.item.ItemStack
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.context.LootContextType
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.ItemScatterer
import net.minecraft.util.math.MathHelper
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World

open class ModdedSlimeEntity(entityType: EntityType<ModdedSlimeEntity>, world: World, private val healthMultiplier: Double, private val speedMultiplier: Double, private val attackMultiplier: Double): SlimeEntity(entityType, world) {

    protected open fun getDefaultSize() = 2

    protected open fun getChildrenType(): EntityType<ModdedSlimeEntity>? = null
    protected open fun getChildrenQnt() = 0..0

    open fun hasBonusDrops() = true
    fun getBonusDrops(): ItemStack = this.dataTracker.get(BONUS_DROPS)
    var itemRotation = 0f

    override fun tick() {
        super.tick()
        itemRotation++
    }

    override fun setSize(size: Int, heal: Boolean) {
        super.setSize(size, heal)
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)?.baseValue = (size * size) * this.healthMultiplier
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)?.baseValue = (0.2F + 0.1F * size) * this.speedMultiplier
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)?.baseValue = size * this.attackMultiplier
        if (heal) {
            this.health = this.maxHealth
        }
        this.experiencePoints = MathHelper.floor(size * attackMultiplier)
    }

    override fun remove() {
        if (!world.isClient && this.isDead) {
            if(this.hasBonusDrops() && !this.getBonusDrops().isEmpty) {
                ItemScatterer.spawn(world, pos.x, pos.y, pos.z, this.getBonusDrops())
            }
            for (l in 0 until getChildrenQnt().random()) {
                val g = ((l % 2) - 0.5f) * this.size/4
                val h = ((l / 2) - 0.5f) * this.size/4
                val childrenEntity = this.getChildrenType()?.create(this.world) ?: break
                if (this.isPersistent) {
                    childrenEntity.setPersistent()
                }
                childrenEntity.customName = this.customName
                childrenEntity.isAiDisabled = this.isAiDisabled
                childrenEntity.isInvulnerable = this.isInvulnerable
                childrenEntity.setSize(childrenEntity.getDefaultSize(), true)
                childrenEntity.refreshPositionAndAngles(
                    this.x + g.toDouble(),
                    this.y + 0.5,
                    this.z + h.toDouble(),
                    this.random.nextFloat() * 360.0f,
                    0.0f
                )
                this.world.spawnEntity(childrenEntity)
            }
        }
        this.removed = true
    }

    override fun initialize(world: ServerWorldAccess, difficulty: LocalDifficulty, spawnReason: SpawnReason, entityData: EntityData?, entityTag: CompoundTag?): EntityData? {
        this.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE)?.addPersistentModifier(EntityAttributeModifier("Random spawn bonus", random.nextGaussian() * 0.05, EntityAttributeModifier.Operation.MULTIPLY_BASE))
        this.isLeftHanded = this.random.nextFloat() < 0.05f
        this.setSize(this.getDefaultSize(), true)
        if(this.hasBonusDrops()) {
            val serverWorld = world.toServerWorld()
            val table = serverWorld.server.lootManager.getTable(ModIdentifier("slimes/default_drops"))
            val ctx = LootContext.Builder(serverWorld).random(this.random).build(SLIMES_LOOT_CONTEXT)
            val stackList = table.generateLoot(ctx)
            this.dataTracker.set(BONUS_DROPS, stackList.firstOrNull() ?: ItemStack.EMPTY)
        }
        return entityData
    }

    override fun readCustomDataFromTag(tag: CompoundTag) {
        if(this.hasBonusDrops() && tag.contains("bonusDrops")) {
           this.dataTracker.set(BONUS_DROPS, ItemStack.fromTag(tag.getCompound("bonusDrops")))
        }
        super.readCustomDataFromTag(tag)
    }

    override fun writeCustomDataToTag(tag: CompoundTag) {
        if(this.hasBonusDrops() && !this.getBonusDrops().isEmpty) {
            tag.put("bonusDrops", this.getBonusDrops().toTag(CompoundTag()))
        }
        super.writeCustomDataToTag(tag)
    }

    override fun initDataTracker() {
        super.initDataTracker()
        this.dataTracker.startTracking(BONUS_DROPS, ItemStack.EMPTY)
    }

    companion object {
        private val BONUS_DROPS: TrackedData<ItemStack> = DataTracker.registerData(ModdedSlimeEntity::class.java, TrackedDataHandlerRegistry.ITEM_STACK)
        private val SLIMES_LOOT_CONTEXT: LootContextType = LootContextType.Builder().build()
        init {
            AccessorLootContextTypes.getMap()[ModIdentifier("slimes")] = SLIMES_LOOT_CONTEXT
        }
    }

}