using System.ComponentModel.DataAnnotations;

namespace Interview.Application.Mapper.DTO.UserDTO
{
    public class UserDTOforCreate
    {


        [Required(ErrorMessage = "UserName  is required")]
        public string UserName { get; set; }

        [Required(ErrorMessage = "Email  is required")]
        public string Email { get; set; }

        [Required(ErrorMessage = "Password  is required")]
        public string Password { get; set; }


        [Required(ErrorMessage = "Phonenumber  is required")]
        public string Phonenumber { get; set; }

        [Required(ErrorMessage = "ConcurrencyStamp  is required")]
        public string ConcurrencyStamp { get; set; }

        [Required(ErrorMessage = "ImagePath  is required")]
        public string ImagePath { get; set; }

    }
}
