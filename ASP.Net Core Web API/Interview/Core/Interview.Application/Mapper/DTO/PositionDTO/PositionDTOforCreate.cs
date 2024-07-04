using System.ComponentModel.DataAnnotations;

namespace Interview.Application.Mapper.DTO.PositionDTO
{
    public class PositionDTOforCreate
    {

        [Required(ErrorMessage = "Name is required")]
        public string Name { get; set; }

    }
}
