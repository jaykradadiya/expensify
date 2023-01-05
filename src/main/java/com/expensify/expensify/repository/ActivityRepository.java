package com.expensify.expensify.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expensify.expensify.entity.Activity;
import com.expensify.expensify.entity.User;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

	List<Activity> findByUserOrderByTimestampDesc(User user);

}
