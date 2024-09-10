package org.assignment.util.base;

import java.util.ArrayList;

public interface DatabaseOperations<T> {
    boolean addToDb(T entity);
    T getFromDb(String id);
    ArrayList<T> getAllFromDb();
    boolean updateInDb(String id, T entity);
    boolean deleteFromDb(String id);
}
