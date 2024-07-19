using Interview.Application.Mapper.DTO.StructureTypeDTO;
using System.Security.Claims;

namespace Interview.Application.Services.Abstract
{
    public interface IStructureTypeService
    {
        #region StructureType service

        public Task StructureTypeCreate(StructureTypeDTOforCreate model, ClaimsPrincipal claimsPrincipal);

        public Task<List<StructureTypeDTOforGetandGetAll>> GetStructureType(ClaimsPrincipal claimsPrincipal);

        public Task<StructureTypeDTOforGetandGetAll> GetStructureTypeById(int id, ClaimsPrincipal claimsPrincipal);

        public Task StructureTypeUpdate(StructureTypeDTOforUpdate model , ClaimsPrincipal claimsPrincipal);

        public Task<StructureTypeDTOforGetandGetAll> DeleteStructureTypeById(int id, ClaimsPrincipal claimsPrincipal);

        #endregion
    }


}
