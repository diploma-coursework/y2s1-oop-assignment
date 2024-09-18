package org.assignment.util.enums;

public enum StaffPosition {
    INVENTORY_CLERK(AccessLevel.STAFF),         // STF
    STOCK_CONTROLLER(AccessLevel.STAFF),        // STF
    SUPPLIER(AccessLevel.STAFF),                // STF
    WAREHOUSE_MANAGER(AccessLevel.MANAGER),     // MNG
    INVENTORY_SUPERVISOR(AccessLevel.MANAGER),  // MNG
    ADMIN(AccessLevel.ADMIN);                   // ADM

    private final AccessLevel accessLevel;  // Changed to instance variable

    StaffPosition(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public AccessLevel getAccessLevel() {
        return this.accessLevel;
    }

    public static StaffPosition fromString(String positionName) {
        for (StaffPosition position : StaffPosition.values()) {
            if (position.name().equalsIgnoreCase(positionName)) return position;
        }
        return null;
    }

    public static void printPosition() {
        System.out.println("========================================");
        System.out.println("0) INVENTORY_CLERK\n" +
                           "1) STOCK_CONTROLLER\n" +
                           "2) SUPPLIER   \n" +
                           "3) WAREHOUSE_MANAGER   \n" +
                           "4) INVENTORY_SUPERVISOR \n" +
                           "5) ADMIN");
        System.out.println("========================================");
    }

    public static StaffPosition fromNumber(int number) {
        if (number >= 0 && number < StaffPosition.values().length) {
            return StaffPosition.values()[number];
        } else {
            throw new IllegalArgumentException("Invalid position number.");
        }
    }
}
