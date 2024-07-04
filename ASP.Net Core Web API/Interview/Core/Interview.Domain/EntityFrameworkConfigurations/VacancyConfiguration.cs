using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Microsoft.EntityFrameworkCore;
using Interview.Domain.Entities.Models;
using System.Reflection.Emit;

namespace Interview.Domain.EntityFrameworkConfigurations
{
    public class VacancyConfiguration : IEntityTypeConfiguration<Vacancy>
    {
        public void Configure(EntityTypeBuilder<Vacancy> builder)
        {
            builder.HasKey(e => e.Id).HasName("PK__Vacancy");

            builder.HasOne(d => d.Position).WithMany(p => p.Vacancy)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_PositionId_forVacancy");

            builder.HasOne(d => d.Structure).WithMany(p => p.Vacancy)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_StructureId_forVacancy");

        }
    }
}
