using Interview.Application.Repositories.Custom;
using Interview.Domain.Entities.IdentityAuth;
using Interview.Persistence.Contexts.InterviewDbContext;
using Interview.Persistence.Repositories.Concrete;

namespace Interview.Persistence.Repositories.Custom
{
    public class UserReadRepository : ReadRepository<User>, IUserReadRepository
    {
        public UserReadRepository(InterviewContext context) : base(context)
        {
        }
    }
}
