package ca.jrvs.apps.stockquote.dao;

import java.util.Optional;

public interface CrudDao<T, ID> {

    /**
     * Saves a given entity. Used for create and update
     * @param entity cannot be null
     * @return The saved entity
     * @throws IllegalArgumentException if entity is null
     */
    T save(T entity) throws IllegalArgumentException;

    /**
     * Return entity by id
     * @param id cannot be null
     * @return Entity with given id or empty optional if none found
     * @throws IllegalArgumentException when null id
     */
    Optional<T> findById(ID id) throws IllegalArgumentException;

    /**
     * Retrieve all entities
     * @return all entities
     */
    Iterable<T> findAll();

    /**
     * Deletes the entity with the given id. If the entity does not exist
     * it is silently ignored
     * @param id cannot be null
     * @throws IllegalArgumentException when null
     */
    void deleteById(ID id) throws IllegalArgumentException;

    /**
     * Deletes all entities managed by the repository
     */
    void deleteAll();
}
