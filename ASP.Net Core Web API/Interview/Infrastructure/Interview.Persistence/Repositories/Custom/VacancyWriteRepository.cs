using Interview.Application.Repositories.Custom;
using Interview.Domain.Entities.Models;
using Interview.Persistence.Contexts.InterviewDbContext;
using Interview.Persistence.Repositories.Concrete;

namespace Interview.Persistence.Repositories.Custom
{
    public class VacancyWriteRepository : WriteRepository<Vacancy>, IVacancyWriteRepository
    {
        public VacancyWriteRepository(InterviewContext context) : base(context)
        {
        }
    }
}
