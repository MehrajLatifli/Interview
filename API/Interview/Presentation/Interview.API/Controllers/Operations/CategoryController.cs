using Asp.Versioning;
using Interview.API.API_Routes;
using Interview.Application.Mapper.DTO.CategoryDTO;
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

    public class CategoryController : ControllerBase
    {


        private readonly ICategoryService _categoryService;

        public CategoryController(ICategoryService categoryService)
        {
            _categoryService = categoryService;
        }




        #region Category


        [HttpGet(Routes.CategoryById)]
        public async Task<IActionResult> GetCategoryById(int id)
        {

            var data = await _categoryService.GetCategoryById(id, User);

            return Ok(data);

        }


        [HttpGet(Routes.Category)]
        public async Task<IActionResult> GetCategory()
        {

            var data = await _categoryService.GetCategory(User);


            return Ok(data);

        }


        [HttpPost(Routes.Category)]
        public async Task<IActionResult> CategoryCreate([FromBody] CategoryDTOforCreate model)
        {

            await _categoryService.CategoryCreate(model, User);

            return Ok(new Response { Status = "Success", Message = "The Category created successfully!" });

        }


        [HttpPut(Routes.Category)]
        public async Task<IActionResult> CategoryUpdate([FromBody] CategoryDTOforUpdate model)
        {


            await _categoryService.CategoryUpdate(model, User);


            return Ok(new Response { Status = "Success", Message = "The Category updated successfully!" });




        }


        [HttpDelete(Routes.CategoryById)]
        public async Task<IActionResult> DeleteCategoryById(int id)
        {

            await _categoryService.DeleteCategoryById(id, User);

            return Ok(new Response { Status = "Success", Message = "The Category deleted successfully!" });
        }


        #endregion
    }


}
