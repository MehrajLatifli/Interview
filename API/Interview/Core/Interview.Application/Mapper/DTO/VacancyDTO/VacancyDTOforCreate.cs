using System.ComponentModel.DataAnnotations;

namespace Interview.Application.Mapper.DTO.VacancyDTO
{
    public class VacancyDTOforCreate
    {


        [Required(ErrorMessage = "Title is required")]
        public string Title { get; set; }

        [Required(ErrorMessage = "Description is required")]
        public string Description { get; set; }

        [Required(ErrorMessage = "StartDate is required")]
        public DateTime StartDate { get; set; }

        [Required(ErrorMessage = "EndDate is required")]
        public DateTime EndDate { get; set; }

        [Required(ErrorMessage = "PositionId is required")]
        public int PositionId { get; set; }

        [Required(ErrorMessage = "StructureId is required")]
        public int StructureId { get; set; }

    }
}
