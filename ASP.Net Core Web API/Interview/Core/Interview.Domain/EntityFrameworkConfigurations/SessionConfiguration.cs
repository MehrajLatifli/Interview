using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Microsoft.EntityFrameworkCore;
using Interview.Domain.Entities.Models;

namespace Interview.Domain.EntityFrameworkConfigurations
{
    public class SessionConfiguration : IEntityTypeConfiguration<Session>
    {
        public void Configure(EntityTypeBuilder<Session> builder)
        {
            builder.HasKey(e => e.Id).HasName("PK__Session");

            builder.Property(e => e.EndValue).HasDefaultValueSql("((0.0))");

            builder.HasOne(d => d.Candidate).WithMany(p => p.Session)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_CandidateId_forSession");

            builder.HasOne(d => d.Vacancy).WithMany(p => p.Session)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_VacancyId_forSession");

        }
    }
}
