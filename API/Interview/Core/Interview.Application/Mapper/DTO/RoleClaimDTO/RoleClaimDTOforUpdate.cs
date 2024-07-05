using System.ComponentModel.DataAnnotations;

namespace Interview.Application.Mapper.DTO.RoleClaimDTO
{
    public class RoleClaimDTOforUpdate
    {
        [Required(ErrorMessage = "Id  is required")]
        public int Id { get; set; }

        [Required(ErrorMessage = "ClaimType  is required")]
        public string ClaimType { get; set; }

        [Required(ErrorMessage = "ClaimValue  is required")]
        public string ClaimValue { get; set; }

        [Required(ErrorMessage = "RoleId  is required")]
        public int RoleId { get; set; }

    }
}
