﻿using Asp.Versioning;
using Interview.API.API_Routes;
using Interview.Application.Mapper.DTO.StructureTypeDTO;
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

    public class StructureTypeController : ControllerBase
    {



        private readonly IStructureTypeService _structureTypeService;

        public StructureTypeController(IStructureTypeService structureTypeService)
        {
            _structureTypeService = structureTypeService;
        }


        #region StructureType


        [HttpGet(Routes.StructureTypeById)]
        public async Task<IActionResult> GetStructureTypeById(int id)
        {

            var data = await _structureTypeService.GetStructureTypeById(id, User);

            return Ok(data);

        }


        [HttpGet(Routes.StructureType)]
        public async Task<IActionResult> GetStructureType()
        {

            var data = await _structureTypeService.GetStructureType(User);


            return Ok(data);

        }


        [HttpPost]
        [Route(Routes.StructureType)]
        public async Task<IActionResult> StructureTypeCreate([FromBody] StructureTypeDTOforCreate model)
        {

            await _structureTypeService.StructureTypeCreate(model, User);

            return Ok(new Response { Status = "Success", Message = "The StructureType created successfully!" });

        }


        [HttpPut]
        [Route(Routes.StructureType)]
        public async Task<IActionResult> StructureTypeUpdate([FromBody] StructureTypeDTOforUpdate model)
        {


            await _structureTypeService.StructureTypeUpdate(model, User);


            return Ok(new Response { Status = "Success", Message = "The StructureType updated successfully!" });




        }


        [HttpDelete(Routes.StructureTypeById)]
        public async Task<IActionResult> StructureTypeDelete(int id)
        {

            await _structureTypeService.DeleteStructureTypeById(id, User);

            return Ok(new Response { Status = "Success", Message = "The StructureType deleted successfully!" });
        }


        #endregion
    }


}
