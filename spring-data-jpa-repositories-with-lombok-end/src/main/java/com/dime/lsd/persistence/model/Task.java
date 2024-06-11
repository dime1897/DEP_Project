package com.dime.lsd.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    @Builder.Default
    private String uuid = UUID.randomUUID()
        .toString();

    private String name;

    private String description;

    private LocalDate dueDate;

    @Builder.Default
    private TaskStatus status = TaskStatus.TO_DO;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    @ManyToOne
    private Worker assignee;
}
