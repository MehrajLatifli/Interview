using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Microsoft.EntityFrameworkCore;
using Interview.Domain.Entities.Models;

namespace Interview.Domain.EntityFrameworkConfigurations
{
    public class StructureConfiguration : IEntityTypeConfiguration<Structure>
    {
        public void Configure(EntityTypeBuilder<Structure> builder)
        {
            builder.HasKey(e => e.Id).HasName("PK__Structure");

            builder.HasOne(d => d.StructureType).WithMany(p => p.Structure)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_StructureType_forStructure");

        }
    }
}
