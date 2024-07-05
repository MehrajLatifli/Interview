using Interview.Application.Mapper.DTO.CandidateDTO;

namespace Interview.Application.Services.Abstract
{
    public interface ICandidateService
    {
        #region Candidate service

        public Task CandidateCreate(CandidateDTOforCreate model);

        public Task<List<CandidateDTOforGetandGetAll>> GetCandidate();

        public Task<CandidateDTOforGetandGetAll> GetCandidateById(int id);

        public Task CandidateUpdate(CandidateDTOforUpdate model);

        public Task<CandidateDTOforGetandGetAll> DeleteCandidateById(int id);

        #endregion
    }


}
