using Interview.Application.Repositories.Custom;
using Interview.Domain.Entities.IdentityAuth;
using Interview.Persistence.Contexts.InterviewDbContext;
using Interview.Persistence.Repositories.Concrete;

namespace Interview.Persistence.Repositories.Custom
{
    public class RoleClaimWriteRepository : WriteRepository<RoleClaim>, IRoleClaimWriteRepository
    {
        public RoleClaimWriteRepository(InterviewContext context) : base(context)
        {
        }
    }
}
