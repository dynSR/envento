package com.dyns.evento.generics;

import java.util.Collection;

public interface Fixture<T extends Identifiable<ID>, ID> {
    T getOne();

    Collection<? extends T> getMany();

    int getFixtureAmount();
}
