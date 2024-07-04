using System.ComponentModel.DataAnnotations;
using Interview.Application.Validations;
using RangeAttribute = Interview.Application.Validations.RangeAttribute;

namespace Interview.Application.Mapper.DTO.LevelDTO
{
    public class LevelDTOforUpdate
    {
        [Required(ErrorMessage = "Id  is required")]
        public int Id { get; set; }

        [Required(ErrorMessage = "Name is required")]
        public string Name { get; set; }

        [Required(ErrorMessage = "Coefficient is required")]
        [Range(0.0, 1.0)]
        public decimal Coefficient { get; set; }

    }
}
