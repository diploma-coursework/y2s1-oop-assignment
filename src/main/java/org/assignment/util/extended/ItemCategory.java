package org.assignment.util.extended;

import org.assignment.util.base.BaseItemCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemCategory extends BaseItemCategory {
    private List<Item> items;

    public ItemCategory(){
        this(null,null, (Item) null);
    }

    public ItemCategory(String name, String description, Item... items) {
        super(name, description);
        this.items = new ArrayList<>(Arrays.asList(items));
    }

    // Getter
    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    // Setter
    public void setItems(List<Item> items) {
        this.items = new ArrayList<>(items);
    }

    // add an item to existing list
    public void addItem(Item item) {
        this.items.add(item);
    }
}
