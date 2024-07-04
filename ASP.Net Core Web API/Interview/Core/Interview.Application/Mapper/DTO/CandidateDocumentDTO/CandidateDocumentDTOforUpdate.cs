using Interview.Application.Validations;
using Microsoft.AspNetCore.Http;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Interview.Application.Mapper.DTO.CandidateDocumentDTO
{
    public class CandidateDocumentDTOforUpdate
    {
        [Required(ErrorMessage = "Id is required")]
        public int Id { get; set; }

        public string Surname { get; set; }

        [Required(ErrorMessage = "Candidate name is required")]
        public string Name { get; set; }

        public string Patronymic { get; set; }

        public string Phonenumber { get; set; }

        [Required(ErrorMessage = "Candidate email is required")]
        [Email(ErrorMessage = "Invalid email format.")]
        public string Email { get; set; }

        [Required(ErrorMessage = "CV is required")]
        [AllowedExtensions(new string[] { ".pdf", ".doc", ".epub", ".docx" })]
        [FileSize(5, 10)]
        [Column("CV")]
        public IFormFile Cv { get; set; }

        public string Address { get; set; }
    }
}
