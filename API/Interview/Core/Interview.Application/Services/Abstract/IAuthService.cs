using Interview.Application.Mapper.DTO;
using Interview.Application.Mapper.DTO.AuthDTO;
using Interview.Domain.Entities.AuthModels;
using Interview.Domain.Entities.Others;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace Interview.Application.Services.Abstract
{
    public interface IAuthService
    {

        //#region Auth service

        //public Task<List<RoleAccessTypeDTO>> GetRoleAccessType(ClaimsPrincipal User);

        public Task<List<RoleAccessMethodDTO>> GetMehtods(ClaimsPrincipal User);
        public Task<List<UserAccessDTO>> GetUserAccess(ClaimsPrincipal User);
        public Task<List<GetAuthDTOModel>> GetAdmins(ClaimsPrincipal User);
        public Task<List<GetAuthDTOModel>> GetHRs(ClaimsPrincipal User);
        public Task AddRole(string roleName, ClaimsPrincipal User);
        public Task AddRoleClaim(string roleId, string roleAccessMethodId, ClaimsPrincipal User);
        public Task AddUserClaim(string userId, string userAccessId, ClaimsPrincipal User);
        public Task AddUserRole(string userId, string roleId, ClaimsPrincipal User);
        public Task AddUser(RegisterDTO model, string ConnectionStringAzure);

        public Task RegisterAdmin(RegisterAdminDTO model, string ConnectionStringAzure);


        //public Task RegisterAdmin(RegisterDTO model, string ConnectionStringAzure);

        //public Task RegisterHR(RegisterDTO model, string ConnectionStringAzure);

        //public Task AddUserRole(int userId, int roleId, int roleAccessType, ClaimsPrincipal User);

        //public Task UpdateUserRole(int userId, int roleId, int roleAccessType, ClaimsPrincipal User);

        //public Task DeleteRole(int roleId, int userId, ClaimsPrincipal User);

        public Task<LoginResponse> Login(LoginDTO model);

        //public Task Logout();

        //public Task<TokenModel> RefreshToken(TokenModel model);

        //public Task Revoke(string username);

        //public Task RevokeAll();


        ////public Task<List<GetAuthModel>> GetAdmins(ClaimsPrincipal User, CustomUserClaimResult customUserClaimResult);

        ////public Task<List<GetAuthModel>> GetHR(ClaimsPrincipal User, CustomUserClaimResult customUserClaimResult);
        //public Task<List<GetRoleModel>> GetRoles(ClaimsPrincipal User);



        //public Task UpdateProfile(UpdateProfileDTO model, System.Security.Claims.ClaimsPrincipal claims, string ConnectionStringAzure);

        //public Task UpdatePassword(UpdatePasswordDTO model,ClaimsPrincipal User);


        //#endregion

    }
}
