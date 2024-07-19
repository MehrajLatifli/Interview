using Interview.Application.Mapper.DTO.CandidateDocumentDTO;
using System.Security.Claims;

namespace Interview.Application.Services.Abstract
{
    public interface ICandidateDocumentService
    {
        #region CandidateDocument service

        public Task CandidateDocumentCreate(CandidateDocumentDTOforCreate model, string connection, ClaimsPrincipal claimsPrincipal);

        public Task<List<CandidateDocumentDTOforGetandGetAll>> GetCandidateDocument(ClaimsPrincipal claimsPrincipal);

        public Task<CandidateDocumentDTOforGetandGetAll> GetCandidateDocumentById(int id, ClaimsPrincipal claimsPrincipal);

        public Task CandidateDocumentUpdate(CandidateDocumentDTOforUpdate model, string connection, ClaimsPrincipal claimsPrincipal);

        public Task<CandidateDocumentDTOforGetandGetAll> DeleteCandidateDocumentById(int id, ClaimsPrincipal claimsPrincipal);

        #endregion



    }

}
