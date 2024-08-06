using Asp.Versioning;
using Interview.API.API_Routes;
using Interview.Application.Mapper.DTO.SessionQuestionDTO;
using Interview.Application.Services.Abstract;
using Interview.Domain.Entities.AuthModels;
using Interview.Domain.Entities.Models;
using Interview.Domain.Entities.Requests;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;

namespace Interview.API.Controllers.Operations
{
    [ApiVersion(1, Deprecated = true)]
    [Route("api/v{version:apiVersion}/[controller]")]
    [ApiController]
    [Authorize]

    public class SessionQuestionController : ControllerBase
    {


        private readonly ISessionQuestionService _sessionQuestionService;

        public SessionQuestionController(ISessionQuestionService sessionQuestionService)
        {
            _sessionQuestionService = sessionQuestionService;
        }



        #region SessionQuestion


        [HttpGet]
        [Route(Routes.SessionQuestionById, Name = "GetSessionQuestionById")]
        public async Task<IActionResult> GetSessionQuestionById(int id)
        {

            var data = await _sessionQuestionService.GetSessionQuestionById(id, User);

            return Ok(data);

        }


        [HttpGet]
        [Route(Routes.SessionQuestion, Name = "sessionQuestion")]
        public async Task<IActionResult> GetSessionQuestion()
        {

            var data = await _sessionQuestionService.GetSessionQuestion(User);


            return Ok(data);

        }


        [HttpGet]
        [Route(Routes.GetSessionQuestionBySessionId, Name = "getSessionQuestionBySessionId")]
        public async Task<IActionResult> GetSessionQuestionBySessionId(int sessionId)
        {

            var data = await _sessionQuestionService.GetSessionQuestionBySessionId(sessionId, User);

            return Ok(data);

        }


        [HttpPost(Routes.SessionQuestion)]
        public async Task<IActionResult> SessionQuestionCreate([FromBody] SessionQuestionDTOforCreate model)
        {

            await _sessionQuestionService.SessionQuestionCreate(model, User);

            return Ok(new Response { Status = "Success", Message = "The SessionQuestion created successfully!" });

        }


        [HttpPut(Routes.SessionQuestion)]
        public async Task<IActionResult> SessionQuestionUpdate([FromBody] SessionQuestionDTOforUpdate model)
        {


            await _sessionQuestionService.SessionQuestionUpdate(model, User);


            return Ok(new Response { Status = "Success", Message = "The SessionQuestion updated successfully!" });




        }


        [HttpDelete(Routes.SessionQuestionById)]
        public async Task<IActionResult> SessionQuestionDelete(int id)
        {

            await _sessionQuestionService.DeleteSessionQuestionById(id, User);

            return Ok(new Response { Status = "Success", Message = "The SessionQuestion deleted successfully!" });
        }


        [HttpPost(Routes.RandomQuestionById)]
        public async Task<IActionResult> GetRandomQuestion([FromQuery] RandomQuestionRequestModel request)
        {

            var data = await _sessionQuestionService.GetRandomQuestion(request, User);


            return Ok(data);

        }

        [HttpPost(Routes.CreateRandomQuestion)]
        public async Task<IActionResult> CreateRandomQuestion([FromBody] RandomQuestionRequestModel2 request)
        {

           await _sessionQuestionService.CreateRandomQuestion(request, User);


            return Ok(new Response { Status = "Success", Message = "The RandomQuestion created successfully!" });

        }


        [HttpGet(Routes.GetAllQuestionByPage)]
        public async Task<IActionResult> GetAllQuestionByVacancyId([FromQuery] QuestionByPageRequestModel model)
        {

            var data = await _sessionQuestionService.GetAllQuestionByPage(model , User);


            return Ok(data);

        }

        #endregion
    }


}
