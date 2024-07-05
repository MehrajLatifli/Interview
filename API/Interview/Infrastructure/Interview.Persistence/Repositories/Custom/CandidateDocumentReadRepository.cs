using Interview.Application.Repositories.Custom;
using Interview.Domain.Entities.Models;
using Interview.Persistence.Contexts.InterviewDbContext;
using Interview.Persistence.Repositories.Concrete;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Interview.Persistence.Repositories.Custom
{
    public class CandidateDocumentReadRepository : ReadRepository<CandidateDocument>, ICandidateDocumentReadRepository
    {
        public CandidateDocumentReadRepository(InterviewContext context) : base(context)
        {
        }
    }
}
