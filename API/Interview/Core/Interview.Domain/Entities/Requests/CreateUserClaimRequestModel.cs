using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace Interview.Domain.Entities.Requests
{
    public class CreateUserClaimRequestModel
    {

        [Required]
        [JsonPropertyName("userId")]
        public string UserId { get; set; }

        [Required]
        [JsonPropertyName("userAccessId")]
        public string UserAccessId { get; set; }

    }

}
