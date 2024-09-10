package org.assignment.util.base;

import java.util.ArrayList;
import java.util.HashMap;

public interface DatabaseOperations {
    boolean addToDb();
    boolean getFromDb(String id);
    boolean updateToDb();
    boolean deleteFromDb(String id);
    HashMap<String, Object> toHashMapExcludeId();
}
