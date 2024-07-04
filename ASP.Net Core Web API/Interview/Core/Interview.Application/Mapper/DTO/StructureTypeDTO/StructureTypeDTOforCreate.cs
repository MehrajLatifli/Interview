using System.ComponentModel.DataAnnotations;

namespace Interview.Application.Mapper.DTO.StructureTypeDTO
{
    public class StructureTypeDTOforCreate
    {



        [Required(ErrorMessage = "Name is required")]
        public string Name { get; set; }

    }
}
