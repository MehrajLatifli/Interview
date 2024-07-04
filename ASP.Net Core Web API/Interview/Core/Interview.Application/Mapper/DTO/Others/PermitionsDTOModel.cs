using Interview.Application.Mapper.DTO.RoleClaimDTO;
using Interview.Application.Mapper.DTO.RoleDTO;
using Interview.Application.Mapper.DTO.UserClaimDTO;
using Interview.Domain.Entities.IdentityAuth;

namespace Interview.Domain.Entities.Others
{

    public class PermitionsDTOModel
    {

        public List<UserClaimDTOforGetandGetAll> UserClaims { get; set; }

        public List<RoleClaimDTOforGetandGetAll>  RoleClaims { get; set; }

        public List<RoleDTOforGetandGetAll>  Roles { get; set; }




    }



}
