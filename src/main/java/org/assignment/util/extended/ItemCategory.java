package org.assignment.util.extended;

import org.assignment.util.base.BaseItemCategory;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemCategory extends BaseItemCategory {
    ArrayList<Item> items;

    public ItemCategory(){
        this(null,null, (Item) null);
    }

    public ItemCategory(String name, String description, Item... items){
        super(name,description);
        this.items = new ArrayList<Item>(Arrays.asList(items));
    }
}
