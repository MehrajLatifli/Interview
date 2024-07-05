using System.ComponentModel.DataAnnotations;

namespace Interview.Application.Mapper.DTO.UserRoleDTO
{
    public class UserRoleDTOforUpdate
    {
        [Required(ErrorMessage = "Id  is required")]
        public int Id { get; set; }

        [Required(ErrorMessage = "UserId  is required")]
        public int UserId { get; set; }

        [Required(ErrorMessage = "RoleId  is required")]
        public int RoleId { get; set; }

    }
}
