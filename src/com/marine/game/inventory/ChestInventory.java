package com.marine.game.inventory;

import com.marine.game.chat.ChatComponent;

/**
 * Created 2014-12-04 for MarineStandalone
 *
 * @author Citymonstret
 */
public class ChestInventory extends Inventory {

    private final String title;

    public ChestInventory(int rows, String title, byte uid) {
        super(rows * 9, uid);
        this.title = title;
    }

    public ChestInventory(int rows, byte uid) {
        this(rows, "Chest", uid);
    }

    @Override
    public byte getID() {
        return (byte) InventoryID.CHEST.getIntegerID();
    }

    @Override
    public String getType() {
        return InventoryID.CHEST.getStringID();
    }

    @Override
    public ChatComponent getTitle() {
        return new ChatComponent(title);
    }

    @Override
    public byte getNumberOfSlots() {
        return (byte) super.getSlots().length;
    }
}
