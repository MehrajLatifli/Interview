using Interview.Application.Mapper.DTO.CategoryDTO;
using System.Security.Claims;

namespace Interview.Application.Services.Abstract
{
    public interface ICategoryService
    {
        #region Category service

        public Task CategoryCreate(CategoryDTOforCreate model, ClaimsPrincipal claimsPrincipal);

        public Task<List<CategoryDTOforGetandGetAll>> GetCategory(ClaimsPrincipal claimsPrincipal);

        public Task<CategoryDTOforGetandGetAll> GetCategoryById(int id, ClaimsPrincipal claimsPrincipal);

        public Task CategoryUpdate(CategoryDTOforUpdate model, ClaimsPrincipal claimsPrincipal);

        public Task<CategoryDTOforGetandGetAll> DeleteCategoryById(int id, ClaimsPrincipal claimsPrincipal);

        #endregion
    }


}
