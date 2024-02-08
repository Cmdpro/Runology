package com.cmdpro.runology.screen;

import com.cmdpro.runology.api.*;
import com.cmdpro.runology.init.BlockInit;
import com.cmdpro.runology.init.MenuInit;
import com.cmdpro.runology.screen.slot.CastingDeviceSlot;
import com.cmdpro.runology.screen.slot.CastingUpgradeSlot;
import com.cmdpro.runology.screen.slot.RuneSlot;
import com.cmdpro.runology.screen.slot.RuneSlotContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class CastingTableMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final Level level;
    final List<Slot> invSlots = new ArrayList<>();
    public final Container container = new SimpleContainer(6) {
        public void setChanged() {
            super.setChanged();
            CastingTableMenu.this.slotsChanged(this);
        }
    };

    public CastingTableMenu(int pContainerId, Inventory pPlayerInventory) {
        this(pContainerId, pPlayerInventory, ContainerLevelAccess.NULL);
    }

    public CastingTableMenu(int pContainerId, Inventory pPlayerInventory, final ContainerLevelAccess pAccess) {
        super(MenuInit.CASTINGTABLEMENU.get(), pContainerId);
        this.access = pAccess;
        this.level = pPlayerInventory.player.level();
        addPlayerInventory(pPlayerInventory);
        addPlayerHotbar(pPlayerInventory);
        invSlots.add(this.addSlot(new CastingDeviceSlot(this.container, 0, 80, 33)));
        invSlots.add(this.addSlot(new RuneSlotContainer(this.container, 1, 33, 33)));
        invSlots.add(this.addSlot(new CastingUpgradeSlot(this.container, 2, 127, 33)));
    }
    /**
     * Determines whether supplied player can use this container
     */
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, BlockInit.CASTINGTABLE.get());
    }

    void setupResultSlot() {
        ItemStack castingDevice = invSlots.get(0).getItem();
        if (!castingDevice.is(ItemStack.EMPTY.getItem())) {
            CompoundTag tag = castingDevice.getOrCreateTag();
            if (invSlots.get(2).hasItem() && invSlots.get(2).getItem().getItem() instanceof UpgradeItem item) {
                ListTag tag2 = tag.contains("upgrades") ? (ListTag) tag.get("upgrades") : new ListTag();
                if (tag2.size() < 3) {
                    String upgrade = item.upgrade.toString();
                    boolean alreadyHas = false;
                    for (Tag i : tag2) {
                        if (i instanceof CompoundTag tag3) {
                            if (tag3.getString("upgrade").equals(upgrade)) {
                                alreadyHas = true;
                                break;
                            }
                        }
                    }
                    if (!alreadyHas) {
                        CompoundTag tag3 = new CompoundTag();
                        tag3.putString("upgrade", upgrade);
                        tag2.add(tag3);
                    }
                    invSlots.get(2).getItem().shrink(1);
                }
                tag.put("upgrades", tag2);
            }
            if (invSlots.get(1).hasItem() && invSlots.get(1).getItem().getItem() instanceof RuneItem item) {
                CompoundTag tag2 = tag.contains("runicEnergy") ? (CompoundTag) tag.get("runicEnergy") : new CompoundTag();
                float max = 100;
                if (castingDevice.getItem() instanceof Gauntlet gauntlet) {
                    max = gauntlet.maxRunicEnergy;
                }
                if (castingDevice.getItem() instanceof Staff staff) {
                    max = staff.maxRunicEnergy;
                }
                if (tag2.getFloat(item.runicEnergyType.toString()) <= max) {
                    int costToFill = (int)Math.ceil((max-tag2.getFloat(item.runicEnergyType.toString()))/50);
                    int cost = Math.min(invSlots.get(1).getItem().getCount(), costToFill);
                    float newTotal = tag2.getFloat(item.runicEnergyType.toString()) + (float)cost*50f;
                    if (newTotal > max) {
                        newTotal = max;
                    }
                    tag2.putFloat(item.runicEnergyType.toString(), newTotal);
                    tag.put("runicEnergy", tag2);
                    invSlots.get(1).getItem().shrink(cost);
                }
            }
        }
        this.broadcastChanges();
    }

    public MenuType<?> getType() {
        return MenuInit.CASTINGTABLEMENU.get();
    }
    public void slotsChanged(Container pInventory) {
        setupResultSlot();
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
    private static final int TE_INVENTORY_SLOT_COUNT = 3;  // must be the number of slots you have!

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
        this.access.execute((p_40313_, p_40314_) -> {
            this.clearContainer(pPlayer, this.container);
        });
    }
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}