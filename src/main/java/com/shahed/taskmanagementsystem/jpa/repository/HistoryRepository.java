package com.shahed.taskmanagementsystem.jpa.repository;

import com.shahed.taskmanagementsystem.jpa.entity.History;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, UUID> {

}
