using System.ComponentModel.DataAnnotations;

namespace Interview.Application.Mapper.DTO.AuthDTO
{
    public class UpdatePasswordDTO
    {

        [Required(ErrorMessage = "Old password is required")]
        public string? OldPassword { get; set; }

        [Required(ErrorMessage = "New password is required")]
        public string? NewPassword { get; set; }


    }
}
