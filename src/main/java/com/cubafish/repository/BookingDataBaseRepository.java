package com.cubafish.repository;

import com.cubafish.entity.BookingDataBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingDataBaseRepository extends JpaRepository<BookingDataBase, Long> {

}
