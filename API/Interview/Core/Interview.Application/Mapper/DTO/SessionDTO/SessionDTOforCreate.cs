using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Interview.Application.Mapper.DTO.SessionDTO
{
    public class SessionDTOforCreate
    {
        //[Column(TypeName = "decimal(18, 4)")]
        //public decimal EndValue { get; set; }

        [Required(ErrorMessage = "StartDate is required")]
        public DateTime? StartDate { get; set; }

        //public DateTime? EndDate { get; set; }

        [Required(ErrorMessage = "VacancyId is required")]
        public int VacancyId { get; set; }

        [Required(ErrorMessage = "CandidateId is required")]
        public int CandidateId { get; set; }

    }
}
