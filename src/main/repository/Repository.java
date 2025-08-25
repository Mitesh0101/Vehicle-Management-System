package main.repository;

import java.sql.Connection;

public abstract class Repository {
    public abstract void save(Object obj);
}
