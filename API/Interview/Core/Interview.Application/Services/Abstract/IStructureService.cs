using Interview.Application.Mapper.DTO.StructureDTO;
using System.Security.Claims;

namespace Interview.Application.Services.Abstract
{
    public interface IStructureService
    {
        #region Structure service

        public Task StructureCreate(StructureDTOforCreate model, ClaimsPrincipal claimsPrincipal);

        public Task<List<StructureDTOforGetandGetAll>> GetStructure(ClaimsPrincipal claimsPrincipal);

        public Task<StructureDTOforGetandGetAll> GetStructureById(int id, ClaimsPrincipal claimsPrincipal);

        public Task StructureUpdate(StructureDTOforUpdate model, ClaimsPrincipal claimsPrincipal);

        public Task<StructureDTOforGetandGetAll> DeleteStructureById(int id, ClaimsPrincipal claimsPrincipal);

        #endregion
    }


}
