using Interview.Application.Mapper.DTO.LevelDTO;
using System.Security.Claims;

namespace Interview.Application.Services.Abstract
{
    public interface ILevelService
    {
        #region Level service

        public Task LevelCreate(LevelDTOforCreate model, ClaimsPrincipal claimsPrincipal);

        public Task<List<LevelDTOforGetandGetAll>> GetLevel(ClaimsPrincipal claimsPrincipal);

        public Task<LevelDTOforGetandGetAll> GetLevelById(int id, ClaimsPrincipal claimsPrincipal);

        public Task LevelUpdate(LevelDTOforUpdate model, ClaimsPrincipal claimsPrincipal);

        public Task<LevelDTOforGetandGetAll> DeleteLevelById(int id, ClaimsPrincipal claimsPrincipal);

        #endregion
    }


}
