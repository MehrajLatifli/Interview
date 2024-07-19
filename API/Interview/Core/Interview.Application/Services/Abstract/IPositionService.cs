using Interview.Application.Mapper.DTO.PositionDTO;
using System.Security.Claims;

namespace Interview.Application.Services.Abstract
{
    public interface IPositionService
    {


        #region Position service

        public Task PositionCreate(PositionDTOforCreate model, ClaimsPrincipal claimsPrincipal);

        public Task<List<PositionDTOforGetandGetAll>> GetPosition(ClaimsPrincipal claimsPrincipal);

        public Task<PositionDTOforGetandGetAll> GetPositionById(int id, ClaimsPrincipal claimsPrincipal);

        public Task PositionUpdate(PositionDTOforUpdate model, ClaimsPrincipal claimsPrincipal);

        public Task<PositionDTOforGetandGetAll> DeletePositionById(int id , ClaimsPrincipal claimsPrincipal);

        #endregion

    }

}
