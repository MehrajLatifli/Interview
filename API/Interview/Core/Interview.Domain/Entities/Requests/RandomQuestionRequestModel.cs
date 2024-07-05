using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Text.Json.Serialization;
using System.Threading.Tasks;

namespace Interview.Domain.Entities.Requests
{
    public class RandomQuestionRequestModel
    {
        [Required]
        [JsonPropertyName("questionCount")]
        public int QuestionCount { get; set; }

        [Required]
        [JsonPropertyName("structureId")]
        public int StructureId { get; set; }

        [Required]
        [JsonPropertyName("positionId")]
        public int PositionId { get; set; }

        [Required]
        [JsonPropertyName("vacantionId")]
        public int VacantionId { get; set; }

        [Required]
        [JsonPropertyName("sessionId")]
        public int SessionId { get; set; }
    }

}
