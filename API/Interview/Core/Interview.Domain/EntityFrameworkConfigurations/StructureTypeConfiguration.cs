using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Microsoft.EntityFrameworkCore;
using Interview.Domain.Entities.Models;

namespace Interview.Domain.EntityFrameworkConfigurations
{
    public class StructureTypeConfiguration : IEntityTypeConfiguration<StructureType>
    {
        public void Configure(EntityTypeBuilder<StructureType> builder)
        {

            builder.HasKey(e => e.Id).HasName("PK__StructureType");

        }
    }
}
