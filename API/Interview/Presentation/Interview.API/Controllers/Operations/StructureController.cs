using Asp.Versioning;
using Interview.API.API_Routes;
using Interview.Application.Mapper.DTO.StructureDTO;
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
    public class StructureController : ControllerBase
    {


        private readonly IStructureService _structureService;

        public StructureController(IStructureService structureService)
        {
            _structureService = structureService;
        }

        #region Structure


        [HttpGet(Routes.StructureById)]
        public async Task<IActionResult> GetStructureById(int id)
        {

            var data = await _structureService.GetStructureById(id, User);

            return Ok(data);

        }


        [HttpGet(Routes.Structure)]
        public async Task<IActionResult> GetStructure()
        {

            var data = await _structureService.GetStructure(User);


            return Ok(data);

        }


        [HttpPost(Routes.Structure)]
        public async Task<IActionResult> StructureCreate([FromBody] StructureDTOforCreate model)
        {

            await _structureService.StructureCreate(model,User);

            return Ok(new Response { Status = "Success", Message = "The Structure created successfully!" });

        }


        [HttpPut(Routes.Structure)]
        public async Task<IActionResult> StructureUpdate([FromBody] StructureDTOforUpdate model)
        {


            await _structureService.StructureUpdate(model, User);


            return Ok(new Response { Status = "Success", Message = "The Structure updated successfully!" });




        }


        [HttpDelete(Routes.StructureById)]
        public async Task<IActionResult> StructureDelete(int id)
        {

            await _structureService.DeleteStructureById(id, User);

            return Ok(new Response { Status = "Success", Message = "The Structure deleted successfully!" });
        }


        #endregion
    }


}
