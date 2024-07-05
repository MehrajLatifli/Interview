using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace Interview.Domain.Entities.Requests
{
    public class CreateRoleClaimRequestModel
    {

        [Required]
        [JsonPropertyName("roleId")]
        public string RoleId { get; set; }

        [Required]
        [JsonPropertyName("roleAccessMethodId")]
        public string RoleAccessMethodId { get; set; }

    }

}
