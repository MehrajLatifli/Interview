using Interview.Application.Repositories.Abstract;
using Interview.Domain.Entities.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Interview.Application.Repositories.Custom
{
    public interface ICandidateDocumentReadRepository : IReadRepository<CandidateDocument>
    {
    }
}
