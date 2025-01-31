package com.inn.incident.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "interns")
public class interns implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "mobile", nullable = false)
    private String mobile;

    @Column(name = "nic", nullable = false)
    private String nic;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "startDate", nullable = false)
    private String startDate;

    @Column(name = "endDate", nullable = false)
    private String endDate;

    @Column(name = "institute", nullable = false)
    private String institute;

    @Column(name = "languagesKnown", nullable = false)
    private String languagesKnown;

    @Column(name = "field", nullable = false)
    private String field;

    @Column(name = "supervisor", nullable = false)
    private String supervisor;

    @Column(name = "assignedWork", nullable = false)
    private String assignedWork;

    @Column(name = "targetDate", nullable = false)
    private String targetDate;

    @Column(name = "status", nullable = false)
    private String status = "true";
}
