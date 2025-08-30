package com.urbancart.in.repository;

//import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbancart.in.model.Notification;


public interface NotificationRepository extends JpaRepository<Notification, Long> {



}
