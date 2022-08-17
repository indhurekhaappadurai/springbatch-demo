package com.example.springbatch.repository;

import com.example.springbatch.entity.PersonsDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonDetailsRepository extends JpaRepository<PersonsDetails,Integer> {
}
