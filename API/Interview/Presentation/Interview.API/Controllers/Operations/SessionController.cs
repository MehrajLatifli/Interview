using Asp.Versioning;
using Interview.API.API_Routes;
using Interview.Application.Mapper.DTO.SessionDTO;
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

    public class SessionController : ControllerBase
    {


        private readonly ISessionService _sessionService;

        public SessionController(ISessionService sessionService)
        {
            _sessionService = sessionService;
        }




        #region Session


        [HttpGet(Routes.SessionById)]
        public async Task<IActionResult> GetSessionById(int id)
        {

            var data = await _sessionService.GetSessionById(id, User);

            return Ok(data);

        }


        [HttpGet(Routes.Session)]
        public async Task<IActionResult> GetSession()
        {

            var data = await _sessionService.GetSession(User);


            return Ok(data);

        }


        [HttpPost(Routes.Session)]
        public async Task<IActionResult> SessionCreate([FromBody] SessionDTOforCreate model)
        {

            await _sessionService.SessionCreate(model,User);

            return Ok(new Response { Status = "Success", Message = "The Session created successfully!" });

        }


        [HttpPut(Routes.Session)]
        public async Task<IActionResult> SessionUpdate([FromBody] SessionDTOforUpdate model)
        {


            await _sessionService.SessionUpdate(model, User);


            return Ok(new Response { Status = "Success", Message = "The Session updated successfully!" });




        }


        [HttpDelete(Routes.SessionById)]
        public async Task<IActionResult> SessionDelete(int id)
        {

            await _sessionService.DeleteSessionById(id, User);

            return Ok(new Response { Status = "Success", Message = "The Session deleted successfully!" });
        }


        #endregion
    }


}
