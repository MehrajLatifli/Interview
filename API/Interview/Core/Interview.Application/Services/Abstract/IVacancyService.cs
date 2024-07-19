


using Interview.Application.Mapper.DTO.VacancyDTO;
using Interview.Domain.Entities.AuthModels;
using System.Security.Claims;

namespace Interview.Application.Services.Abstract
{
    public interface IVacancyService
    {

        #region Vacancy service

        public Task VacancyCreate(VacancyDTOforCreate model, ClaimsPrincipal claimsPrincipal);

        public Task<List<VacancyDTOforGetandGetAll>> GetVacancy(ClaimsPrincipal claimsPrincipal);

        public Task<VacancyDTOforGetandGetAll> GetVacancyById(int id, ClaimsPrincipal claimsPrincipal);

        public Task VacancyUpdate(VacancyDTOforUpdate model, ClaimsPrincipal claimsPrincipal);

        public Task<VacancyDTOforGetandGetAll> DeleteVacancyById(int id, ClaimsPrincipal claimsPrincipal);

        #endregion
    }


}
