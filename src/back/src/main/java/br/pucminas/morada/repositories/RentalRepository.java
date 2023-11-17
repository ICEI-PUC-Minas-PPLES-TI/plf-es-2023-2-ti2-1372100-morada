package br.pucminas.morada.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import br.pucminas.morada.models.rental.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long>, JpaSpecificationExecutor<Rental> {

    List<Rental> findByUserId(Long userId);

    @Query(value = "SELECT  r.property_id AS propertyId, r.id AS rentalId, p.photo_base64, p.street, p.neighborhood, p.description, p.id from rental r  join property p  WHERE r.user_id = :user_id", nativeQuery = true)
    List<Map<String, Object>> findAllRentsByUserId(@Param("user_id") Long user_id);

}
