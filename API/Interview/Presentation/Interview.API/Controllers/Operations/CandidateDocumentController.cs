using Asp.Versioning;
using Interview.API.API_Routes;
using Interview.Application.Mapper.DTO.CandidateDocumentDTO;
using Interview.Application.Services.Abstract;
using Interview.Domain.Entities.AuthModels;
using Interview.Persistence.ServiceExtensions;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;

namespace Interview.API.Controllers.Operations
{
    [ApiVersion(1, Deprecated = true)]
    [Route("api/v{version:apiVersion}/[controller]")]
    [ApiController]
    [Authorize]

    public class CandidateDocumentController : ControllerBase
    {


        private readonly ICandidateDocumentService _candidateDocumentService;

        public CandidateDocumentController(ICandidateDocumentService candidateDocumentService)
        {
            _candidateDocumentService = candidateDocumentService;
        }



        #region CandidateDocument


        [HttpGet(Routes.CandidateDocumentById)]
        public async Task<IActionResult> GetCandidateDocumentById(int id)
        {

            var data = await _candidateDocumentService.GetCandidateDocumentById(id,User);

            return Ok(data);

        }


        [HttpGet(Routes.CandidateDocument)]
        public async Task<IActionResult> GetCandidateDocument()
        {

            var data = await _candidateDocumentService.GetCandidateDocument(User);


            return Ok(data);

        }


        [HttpPost]
        [Route(Routes.CandidateDocument)]
        public async Task<IActionResult> CandidateDocumentCreate([FromForm] CandidateDocumentDTOforCreate model)
        {

            await _candidateDocumentService.CandidateDocumentCreate(model, ServiceExtension.ConnectionStringAzure, User);

            return Ok(new Response { Status = "Success", Message = "The CandidateDocument created successfully!" });

        }


        [HttpPut]
        [Route(Routes.CandidateDocument)]
        public async Task<IActionResult> CandidateDocumentUpdate([FromForm] CandidateDocumentDTOforUpdate model)
        {


            await _candidateDocumentService.CandidateDocumentUpdate(model, ServiceExtension.ConnectionStringAzure, User);


            return Ok(new Response { Status = "Success", Message = "The CandidateDocument updated successfully!" });




        }


        [HttpDelete(Routes.CandidateDocumentById)]
        public async Task<IActionResult> CandidateDocumentDelete(int id)
        {

            await _candidateDocumentService.DeleteCandidateDocumentById(id, User);

            return Ok(new Response { Status = "Success", Message = "The CandidateDocument deleted successfully!" });
        }


        #endregion
    }


}
