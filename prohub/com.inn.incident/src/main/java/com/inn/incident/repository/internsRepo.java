package com.inn.incident.repository;

import com.inn.incident.model.interns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface internsRepo extends JpaRepository<interns, Integer> {

    @Query("SELECT i FROM interns i WHERE i.status = 'true'")
    List<interns> findActiveInterns();
}
