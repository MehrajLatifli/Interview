using Interview.Application.Validations;
using Microsoft.AspNetCore.Http;
using System.ComponentModel.DataAnnotations;

namespace Interview.Application.Mapper.DTO.AuthDTO
{
    public class UpdateProfileDTO
    {

        [Required(ErrorMessage = "User Name is required")]
        public string? Username { get; set; }
        [Required(ErrorMessage = "User Email is required")]
        [Email(ErrorMessage = "Invalid email format.")]
        public string? Email { get; set; }
        [Required(ErrorMessage = "Old password is required")]
        public string? OldPassword { get; set; }
        [Required(ErrorMessage = "New password is required")]
        public string? NewPassword { get; set; }
        [Required(ErrorMessage = "Phone Number is required")]
        public string? PhoneNumber { get; set; }
        [Required(ErrorMessage = "Image Path is required")]
        [AllowedExtensions(new string[] { ".jpg", ".jpeg", ".png", ".gif" })]
        [FileSize(5, 10)]
        public IFormFile ImagePath { get; set; }

    }
}
