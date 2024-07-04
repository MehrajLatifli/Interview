using Interview.Application.Repositories.Custom;
using Interview.Domain.Entities.IdentityAuth;
using Interview.Persistence.Contexts.InterviewDbContext;
using Interview.Persistence.Repositories.Concrete;

namespace Interview.Persistence.Repositories.Custom
{
    public class UserRoleWriteRepository : WriteRepository<UserRole>, IUserRoleWriteRepository
    {
        public UserRoleWriteRepository(InterviewContext context) : base(context)
        {
        }
    }
}
