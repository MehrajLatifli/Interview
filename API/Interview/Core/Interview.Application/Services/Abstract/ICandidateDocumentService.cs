using Interview.Application.Mapper.DTO.CandidateDocumentDTO;

namespace Interview.Application.Services.Abstract
{
    public interface ICandidateDocumentService
    {
        #region CandidateDocument service

        public Task CandidateDocumentCreate(CandidateDocumentDTOforCreate model, string connection);

        public Task<List<CandidateDocumentDTOforGetandGetAll>> GetCandidateDocument();

        public Task<CandidateDocumentDTOforGetandGetAll> GetCandidateDocumentById(int id);

        public Task CandidateDocumentUpdate(CandidateDocumentDTOforUpdate model, string connection);

        public Task<CandidateDocumentDTOforGetandGetAll> DeleteCandidateDocumentById(int id);

        #endregion



    }

}
