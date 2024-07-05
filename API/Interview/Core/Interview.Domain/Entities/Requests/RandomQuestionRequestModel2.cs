using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace Interview.Domain.Entities.Requests
{
    public class RandomQuestionRequestModel2
    {
        [Required]
        [JsonPropertyName("questionCount")]
        public int QuestionCount { get; set; }

        [Required]
        [JsonPropertyName("vacantionId")]
        public int VacantionId { get; set; }

        [Required]
        [JsonPropertyName("sessionId")]
        public int SessionId { get; set; }
    }

}
