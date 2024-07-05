using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Microsoft.EntityFrameworkCore;
using Interview.Domain.Entities.Models;

namespace Interview.Domain.EntityFrameworkConfigurations
{
    public class CategoryConfiguration : IEntityTypeConfiguration<Category>
    {
        public void Configure(EntityTypeBuilder<Category> builder)
        {

            builder.HasKey(e => e.Id).HasName("PK__Category");

        }
    }
}
