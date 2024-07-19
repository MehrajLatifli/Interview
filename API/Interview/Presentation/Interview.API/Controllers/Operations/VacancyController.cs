using Asp.Versioning;
using Interview.API.API_Routes;
using Interview.Application.Mapper.DTO.VacancyDTO;
using Interview.Application.Services.Abstract;
using Interview.Domain.Entities.AuthModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;

namespace Interview.API.Controllers.Operations
{
    [ApiVersion(1, Deprecated = true)]
    [Route("api/v{version:apiVersion}/[controller]")]
    [ApiController]
    [Authorize]

    public class VacancyController : ControllerBase
    {


        private readonly IVacancyService _vacancyService;

        public VacancyController(IVacancyService vacancyService)
        {
            _vacancyService = vacancyService;
        }



        #region Vacancy


        [HttpGet(Routes.VacancyById)]
        public async Task<IActionResult> GetVacancyById(int id)
        {

            var data = await _vacancyService.GetVacancyById(id, User);

            return Ok(data);

        }


        [HttpGet(Routes.Vacancy)]
        public async Task<IActionResult> GetVacancy()
        {

            var data = await _vacancyService.GetVacancy(User);


            return Ok(data);

        }


        [HttpPost(Routes.Vacancy)]
        public async Task<IActionResult> VacancyCreate([FromBody] VacancyDTOforCreate model)
        {

            await _vacancyService.VacancyCreate(model, User);

            return Ok(new Response { Status = "Success", Message = "The Vacancy created successfully!" });

        }


        [HttpPut(Routes.Vacancy)]
        public async Task<IActionResult> VacancyUpdate([FromBody] VacancyDTOforUpdate model)
        {


            await _vacancyService.VacancyUpdate(model, User);


            return Ok(new Response { Status = "Success", Message = "The Vacancy updated successfully!" });




        }


        [HttpDelete(Routes.VacancyById)]
        public async Task<IActionResult> VacancyDelete(int id)
        {

            await _vacancyService.DeleteVacancyById(id, User);

            return Ok(new Response { Status = "Success", Message = "The Vacancy deleted successfully!" });
        }


        #endregion
    }


}
