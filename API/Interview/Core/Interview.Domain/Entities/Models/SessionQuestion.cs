﻿// <auto-generated> This file has been auto generated by EF Core Power Tools. </auto-generated>
#nullable disable
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Interview.Domain.Entities.Base;
using Microsoft.EntityFrameworkCore;

namespace Interview.Domain.Entities.Models;


[Table("SessionQuestions")]
public  class SessionQuestion : BaseEntity
{
    [Key]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public int Id { get; set; }

    public int? Value { get; set; }

    public int SessionId { get; set; }

    public int QuestionId { get; set; }

    [ForeignKey("QuestionId")]
    [InverseProperty("SessionQuestion")]
    public virtual Question Question { get; set; }

    [ForeignKey("SessionId")]
    [InverseProperty("SessionQuestion")]
    public virtual Session Session { get; set; }
}