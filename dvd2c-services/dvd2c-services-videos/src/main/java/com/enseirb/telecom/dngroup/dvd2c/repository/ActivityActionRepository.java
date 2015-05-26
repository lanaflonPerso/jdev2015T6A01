package com.enseirb.telecom.dngroup.dvd2c.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.ActivityAction;;

@Repository
public interface ActivityActionRepository extends CrudRepository<ActivityAction, Integer> {
	
	
}
