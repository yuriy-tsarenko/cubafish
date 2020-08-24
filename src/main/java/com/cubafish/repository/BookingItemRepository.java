package com.cubafish.repository;

import com.cubafish.entity.BookingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingItemRepository extends JpaRepository<BookingItem, Long> {

    BookingItem findBookingItemById(Long id);

    BookingItem findBookingItemByCreationTime(String creationTime);
}
