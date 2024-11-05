package es.com.w2m.dls.dao.repository;

import es.com.w2m.dls.dao.entity.ShipEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface IShipRepository extends CrudRepository<ShipEntity, Long>, JpaSpecificationExecutor<ShipEntity> {
    Page<ShipEntity> findAll(Pageable pageable);
    Page<ShipEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
