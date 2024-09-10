package org.assignment.util.base;

import java.util.ArrayList;

public interface DatabaseOperations {
    boolean addToDb();
    boolean getFromDb(String id);
    boolean updateToDb();
    boolean deleteFromDb(String id);
}
