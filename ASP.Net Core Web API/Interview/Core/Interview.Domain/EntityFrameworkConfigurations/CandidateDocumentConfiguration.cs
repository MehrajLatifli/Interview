using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Microsoft.EntityFrameworkCore;
using Interview.Domain.Entities.Models;

namespace Interview.Domain.EntityFrameworkConfigurations
{
    public class CandidateDocumentConfiguration : IEntityTypeConfiguration<CandidateDocument>
    {
        public void Configure(EntityTypeBuilder<CandidateDocument> builder)
        {

            builder.HasKey(e => e.Id).HasName("PK__CandidateDocument");

        }
    }
}
