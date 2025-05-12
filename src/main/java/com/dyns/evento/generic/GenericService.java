package com.dyns.evento.generic;

import java.util.Collection;

/**
 * Interface used for services to implements basic CRUD functions.
 *
 * @param <T>  The persisted class type.
 * @param <ID> Long, String, UUID, ...
 */
public interface GenericService<T extends Identifiable<ID>, ID> {
    T create(T input);

    T getById(ID id);

    Collection<? extends T> getAll();

    T edit(ID id, T changes);

    T update(ID id, T update);

    void delete(ID id);
}
