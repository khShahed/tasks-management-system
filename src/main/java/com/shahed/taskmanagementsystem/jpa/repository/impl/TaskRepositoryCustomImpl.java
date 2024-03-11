package com.shahed.taskmanagementsystem.jpa.repository.impl;

import com.shahed.taskmanagementsystem.dto.payload.SearchTaskRequest;
import com.shahed.taskmanagementsystem.jpa.entity.Task;
import com.shahed.taskmanagementsystem.jpa.repository.TaskRepositoryCustom;
import com.shahed.taskmanagementsystem.utils.DateUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class TaskRepositoryCustomImpl implements TaskRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Task> findByCriteria(SearchTaskRequest searchTaskRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);
        Root<Task> root = criteriaQuery.from(Task.class);

        // Creating predicates based on search criteria
        Predicate predicate = createPredicates(criteriaBuilder, root, searchTaskRequest);
        criteriaQuery.where(predicate);

        // Count query for pagination
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Task> countRoot = countQuery.from(Task.class);
        Predicate countPredicate = createPredicates(criteriaBuilder, countRoot, searchTaskRequest);
        countQuery.select(criteriaBuilder.count(countRoot));
        countQuery.where(countPredicate);

        // Fetch results with pagination
        var pageable = searchTaskRequest.getPageable();
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int firstResult = pageNumber * pageSize;
        List<Task> tasks = entityManager.createQuery(criteriaQuery)
            .setFirstResult(firstResult)
            .setMaxResults(pageSize)
            .getResultList();

        // Total count
        Long totalCount = entityManager.createQuery(countQuery)
            .getSingleResult();

        return new PageImpl<>(tasks, pageable, totalCount);
    }

    private Predicate createPredicates(CriteriaBuilder criteriaBuilder, Root<Task> root, SearchTaskRequest searchTaskRequest) {
        Predicate predicate = criteriaBuilder.conjunction();

        var title = searchTaskRequest.getTitle();
        if (title != null && !title.isEmpty()) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("title"), "%" + title + "%"));
        }

        var description = searchTaskRequest.getDescription();
        if (description != null && !description.isEmpty()) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("description"), "%" + description + "%"));
        }

        var status = searchTaskRequest.getStatus();
        if (status != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), status));
        }

        var priority = searchTaskRequest.getPriority();
        if (priority != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("priority"), priority));
        }

        var assigneeId = searchTaskRequest.getAssigneeId();
        if (assigneeId != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("assignee").get("id"), assigneeId));
        }

        var dueDate = DateUtils.convertOffsetDtoToInternalDto(searchTaskRequest.getDueDate());
        if (dueDate != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), dueDate));
        }

        return predicate;
    }
}
