using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Microsoft.EntityFrameworkCore;
using Interview.Domain.Entities.Models;

namespace Interview.Domain.EntityFrameworkConfigurations
{
    public class PositionConfiguration : IEntityTypeConfiguration<Position>
    {
        public void Configure(EntityTypeBuilder<Position> builder)
        {

            builder.HasKey(e => e.Id).HasName("PK__Position");

        }
    }
}
