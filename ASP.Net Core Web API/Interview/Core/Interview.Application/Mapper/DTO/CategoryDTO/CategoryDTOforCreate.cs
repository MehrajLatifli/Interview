using System.ComponentModel.DataAnnotations;

namespace Interview.Application.Mapper.DTO.CategoryDTO
{
    public class CategoryDTOforCreate
    {


        [Required(ErrorMessage = "Name is required")]
        public string Name { get; set; }

    }
}
