package com.cubafish.repository;

import com.cubafish.entity.BookingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingListRepository extends JpaRepository<BookingList, Long> {

}
