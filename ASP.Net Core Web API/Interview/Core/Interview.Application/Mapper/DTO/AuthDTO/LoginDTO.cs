using System.ComponentModel.DataAnnotations;

namespace Interview.Application.Mapper.DTO.AuthDTO
{

    public class LoginDTO
    {
        [Required(ErrorMessage = "User Name is required")]
        public string Username { get; set; }

        [Required(ErrorMessage = "User Password is required")]
        public string Password { get; set; }
    }

}

