using System.ComponentModel.DataAnnotations;

namespace Interview.Application.Mapper.DTO.SessionQuestionDTO
{
    public class SessionQuestionDTOforCreate
    {

        //public int Value { get; set; }

        [Required(ErrorMessage = "SessionId is required")]
        public int SessionId { get; set; }

        [Required(ErrorMessage = "QuestionId is required")]
        public int QuestionId { get; set; }

    }
}
