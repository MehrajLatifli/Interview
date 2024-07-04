using Interview.Application.Repositories.Custom;
using Interview.Domain.Entities.IdentityAuth;
using Interview.Persistence.Contexts.InterviewDbContext;
using Interview.Persistence.Repositories.Concrete;

namespace Interview.Persistence.Repositories.Custom
{
    public class RoleClaimReadRepository : ReadRepository<RoleClaim>, IRoleClaimReadRepository
    {
        public RoleClaimReadRepository(InterviewContext context) : base(context)
        {
        }
    }
}
