using Asp.Versioning;
using Interview.API.API_Routes;
using Interview.Application.Mapper.DTO.LevelDTO;
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
    public class LevelController : ControllerBase
    {



        private readonly ILevelService _levelService;

        public LevelController(ILevelService levelService)
        {
            _levelService = levelService;
        }





        #region Level


        [HttpGet(Routes.LevelById)]
        public async Task<IActionResult> GetLevelById(int id)
        {

            var data = await _levelService.GetLevelById(id, User);

            return Ok(data);

        }


        [HttpGet(Routes.Level)]
        public async Task<IActionResult> GetLevel()
        {

            var data = await _levelService.GetLevel(User);


            return Ok(data);

        }


        [HttpPost(Routes.Level)]

        public async Task<IActionResult> LevelCreate([FromBody] LevelDTOforCreate model)
        {

            await _levelService.LevelCreate(model, User);

            return Ok(new Response { Status = "Success", Message = "The Level created successfully!" });

        }


        [HttpPut(Routes.Level)]
        public async Task<IActionResult> LevelUpdate([FromBody] LevelDTOforUpdate model)
        {


            await _levelService.LevelUpdate(model, User);


            return Ok(new Response { Status = "Success", Message = "The Level updated successfully!" });




        }


        [HttpDelete(Routes.LevelById)]
        public async Task<IActionResult> LevelDelete(int id)
        {

            await _levelService.DeleteLevelById(id, User);

            return Ok(new Response { Status = "Success", Message = "The Level deleted successfully!" });
        }


        #endregion
    }


}
