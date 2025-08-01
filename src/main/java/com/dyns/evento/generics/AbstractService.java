package com.dyns.evento.generics;

import com.dyns.evento.error.exceptions.NotFoundException;
import com.dyns.evento.utils.ClassUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

@Slf4j
public abstract class AbstractService<
        T, ID,
        TRepo extends JpaRepository<T, ID>> {
    protected TRepo repository;
    protected String className = ClassUtils.getName(getEntityClass().getClass());

    protected AbstractService(TRepo repository) {
        this.repository = repository;
    }

    @Transactional
    public T save(T input) {
        try {
            return repository.save(input);
        } catch (Exception exception) {
            log.info(exception.getCause().getMessage());
            throw exception;
        }
    }

    @Transactional(readOnly = true)
    public T findById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(className));
    }

    @Transactional(readOnly = true)
    public Collection<? extends T> findAll() {
        return repository.findAll();
    }

    @Transactional
    public abstract T partialUpdate(ID uuid, T input);

    @Transactional
    public abstract T fullUpdate(ID uuid, T input);

    @Transactional
    public void delete(ID id) {
        repository.findById(id)
                .ifPresentOrElse(
                        repository::delete,
                        () -> {
                            throw new RuntimeException(className);
                        }
                );
    }

    private Type getEntityClass() {
        Type superClass = getClass().getGenericSuperclass();
        return ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }
}
