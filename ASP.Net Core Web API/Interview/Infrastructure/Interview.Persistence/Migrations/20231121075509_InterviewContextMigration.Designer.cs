﻿// <auto-generated />
using System;
using Interview.Persistence.Contexts.InterviewDbContext;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;

#nullable disable

namespace Interview.Persistence.Migrations
{
    [DbContext(typeof(InterviewContext))]
    [Migration("20231121075509_InterviewContextMigration")]
    partial class InterviewContextMigration
    {
        /// <inheritdoc />
        protected override void BuildTargetModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "7.0.12")
                .HasAnnotation("Relational:MaxIdentifierLength", 128);

            SqlServerModelBuilderExtensions.UseIdentityColumns(modelBuilder);

            modelBuilder.Entity("Interview.Domain.Entities.IdentityAuth.Role", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    SqlServerPropertyBuilderExtensions.UseIdentityColumn(b.Property<int>("Id"));

                    b.Property<string>("ConcurrencyStamp")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Name")
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id")
                        .HasName("PK__Role__3214EC07C59F717C");

                    b.ToTable("Roles");
                });

            modelBuilder.Entity("Interview.Domain.Entities.IdentityAuth.RoleClaim", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    SqlServerPropertyBuilderExtensions.UseIdentityColumn(b.Property<int>("Id"));

                    b.Property<string>("ClaimType")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("ClaimValue")
                        .HasColumnType("nvarchar(max)");

                    b.Property<int>("RoleId")
                        .HasColumnType("int");

                    b.HasKey("Id")
                        .HasName("PK__RoleClai__3214EC0701854A8C");

                    b.HasIndex("RoleId");

                    b.ToTable("RoleClaims");
                });

            modelBuilder.Entity("Interview.Domain.Entities.IdentityAuth.User", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    SqlServerPropertyBuilderExtensions.UseIdentityColumn(b.Property<int>("Id"));

                    b.Property<string>("ConcurrencyStamp")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Email")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("ImagePath")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Password")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Phonenumber")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("RefreshToken")
                        .HasColumnType("nvarchar(max)");

                    b.Property<DateTime?>("RefreshTokenExpiryTime")
                        .HasColumnType("datetime2");

                    b.Property<string>("UserName")
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id")
                        .HasName("PK__User__3214EC07FA4259EB");

                    b.ToTable("Users");
                });

            modelBuilder.Entity("Interview.Domain.Entities.IdentityAuth.UserClaim", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    SqlServerPropertyBuilderExtensions.UseIdentityColumn(b.Property<int>("Id"));

                    b.Property<string>("ClaimType")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("ClaimValue")
                        .HasColumnType("nvarchar(max)");

                    b.Property<int>("UserId")
                        .HasColumnType("int");

                    b.HasKey("Id")
                        .HasName("PK__UserClai__3214EC076DCED94E");

                    b.HasIndex("UserId");

                    b.ToTable("UserClaims");
                });

            modelBuilder.Entity("Interview.Domain.Entities.IdentityAuth.UserRole", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    SqlServerPropertyBuilderExtensions.UseIdentityColumn(b.Property<int>("Id"));

                    b.Property<int>("RoleId")
                        .HasColumnType("int");

                    b.Property<int>("UserId")
                        .HasColumnType("int");

                    b.HasKey("Id")
                        .HasName("PK__UserRole__3214EC079524A0B8");

                    b.HasIndex("RoleId");

                    b.HasIndex("UserId");

                    b.ToTable("UserRoles");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Candidate", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    SqlServerPropertyBuilderExtensions.UseIdentityColumn(b.Property<int>("Id"));

                    b.Property<int>("CandidateDocumentId")
                        .HasColumnType("int");

                    b.HasKey("Id")
                        .HasName("PK__Candidat__3214EC07469F46D4");

                    b.HasIndex("CandidateDocumentId");

                    b.ToTable("Candidates");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.CandidateDocument", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    SqlServerPropertyBuilderExtensions.UseIdentityColumn(b.Property<int>("Id"));

                    b.Property<string>("Address")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("CV")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Email")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Name")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Patronymic")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Phonenumber")
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("Surname")
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id")
                        .HasName("PK__Candidat__3214EC07412C1956");

                    b.ToTable("CandidateDocuments");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Category", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    SqlServerPropertyBuilderExtensions.UseIdentityColumn(b.Property<int>("Id"));

                    b.Property<string>("Name")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id")
                        .HasName("PK__Category__3214EC072D3EC4B7");

                    b.ToTable("Categories");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Level", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    SqlServerPropertyBuilderExtensions.UseIdentityColumn(b.Property<int>("Id"));

                    b.Property<decimal>("Coefficient")
                        .HasColumnType("decimal(18, 0)");

                    b.Property<string>("Name")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id")
                        .HasName("PK__Level__3214EC0735A80859");

                    b.ToTable("Levels");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Position", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    SqlServerPropertyBuilderExtensions.UseIdentityColumn(b.Property<int>("Id"));

                    b.Property<string>("Name")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id")
                        .HasName("PK__Position__3214EC0730660A0E");

                    b.ToTable("Positions");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Question", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    SqlServerPropertyBuilderExtensions.UseIdentityColumn(b.Property<int>("Id"));

                    b.Property<int>("CategoryId")
                        .HasColumnType("int");

                    b.Property<int>("LevelId")
                        .HasColumnType("int");

                    b.Property<int>("StructureId")
                        .HasColumnType("int");

                    b.Property<string>("Text")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id")
                        .HasName("PK__Question__3214EC07C839599E");

                    b.HasIndex("CategoryId");

                    b.HasIndex("LevelId");

                    b.HasIndex("StructureId");

                    b.ToTable("Questions");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Session", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    SqlServerPropertyBuilderExtensions.UseIdentityColumn(b.Property<int>("Id"));

                    b.Property<int>("CandidateId")
                        .HasColumnType("int");

                    b.Property<DateTime?>("EndDate")
                        .HasColumnType("datetime2");

                    b.Property<decimal?>("EndValue")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("decimal(18, 0)")
                        .HasDefaultValueSql("((0.0))");

                    b.Property<DateTime?>("StartDate")
                        .HasColumnType("datetime2");

                    b.Property<int>("UserId")
                        .HasColumnType("int");

                    b.Property<int>("VacancyId")
                        .HasColumnType("int");

                    b.HasKey("Id")
                        .HasName("PK__Session__3214EC07289FC67F");

                    b.HasIndex("CandidateId");

                    b.HasIndex("UserId");

                    b.HasIndex("VacancyId");

                    b.ToTable("Sessions");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.SessionQuestion", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    SqlServerPropertyBuilderExtensions.UseIdentityColumn(b.Property<int>("Id"));

                    b.Property<int>("QuestionId")
                        .HasColumnType("int");

                    b.Property<int>("SessionId")
                        .HasColumnType("int");

                    b.Property<int?>("Value")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int")
                        .HasDefaultValueSql("((0))");

                    b.HasKey("Id")
                        .HasName("PK__SessionQ__3214EC07DABC968F");

                    b.HasIndex("QuestionId");

                    b.HasIndex("SessionId");

                    b.ToTable("SessionQuestions");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Structure", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    SqlServerPropertyBuilderExtensions.UseIdentityColumn(b.Property<int>("Id"));

                    b.Property<string>("Name")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.Property<string>("ParentId")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.Property<int>("StructureTypeId")
                        .HasColumnType("int");

                    b.HasKey("Id")
                        .HasName("PK__Structur__3214EC077D1F08DF");

                    b.HasIndex("StructureTypeId");

                    b.ToTable("Structures");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.StructureType", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    SqlServerPropertyBuilderExtensions.UseIdentityColumn(b.Property<int>("Id"));

                    b.Property<string>("Name")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id")
                        .HasName("PK__Structur__3214EC0720B22513");

                    b.ToTable("StructureTypes");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Vacancy", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    SqlServerPropertyBuilderExtensions.UseIdentityColumn(b.Property<int>("Id"));

                    b.Property<string>("Description")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.Property<DateTime>("EndDate")
                        .HasColumnType("datetime2");

                    b.Property<int>("PositionId")
                        .HasColumnType("int");

                    b.Property<DateTime>("StartDate")
                        .HasColumnType("datetime2");

                    b.Property<int>("StructureId")
                        .HasColumnType("int");

                    b.Property<string>("Title")
                        .IsRequired()
                        .HasColumnType("nvarchar(max)");

                    b.HasKey("Id")
                        .HasName("PK__Vacancy__3214EC07C2961250");

                    b.HasIndex("PositionId");

                    b.HasIndex("StructureId");

                    b.ToTable("Vacancies");
                });

            modelBuilder.Entity("Interview.Domain.Entities.IdentityAuth.RoleClaim", b =>
                {
                    b.HasOne("Interview.Domain.Entities.IdentityAuth.Role", "Role")
                        .WithMany("RoleClaim")
                        .HasForeignKey("RoleId")
                        .IsRequired()
                        .HasConstraintName("FK_RoleId_forRoleClaim");

                    b.Navigation("Role");
                });

            modelBuilder.Entity("Interview.Domain.Entities.IdentityAuth.UserClaim", b =>
                {
                    b.HasOne("Interview.Domain.Entities.IdentityAuth.User", "User")
                        .WithMany("UserClaim")
                        .HasForeignKey("UserId")
                        .IsRequired()
                        .HasConstraintName("FK_UserId_forUserClaim");

                    b.Navigation("User");
                });

            modelBuilder.Entity("Interview.Domain.Entities.IdentityAuth.UserRole", b =>
                {
                    b.HasOne("Interview.Domain.Entities.IdentityAuth.Role", "Role")
                        .WithMany("UserRole")
                        .HasForeignKey("RoleId")
                        .IsRequired()
                        .HasConstraintName("FK_RoleId_forUserRole");

                    b.HasOne("Interview.Domain.Entities.IdentityAuth.User", "User")
                        .WithMany("UserRole")
                        .HasForeignKey("UserId")
                        .IsRequired()
                        .HasConstraintName("FK_UserId_forUserRole");

                    b.Navigation("Role");

                    b.Navigation("User");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Candidate", b =>
                {
                    b.HasOne("Interview.Domain.Entities.Models.CandidateDocument", "CandidateDocument")
                        .WithMany("Candidate")
                        .HasForeignKey("CandidateDocumentId")
                        .IsRequired()
                        .HasConstraintName("FK_CandidateDocument_forCandidates");

                    b.Navigation("CandidateDocument");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Question", b =>
                {
                    b.HasOne("Interview.Domain.Entities.Models.Category", "Category")
                        .WithMany("Question")
                        .HasForeignKey("CategoryId")
                        .IsRequired()
                        .HasConstraintName("FK_CategoryId_forQuestion");

                    b.HasOne("Interview.Domain.Entities.Models.Level", "Level")
                        .WithMany("Question")
                        .HasForeignKey("LevelId")
                        .IsRequired()
                        .HasConstraintName("FK_LevelId_forQuestion");

                    b.HasOne("Interview.Domain.Entities.Models.Structure", "Structure")
                        .WithMany("Question")
                        .HasForeignKey("StructureId")
                        .IsRequired()
                        .HasConstraintName("FK_StructureId_forQuestion");

                    b.Navigation("Category");

                    b.Navigation("Level");

                    b.Navigation("Structure");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Session", b =>
                {
                    b.HasOne("Interview.Domain.Entities.Models.Candidate", "Candidate")
                        .WithMany("Session")
                        .HasForeignKey("CandidateId")
                        .IsRequired()
                        .HasConstraintName("FK_CandidateId_forSession");

                    b.HasOne("Interview.Domain.Entities.IdentityAuth.User", "User")
                        .WithMany("Session")
                        .HasForeignKey("UserId")
                        .IsRequired()
                        .HasConstraintName("FK_UserId_forSession");

                    b.HasOne("Interview.Domain.Entities.Models.Vacancy", "Vacancy")
                        .WithMany("Session")
                        .HasForeignKey("VacancyId")
                        .IsRequired()
                        .HasConstraintName("FK_VacancyId_forSession");

                    b.Navigation("Candidate");

                    b.Navigation("User");

                    b.Navigation("Vacancy");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.SessionQuestion", b =>
                {
                    b.HasOne("Interview.Domain.Entities.Models.Question", "Question")
                        .WithMany("SessionQuestion")
                        .HasForeignKey("QuestionId")
                        .IsRequired()
                        .HasConstraintName("FK_QuestionId_forSessionQuestion");

                    b.HasOne("Interview.Domain.Entities.Models.Session", "Session")
                        .WithMany("SessionQuestion")
                        .HasForeignKey("SessionId")
                        .IsRequired()
                        .HasConstraintName("FK_SessionId_forSessionQuestion");

                    b.Navigation("Question");

                    b.Navigation("Session");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Structure", b =>
                {
                    b.HasOne("Interview.Domain.Entities.Models.StructureType", "StructureType")
                        .WithMany("Structure")
                        .HasForeignKey("StructureTypeId")
                        .IsRequired()
                        .HasConstraintName("FK_StructureType_forStructure");

                    b.Navigation("StructureType");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Vacancy", b =>
                {
                    b.HasOne("Interview.Domain.Entities.Models.Position", "Position")
                        .WithMany("Vacancy")
                        .HasForeignKey("PositionId")
                        .IsRequired()
                        .HasConstraintName("FK_PositionId_forVacancy");

                    b.HasOne("Interview.Domain.Entities.Models.Structure", "Structure")
                        .WithMany("Vacancy")
                        .HasForeignKey("StructureId")
                        .IsRequired()
                        .HasConstraintName("FK_StructureId_forVacancy");

                    b.Navigation("Position");

                    b.Navigation("Structure");
                });

            modelBuilder.Entity("Interview.Domain.Entities.IdentityAuth.Role", b =>
                {
                    b.Navigation("RoleClaim");

                    b.Navigation("UserRole");
                });

            modelBuilder.Entity("Interview.Domain.Entities.IdentityAuth.User", b =>
                {
                    b.Navigation("Session");

                    b.Navigation("UserClaim");

                    b.Navigation("UserRole");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Candidate", b =>
                {
                    b.Navigation("Session");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.CandidateDocument", b =>
                {
                    b.Navigation("Candidate");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Category", b =>
                {
                    b.Navigation("Question");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Level", b =>
                {
                    b.Navigation("Question");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Position", b =>
                {
                    b.Navigation("Vacancy");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Question", b =>
                {
                    b.Navigation("SessionQuestion");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Session", b =>
                {
                    b.Navigation("SessionQuestion");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Structure", b =>
                {
                    b.Navigation("Question");

                    b.Navigation("Vacancy");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.StructureType", b =>
                {
                    b.Navigation("Structure");
                });

            modelBuilder.Entity("Interview.Domain.Entities.Models.Vacancy", b =>
                {
                    b.Navigation("Session");
                });
#pragma warning restore 612, 618
        }
    }
}
