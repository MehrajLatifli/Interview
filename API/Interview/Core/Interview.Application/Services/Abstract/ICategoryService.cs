using Interview.Application.Mapper.DTO.CategoryDTO;

namespace Interview.Application.Services.Abstract
{
    public interface ICategoryService
    {
        #region Category service

        public Task CategoryCreate(CategoryDTOforCreate model);

        public Task<List<CategoryDTOforGetandGetAll>> GetCategory();

        public Task<CategoryDTOforGetandGetAll> GetCategoryById(int id);

        public Task CategoryUpdate(CategoryDTOforUpdate model);

        public Task<CategoryDTOforGetandGetAll> DeleteCategoryById(int id);

        #endregion
    }


}
