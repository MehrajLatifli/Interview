using Asp.Versioning;
using Interview.API.API_Routes;
using Interview.Application.Mapper.DTO.QuestionDTO;
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

    public class QuestionController : ControllerBase
    {


        private readonly IQuestionService _questionService;

        public QuestionController(IQuestionService questionService)
        {
            _questionService = questionService;
        }





        #region Question


        [HttpGet(Routes.QuestionById)]
        public async Task<IActionResult> GetQuestionById(int id)
        {

            var data = await _questionService.GetQuestionById(id, User);

            return Ok(data);

        }


        [HttpGet(Routes.Question)]
        public async Task<IActionResult> GetQuestion()
        {

            var data = await _questionService.GetQuestion(User);


            return Ok(data);

        }



        [HttpPost(Routes.Question)]
        public async Task<IActionResult> QuestionCreate([FromBody] QuestionDTOforCreate model)
        {

            await _questionService.QuestionCreate(model, User);

            return Ok(new Response { Status = "Success", Message = "The Question created successfully!" });

        }


        [HttpPut(Routes.Question)]
        public async Task<IActionResult> QuestionUpdate([FromBody] QuestionDTOforUpdate model)
        {


            await _questionService.QuestionUpdate(model, User);


            return Ok(new Response { Status = "Success", Message = "The Question updated successfully!" });




        }


        [HttpDelete(Routes.QuestionById)]
        public async Task<IActionResult> QuestionDelete(int id)
        {

            await _questionService.DeleteQuestionById(id, User);

            return Ok(new Response { Status = "Success", Message = "The Question deleted successfully!" });
        }


        #endregion        
    }


}
