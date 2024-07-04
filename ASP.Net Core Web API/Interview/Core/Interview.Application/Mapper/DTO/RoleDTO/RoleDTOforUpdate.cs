using System.ComponentModel.DataAnnotations;

namespace Interview.Application.Mapper.DTO.RoleDTO
{
    public class RoleDTOforUpdate
    {
        [Required(ErrorMessage = "Id  is required")]
        public int Id { get; set; }

        [Required(ErrorMessage = "Name  is required")]
        public string Name { get; set; }

        [Required(ErrorMessage = "ConcurrencyStamp  is required")]
        public string ConcurrencyStamp { get; set; }

    }
}
