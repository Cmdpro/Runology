package com.cmdpro.runicarts.screen;

import com.cmdpro.runicarts.api.IAmplifierSoulcastersCrystal;
import com.cmdpro.runicarts.api.ISoulcastersCrystal;
import com.cmdpro.runicarts.init.BlockInit;
import com.cmdpro.runicarts.init.MenuInit;
import com.cmdpro.runicarts.screen.slot.AmplifierSoulcastersCrystalSlot;
import com.cmdpro.runicarts.screen.slot.SoulcastersCrystalSlot;
import com.cmdpro.runicarts.screen.slot.WandSlot;
import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class SoulcastersTableMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final Level level;
    long lastSoundTime;
    final List<Slot> invSlots = new ArrayList<>();
    final Slot resultSlot;
    public final Container container = new SimpleContainer(6) {
        /**
         * For block entities, ensures the chunk containing the block entity is saved to disk later - the game won't think
         * it hasn't changed and skip it.
         */
        public void setChanged() {
            super.setChanged();
            SoulcastersTableMenu.this.slotsChanged(this);
        }
    };
    final ResultContainer resultContainer = new ResultContainer();

    public SoulcastersTableMenu(int pContainerId, Inventory pPlayerInventory) {
        this(pContainerId, pPlayerInventory, ContainerLevelAccess.NULL);
    }

    public SoulcastersTableMenu(int pContainerId, Inventory pPlayerInventory, final ContainerLevelAccess pAccess) {
        super(MenuInit.SOULCASTERSTABLE_MENU.get(), pContainerId);
        this.access = pAccess;
        this.level = pPlayerInventory.player.level();
        addPlayerInventory(pPlayerInventory);
        addPlayerHotbar(pPlayerInventory);
        invSlots.add(this.addSlot(new WandSlot(this.container, 0, 102, 46)));
        invSlots.add(this.addSlot(new SoulcastersCrystalSlot(this.container, 1, 58, 24)));
        invSlots.add(this.addSlot(new SoulcastersCrystalSlot(this.container, 2, 80, 24)));
        invSlots.add(this.addSlot(new SoulcastersCrystalSlot(this.container, 3, 102, 24)));
        invSlots.add(this.addSlot(new AmplifierSoulcastersCrystalSlot(this.container, 4, 38, 35)));
        invSlots.add(this.addSlot(new AmplifierSoulcastersCrystalSlot(this.container, 5, 122, 35)));
        this.resultSlot = this.addSlot(new Slot(this.resultContainer, 0, 58, 46) {
            /**
             * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
             */
            public boolean mayPlace(ItemStack p_40362_) {
                return false;
            }

            public void onTake(Player p_150672_, ItemStack p_150673_) {
                p_150673_.onCraftedBy(p_150672_.level(), p_150672_, p_150673_.getCount());
                ItemStack itemstack = invSlots.get(0).remove(1);
                ItemStack itemstack2 = invSlots.get(1).remove(1);
                ItemStack itemstack3 = invSlots.get(2).remove(1);
                ItemStack itemstack4 = invSlots.get(3).remove(1);
                ItemStack itemstack5 = invSlots.get(4).remove(1);
                ItemStack itemstack6 = invSlots.get(5).remove(1);
                if (!itemstack.isEmpty()) {
                    SoulcastersTableMenu.this.setupResultSlot();
                }

                pAccess.execute((p_40364_, p_40365_) -> {
                    long l = p_40364_.getGameTime();
                    if (SoulcastersTableMenu.this.lastSoundTime != l) {
                        p_40364_.playSound((Player)null, p_40365_, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 2.0F, 1.0F);
                        SoulcastersTableMenu.this.lastSoundTime = l;
                    }

                });
                super.onTake(p_150672_, p_150673_);
            }
        });

    }


    /**
     * Determines whether supplied player can use this container
     */
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, BlockInit.SOULCASTERSTABLE.get());
    }

    void setupResultSlot() {
        ItemStack wand = invSlots.get(0).getItem().copy();
        if (!wand.is(ItemStack.EMPTY.getItem())) {
            CompoundTag tag = wand.getOrCreateTag();
            tag.remove("types");
            ListTag tag2 = new ListTag();
            for (int i = 1; i <= 3; i++) {
                if (invSlots.get(i).hasItem()) {
                    CompoundTag tag3 = new CompoundTag();
                    tag3.putString("id", ((ISoulcastersCrystal) invSlots.get(i).getItem().getItem()).getId());
                    tag2.add(tag3);
                }
            }
            tag.remove("amplifier");
            if (!tag2.isEmpty()) {
                tag.put("types", tag2);
                int amplifier = 1;
                for (int i = 4; i <= 5; i++) {
                    if (invSlots.get(i).hasItem()) {
                        amplifier += ((IAmplifierSoulcastersCrystal)invSlots.get(i).getItem().getItem()).getTimesAdd();
                    }
                }
                tag.putInt("amplifier", amplifier);
            }
        }
        this.resultSlot.set(wand);
        this.broadcastChanges();
    }

    public MenuType<?> getType() {
        return MenuInit.SOULCASTERSTABLE_MENU.get();
    }
    public void slotsChanged(Container pInventory) {
        setupResultSlot();
    }
    /**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in is
     * null for the initial slot that was double-clicked.
     */
    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.resultContainer && super.canTakeItemForPickAll(pStack, pSlot);
    }



    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 7;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    /**
     * Called when the container is closed.
     */
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.resultContainer.removeItemNoUpdate(1);
        this.access.execute((p_40313_, p_40314_) -> {
            this.clearContainer(pPlayer, this.container);
        });
    }
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }
}