package org.assignment.util.extended;

import org.assignment.util.base.BaseItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Item extends BaseItem {

    public void increaseQuantity(int amount) {
        // Increment the quantity
        this.setQuantity(this.getQuantity() + amount);
        // Update the quantity in the database
        // updateQuantityInDatabase();
    }

    private void updateQuantityInDatabase() {
        // Get a reference to the database
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        // Update the quantity in Firebase (adjust the path if necessary)
        database.child("items").child("P001").child("quantity").setValue(2, null, (databaseError, databaseReference) -> {
            if (databaseError == null) {
                System.out.println("Quantity successfully updated.");
            } else {
                System.err.println("Failed to update quantity: " + databaseError.getMessage());
            }
        });
    }
}
