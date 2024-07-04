﻿// <auto-generated> This file has been auto generated by EF Core Power Tools. </auto-generated>
#nullable disable
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Interview.Domain.Entities.Base;
using Microsoft.EntityFrameworkCore;

namespace Interview.Domain.Entities.Models;

[Table("CandidateDocuments")]
public  class CandidateDocument : BaseEntity
{
    [Key]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public int Id { get; set; }

    public string Surname { get; set; }

    [Required]
    public string Name { get; set; }

    public string Patronymic { get; set; }

    public string Phonenumber { get; set; }

    [Required]
    public string Email { get; set; }

    [Required]
    public string CV { get; set; }

    public string Address { get; set; }

    [InverseProperty("CandidateDocument")]
    public virtual ICollection<Candidate> Candidate { get; set; } = new List<Candidate>();
}