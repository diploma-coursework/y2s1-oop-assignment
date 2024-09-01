package org.assignment.util.enums;


public enum StaffPosition {
    INVENTORY_CLERK(AccessLevel.STAFF),
    STOCK_CONTROLLER(AccessLevel.STAFF),
    WAREHOUSE_MANAGER(AccessLevel.MANAGER),
    PURCHASING_AGENT(AccessLevel.STAFF),
    INVENTORY_SUPERVISOR(AccessLevel.MANAGER),
    ADMIN(AccessLevel.ADMIN);

    private final AccessLevel accessLevel;  // Changed to instance variable

    StaffPosition(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public AccessLevel getAccessLevel() {
        return this.accessLevel;
    }
}