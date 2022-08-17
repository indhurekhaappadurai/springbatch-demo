package com.example.springbatch.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Persons_Info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonsDetails {

    @Id
    @Column(name = "PersonId")
    private int id;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "emailId")
    private String emailId;

    @Column(name = "Gender")
    private String gender;

    @Column(name = "ContactNo")
    private String contactNo;

    @Column(name="Country")
     private String Country;

}
