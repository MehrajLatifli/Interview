using System.ComponentModel.DataAnnotations;

namespace Interview.Application.Mapper.DTO.StructureDTO
{
    public class StructureDTOforCreate
    {



        [Required(ErrorMessage = "Name is required")]
        public string Name { get; set; }

        [Required(ErrorMessage = "ParentId is required")]
        public string ParentId { get; set; }

        [Required(ErrorMessage = "StructureTypeId is required")]
        public int StructureTypeId { get; set; }

    }
}
