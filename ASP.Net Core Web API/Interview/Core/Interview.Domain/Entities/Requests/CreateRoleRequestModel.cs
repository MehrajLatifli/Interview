using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace Interview.Domain.Entities.Requests
{
    public class CreateRoleRequestModel
    {

        [Required]
        [JsonPropertyName("roleName")]
        public string RoleName { get; set; }

    }




}
