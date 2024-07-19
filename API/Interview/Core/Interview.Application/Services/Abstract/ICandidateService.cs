using Interview.Application.Mapper.DTO.CandidateDTO;
using System.Security.Claims;

namespace Interview.Application.Services.Abstract
{
    public interface ICandidateService
    {
        #region Candidate service

        public Task CandidateCreate(CandidateDTOforCreate model, ClaimsPrincipal claimsPrincipal);

        public Task<List<CandidateDTOforGetandGetAll>> GetCandidate(ClaimsPrincipal claimsPrincipal);

        public Task<CandidateDTOforGetandGetAll> GetCandidateById(int id, ClaimsPrincipal claimsPrincipal);

        public Task CandidateUpdate(CandidateDTOforUpdate model, ClaimsPrincipal claimsPrincipal);

        public Task<CandidateDTOforGetandGetAll> DeleteCandidateById(int id, ClaimsPrincipal claimsPrincipal);

        #endregion
    }


}
