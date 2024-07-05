using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Microsoft.EntityFrameworkCore;
using Interview.Domain.Entities.Models;

namespace Interview.Domain.EntityFrameworkConfigurations
{
    public class QuestionConfiguration : IEntityTypeConfiguration<Question>
    {
        public void Configure(EntityTypeBuilder<Question> builder)
        {
            builder.HasKey(e => e.Id).HasName("PK__Question");

            builder.HasOne(d => d.Category).WithMany(p => p.Question)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_CategoryId_forQuestion");

            builder.HasOne(d => d.Level).WithMany(p => p.Question)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_LevelId_forQuestion");

            builder.HasOne(d => d.Structure).WithMany(p => p.Question)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_StructureId_forQuestion");

        }
    }
}
