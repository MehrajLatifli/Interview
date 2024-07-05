using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Microsoft.EntityFrameworkCore;
using Interview.Domain.Entities.Models;

namespace Interview.Domain.EntityFrameworkConfigurations
{ 
    public class SessionQuestionConfiguration : IEntityTypeConfiguration<SessionQuestion>
    {
        public void Configure(EntityTypeBuilder<SessionQuestion> builder)
        {

            builder.HasKey(e => e.Id).HasName("PK__SessionQuestion");

            builder.Property(e => e.Value).HasDefaultValueSql("((0))");

            builder.HasOne(d => d.Question).WithMany(p => p.SessionQuestion)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_QuestionId_forSessionQuestion");

            builder.HasOne(d => d.Session).WithMany(p => p.SessionQuestion)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_SessionId_forSessionQuestion");

        }
    }
}
