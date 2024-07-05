using System.ComponentModel.DataAnnotations;

namespace Interview.Domain.Entities.AuthModels
{
    public class UpdatePassword
    {
        public string? OldPassword { get; set; }

        public string? NewPassword { get; set; }


    }
}
