using Interview.API.API_Routes;
using Interview.Application.Mapper.DTO.CandidateDTO;
using Interview.Application.Services.Abstract;
using Interview.Domain.Entities.AuthModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;

namespace Interview.API.Controllers.Operations
{
    [Route("api/[controller]")]
    [ApiController]

    public class CandidateController : ControllerBase
    {



        private readonly ICandidateService  _candidateService;

        public CandidateController(ICandidateService candidateService)
        {
            _candidateService = candidateService;
        }



        #region Candidate


        [HttpGet(Routes.CandidateById)]
        public async Task<IActionResult> GetCandidateById(int id)
        {

            var data = await _candidateService.GetCandidateById(id);

            return Ok(data);

        }


        [HttpGet(Routes.Candidate)]
        public async Task<IActionResult> GetCandidate()
        {

            var data = await _candidateService.GetCandidate();


            return Ok(data);

        }


        [HttpPost(Routes.Candidate)]
        public async Task<IActionResult> CandidateCreate([FromBody] CandidateDTOforCreate model)
        {

            await _candidateService.CandidateCreate(model);

            return Ok(new Response { Status = "Success", Message = "The Candidate created successfully!" });

        }


        [HttpPut(Routes.Candidate)]

        public async Task<IActionResult> CandidateUpdate([FromBody] CandidateDTOforUpdate model)
        {


            await _candidateService.CandidateUpdate(model);


            return Ok(new Response { Status = "Success", Message = "The Candidate updated successfully!" });




        }


        [HttpDelete(Routes.CandidateById)]
        public async Task<IActionResult> CandidateDelete(int id)
        {

            await _candidateService.DeleteCandidateById(id);

            return Ok(new Response { Status = "Success", Message = "The Candidate deleted successfully!" });
        }


        #endregion
    }


}
