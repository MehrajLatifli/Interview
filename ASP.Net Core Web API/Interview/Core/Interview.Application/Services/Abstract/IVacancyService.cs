


using Interview.Application.Mapper.DTO.VacancyDTO;
using Interview.Domain.Entities.AuthModels;

namespace Interview.Application.Services.Abstract
{
    public interface IVacancyService
    {

        #region Vacancy service

        public Task VacancyCreate(VacancyDTOforCreate model);

        public Task<List<VacancyDTOforGetandGetAll>> GetVacancy();

        public Task<VacancyDTOforGetandGetAll> GetVacancyById(int id);

        public Task VacancyUpdate(VacancyDTOforUpdate model);

        public Task<VacancyDTOforGetandGetAll> DeleteVacancyById(int id);

        #endregion
    }


}
