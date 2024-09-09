package org.assignment.util.extended;

import org.assignment.util.base.BaseItemCategory;
import java.util.ArrayList;



public class ItemCategory extends BaseItemCategory {
    public ItemCategory() { this("", "", ""); }

    public ItemCategory(String id, String name, String description) {
        super(id, name, description);
    }

    public ArrayList<Item> getItems() {
        return new ArrayList<>();
    }
}
