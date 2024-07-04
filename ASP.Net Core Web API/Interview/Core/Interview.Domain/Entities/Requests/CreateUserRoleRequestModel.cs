using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace Interview.Domain.Entities.Requests
{
    public class CreateUserRoleRequestModel
    {

        [Required]
        [JsonPropertyName("userId")]
        public string UserId { get; set; }

        [Required]
        [JsonPropertyName("roleId")]
        public string RoleId { get; set; }

    }

}
