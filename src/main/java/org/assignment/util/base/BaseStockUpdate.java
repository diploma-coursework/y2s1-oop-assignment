package org.assignment.util.base;

import java.time.LocalDate;
import org.assignment.util.enums.StockUpdateType;
import org.assignment.util.extended.Item;
import org.assignment.util.extended.Staff;

abstract public class BaseStockUpdate {
    protected String id;
    protected Item item;
    protected Staff staff;
    protected StockUpdateType updateType;
    protected int quantity;
    protected LocalDate updateDate;

    public BaseStockUpdate() {
        this(null, null, null, null, 0, null);
    }

    public BaseStockUpdate(String id, Item item, Staff staff, StockUpdateType updateType, int quantity, LocalDate updateDate) {
        this.id = id;
        this.item = item;
        this.staff = staff;
        this.updateType = updateType;
        this.quantity = quantity;
        this.updateDate = updateDate;
    }

    // setters
    public void setId(String id) {
        this.id = id;
    }
    public void setItem(Item item) {
        this.item = item;
    }
    public void setStaff(Staff staff) {
        this.staff = staff;
    }
    public void setUpdateType(StockUpdateType updateType) {
        this.updateType = updateType;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    // getters
    public String getId() {
        return id;
    }
    public BaseItem getItem() {
        return item;
    }
    public BaseStaff getStaff() {
        return staff;
    }
    public StockUpdateType getUpdateType() {
        return updateType;
    }
    public int getQuantity() {
        return quantity;
    }
    public LocalDate getUpdateDate() {
        return updateDate;
    }
}
