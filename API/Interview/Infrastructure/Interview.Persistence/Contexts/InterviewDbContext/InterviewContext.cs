
using System;
using System.Collections.Generic;
using Interview.Domain.Entities.IdentityAuth;
using Interview.Domain.Entities.Models;
using Interview.Persistence.ServiceExtensions;
using Microsoft.EntityFrameworkCore;

namespace Interview.Persistence.Contexts.InterviewDbContext;

public class InterviewContext : DbContext
{
    public InterviewContext()
    {
    }

    public InterviewContext(DbContextOptions<InterviewContext> options)
        : base(options)
    {
    }

    public virtual DbSet<Candidate> Candidate { get; set; }

    public virtual DbSet<CandidateDocument> CandidateDocument { get; set; }

    public virtual DbSet<Category> Category { get; set; }

    public virtual DbSet<Level> Level { get; set; }

    public virtual DbSet<Position> Position { get; set; }

    public virtual DbSet<Question> Question { get; set; }

    public virtual DbSet<Role> Role { get; set; }

    public virtual DbSet<RoleClaim> RoleClaim { get; set; }

    public virtual DbSet<Session> Session { get; set; }

    public virtual DbSet<SessionQuestion> SessionQuestion { get; set; }

    public virtual DbSet<Structure> Structure { get; set; }

    public virtual DbSet<StructureType> StructureType { get; set; }

    public virtual DbSet<User> User { get; set; }

    public virtual DbSet<UserClaim> UserClaim { get; set; }

    public virtual DbSet<UserRole> UserRole { get; set; }

    public virtual DbSet<Vacancy> Vacancy { get; set; }

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
    {
        if (!optionsBuilder.IsConfigured)
        {
            optionsBuilder.UseSqlServer(ServiceExtension.CustomDbConnectionString);
        }
    }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Candidate>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Candidat__3214EC07469F46D4");

            entity.HasOne(d => d.CandidateDocument).WithMany(p => p.Candidate)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_CandidateDocument_forCandidates");
        });

        modelBuilder.Entity<CandidateDocument>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Candidat__3214EC07412C1956");
        });

        modelBuilder.Entity<Category>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Category__3214EC072D3EC4B7");
        });

        modelBuilder.Entity<Level>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Level__3214EC0735A80859");
        });

        modelBuilder.Entity<Position>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Position__3214EC0730660A0E");
        });

        modelBuilder.Entity<Question>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Question__3214EC07C839599E");

            entity.HasOne(d => d.Category).WithMany(p => p.Question)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_CategoryId_forQuestion");

            entity.HasOne(d => d.Level).WithMany(p => p.Question)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_LevelId_forQuestion");

            entity.HasOne(d => d.Structure).WithMany(p => p.Question)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_StructureId_forQuestion");
        });

        modelBuilder.Entity<Role>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Role__3214EC07C59F717C");
        });

        modelBuilder.Entity<RoleClaim>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__RoleClai__3214EC0701854A8C");

            entity.HasOne(d => d.Role).WithMany(p => p.RoleClaim)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_RoleId_forRoleClaim");
        });

        modelBuilder.Entity<Session>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Session__3214EC07289FC67F");

            entity.Property(e => e.EndValue).HasDefaultValueSql("((0.0))");

            entity.HasOne(d => d.Candidate).WithMany(p => p.Session)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_CandidateId_forSession");

            entity.HasOne(d => d.User).WithMany(p => p.Session)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_UserId_forSession");

            entity.HasOne(d => d.Vacancy).WithMany(p => p.Session)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_VacancyId_forSession");
        });

        modelBuilder.Entity<SessionQuestion>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__SessionQ__3214EC07DABC968F");

            entity.Property(e => e.Value).HasDefaultValueSql("((0))");

            entity.HasOne(d => d.Question).WithMany(p => p.SessionQuestion)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_QuestionId_forSessionQuestion");

            entity.HasOne(d => d.Session).WithMany(p => p.SessionQuestion)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_SessionId_forSessionQuestion");
        });

        modelBuilder.Entity<Structure>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Structur__3214EC077D1F08DF");

            entity.HasOne(d => d.StructureType).WithMany(p => p.Structure)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_StructureType_forStructure");
        });

        modelBuilder.Entity<StructureType>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Structur__3214EC0720B22513");
        });

        modelBuilder.Entity<User>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__User__3214EC07FA4259EB");
        });

        modelBuilder.Entity<UserClaim>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__UserClai__3214EC076DCED94E");

            entity.HasOne(d => d.User).WithMany(p => p.UserClaim)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_UserId_forUserClaim");
        });

        modelBuilder.Entity<UserRole>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__UserRole__3214EC079524A0B8");

            entity.HasOne(d => d.Role).WithMany(p => p.UserRole)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_RoleId_forUserRole");

            entity.HasOne(d => d.User).WithMany(p => p.UserRole)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_UserId_forUserRole");
        });

        modelBuilder.Entity<Vacancy>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Vacancy__3214EC07C2961250");

            entity.HasOne(d => d.Position).WithMany(p => p.Vacancy)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_PositionId_forVacancy");

            entity.HasOne(d => d.Structure).WithMany(p => p.Vacancy)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_StructureId_forVacancy");
        });

    }


}