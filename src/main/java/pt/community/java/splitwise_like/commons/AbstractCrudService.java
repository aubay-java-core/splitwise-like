package pt.community.java.splitwise_like.commons;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class AbstractCrudService<T, ID> {

    protected abstract JpaRepository<T, ID> getRepository();

    public Optional<T> findById(ID id) {
        return getRepository().findById(id);
    }

    public T save(T entity) {
        return getRepository().save(entity);
    }

    public void delete(ID id) {
        getRepository().deleteById(id);
    }

    public List<T> findAll() {
        return getRepository().findAll();
    }
}