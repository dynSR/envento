package com.dyns.evento.generics;

/**
 * Base interface to be implemented in all object using an ID.
 * This makes the use of generic services possible.
 *
 * @param <ID> Long, String, UUID, ...
 */
public interface Identifiable<ID> {
    ID getId();
}
