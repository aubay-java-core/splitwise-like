package pt.community.java.splitwise_like.commons;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.community.java.splitwise_like.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public abstract class AbstractCrudService<T, ID> {

    protected abstract JpaRepository<T, ID> getRepository();

    public Optional<T> findById(ID id) {

        var optionalEntity = getRepository().findById(id);

        if (optionalEntity.isEmpty()) {
            throw new ResourceNotFoundException("Resource with ID " + id + " not found");
        }
        return optionalEntity;
    }

    public T save(T entity) {
        return getRepository().save(entity);
    }

    public void delete(ID id) {
        this.findById(id);
        getRepository().deleteById(id);

    }

    public List<T> findAll() {
        return getRepository().findAll();
    }
}