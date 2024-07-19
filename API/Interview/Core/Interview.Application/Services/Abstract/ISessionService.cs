using Interview.Application.Mapper.DTO.SessionDTO;
using System.Security.Claims;

namespace Interview.Application.Services.Abstract
{
    public interface ISessionService
    {
        #region Session service

        public Task SessionCreate(SessionDTOforCreate model, ClaimsPrincipal User);

        public Task<List<SessionDTOforGetandGetAll>> GetSession(ClaimsPrincipal claimsPrincipal);

        public Task<SessionDTOforGetandGetAll> GetSessionById(int id, ClaimsPrincipal claimsPrincipal);

        public Task SessionUpdate(SessionDTOforUpdate model, ClaimsPrincipal claimsPrincipal);

        public Task<SessionDTOforGetandGetAll> DeleteSessionById(int id, ClaimsPrincipal claimsPrincipal);



        #endregion
    }


}
