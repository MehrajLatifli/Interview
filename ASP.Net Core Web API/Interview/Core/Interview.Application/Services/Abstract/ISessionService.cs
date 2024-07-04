using Interview.Application.Mapper.DTO.SessionDTO;
using System.Security.Claims;

namespace Interview.Application.Services.Abstract
{
    public interface ISessionService
    {
        #region Session service

        public Task SessionCreate(SessionDTOforCreate model, ClaimsPrincipal User);

        public Task<List<SessionDTOforGetandGetAll>> GetSession();

        public Task<SessionDTOforGetandGetAll> GetSessionById(int id);

        public Task SessionUpdate(SessionDTOforUpdate model);

        public Task<SessionDTOforGetandGetAll> DeleteSessionById(int id);



        #endregion
    }


}
