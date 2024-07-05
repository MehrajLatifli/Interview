using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Interview.Application.Mapper.DTO.CandidateDTO
{
    public class CandidateDTOforCreate
    {

        [Required(ErrorMessage = "CandidateDocumentId is required")]

        public int CandidateDocumentId { get; set; }
    }
}
