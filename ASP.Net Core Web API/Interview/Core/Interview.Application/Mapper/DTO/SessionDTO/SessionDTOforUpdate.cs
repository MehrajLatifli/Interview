using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Interview.Application.Mapper.DTO.SessionDTO
{
    public class SessionDTOforUpdate
    {
        [Required(ErrorMessage = "Id is required")]
        public int Id { get; set; }

        //[Column(TypeName = "decimal(18, 4)")]
        //public decimal EndValue { get; set; }

        //public DateTime? StartDate { get; set; }

        [Required(ErrorMessage = "EndDate is required")]
        public DateTime? EndDate { get; set; }

        //[Required(ErrorMessage = "VacancyId is required")]
        //public int VacancyId { get; set; }

        //[Required(ErrorMessage = "CandidateId is required")]
        //public int CandidateId { get; set; }

    }
}
