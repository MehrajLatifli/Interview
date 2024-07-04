using Interview.Application.Repositories.Abstract;
using Interview.Domain.Entities.IdentityAuth;

namespace Interview.Application.Repositories.Custom
{
    public interface IRoleClaimWriteRepository : IWriteRepository<RoleClaim>
    {
    }
}
