using Interview.Application.Validations;
using Microsoft.AspNetCore.Http;
using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace Interview.Application.Mapper.DTO.AuthDTO
{
    public class RegisterDTO
    {

        //[Required]
        //[JsonPropertyName("roleName")]
        //public string RoleName { get; set; }

        //[Required]
        //[JsonPropertyName("UserAccessId")]
        //public int UserAccessId { get; set; }

        //[Required]
        //[JsonPropertyName("RoleName")]
        //public int RoleAccessMethodId { get; set; }


        [Required(ErrorMessage = "User Name is required")]
        public string Username { get; set; }

        [Required(ErrorMessage = "User Email is required")]
        [Email(ErrorMessage = "Invalid email format.")]
        public string Email { get; set; }

        [Required(ErrorMessage = "User Password is required")]
        [StrongPassword(ErrorMessage = "Minimum eight characters, at least one uppercase letter, one lowercase letter, one special character and one number")]
        public string Password { get; set; }

        [Required(ErrorMessage = "Phone Number is required")]
        public string? PhoneNumber { get; set; }

        [Required(ErrorMessage = "Image Path is required")]
        [AllowedExtensions(new string[] { ".jpg", ".jpeg", ".png", ".gif" })]
        [FileSize(5, 10)]
        public IFormFile ImagePath { get; set; }




    }


    public class RegisterAdminDTO
    {

        //[Required]
        //[JsonPropertyName("roleName")]
        //public string RoleName { get; set; }

        //[Required]
        //[JsonPropertyName("UserAccessId")]
        //public int UserAccessId { get; set; }

        //[Required]
        //[JsonPropertyName("RoleName")]
        //public int RoleAccessMethodId { get; set; }


        [Required(ErrorMessage = "User Name is required")]
        public string Username { get; set; }

        [Required(ErrorMessage = "User Email is required")]
        [Email(ErrorMessage = "Invalid email format.")]
        public string Email { get; set; }

        [Required(ErrorMessage = "User Password is required")]
        [StrongPassword(ErrorMessage = "Minimum eight characters, at least one uppercase letter, one lowercase letter, one special character and one number")]
        public string Password { get; set; }

        [Required(ErrorMessage = "Phone Number is required")]
        public string? PhoneNumber { get; set; }

        [Required(ErrorMessage = "Image Path is required")]
        [AllowedExtensions(new string[] { ".jpg", ".jpeg", ".png", ".gif" })]
        [FileSize(5, 10)]
        public IFormFile ImagePath { get; set; }




    }
}
