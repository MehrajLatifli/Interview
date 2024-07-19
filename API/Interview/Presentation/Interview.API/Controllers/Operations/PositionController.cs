using Asp.Versioning;
using Interview.API.API_Routes;
using Interview.Application.Mapper.DTO.PositionDTO;
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

    public class PositionController : ControllerBase
    {

        private readonly IPositionService _positionService;

        public PositionController(IPositionService positionService)
        {
            _positionService = positionService;
        }


        #region Position

        [HttpGet(Routes.PositionById)]
        public async Task<IActionResult> GetPositionById(int id)
        {

            var data = await _positionService.GetPositionById(id, User);

            return Ok(data);

        }


        [HttpGet(Routes.Position)]
        public async Task<IActionResult> GetPosition()
        {

            var data = await _positionService.GetPosition(User);


            return Ok(data);

        }


        [HttpPost(Routes.Position)]
        public async Task<IActionResult> PositionCreate([FromBody] PositionDTOforCreate model)
        {

            await _positionService.PositionCreate(model, User);

            return Ok(new Response { Status = "Success", Message = "The Position created successfully!" });

        }


        [HttpPut(Routes.Position)]
        public async Task<IActionResult> PositionUpdate([FromBody] PositionDTOforUpdate model)
        {


            await _positionService.PositionUpdate(model, User);


            return Ok(new Response { Status = "Success", Message = "The Position updated successfully!" });




        }


        [HttpDelete(Routes.PositionById)]
        public async Task<IActionResult> PositionDelete(int id)
        {

            await _positionService.DeletePositionById(id, User);

            return Ok(new Response { Status = "Success", Message = "The Position deleted successfully!" });
        }


        #endregion        
    }


}
