package org.assignment.util.base;

import java.util.ArrayList;
import java.util.HashMap;

public interface DatabaseOperations {
    boolean addToDb();
    boolean getFromDb(String id);
    boolean updateToDb(String fieldName);
    boolean deleteFromDb();
}
