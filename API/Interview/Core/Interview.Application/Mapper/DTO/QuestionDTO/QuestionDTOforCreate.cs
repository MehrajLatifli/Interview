using System.ComponentModel.DataAnnotations;

namespace Interview.Application.Mapper.DTO.QuestionDTO
{
    public class QuestionDTOforCreate
    {

        [Required(ErrorMessage = "Text is required")]
        public string Text { get; set; }

        [Required(ErrorMessage = "LevelId is required")]
        public int LevelId { get; set; }

        [Required(ErrorMessage = "CategoryId is required")]
        public int CategoryId { get; set; }

        [Required(ErrorMessage = "StructureId is required")]
        public int StructureId { get; set; }

    }
}
