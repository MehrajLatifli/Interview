using System.ComponentModel.DataAnnotations;

namespace Interview.Application.Mapper.DTO.UserClaimDTO
{
    public class UserClaimDTOforUpdate
    {
        [Required(ErrorMessage = "Id  is required")]
        public int Id { get; set; }

        [Required(ErrorMessage = "ClaimType  is required")]
        public string ClaimType { get; set; }

        [Required(ErrorMessage = "ClaimValue  is required")]
        public string ClaimValue { get; set; }

        [Required(ErrorMessage = "UserId  is required")]
        public int UserId { get; set; }

    }
}
