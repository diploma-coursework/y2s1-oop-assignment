package org.assignment.util.base;

public interface DatabaseOperations {
    boolean addToDb();
    boolean updateToDb(String fieldName);
    boolean deleteFromDb();
}
