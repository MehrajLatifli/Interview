using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace Interview.Domain.Entities.Requests
{
    public class DeleteRoleRequestModel
    {


        [Required]
        [JsonPropertyName("roleId")]
        public int RoleId { get; set; }

        [Required]
        [JsonPropertyName("userId")]
        public int UserId { get; set; }
    }


}
