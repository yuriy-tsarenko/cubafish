package com.cubafish.repository;

import com.cubafish.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    Boolean existsStatisticsByReportingPeriod(String period);

    Statistics findStatisticsByReportingPeriod(String period);
}
