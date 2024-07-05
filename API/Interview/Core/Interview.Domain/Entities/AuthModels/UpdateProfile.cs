
using Microsoft.AspNetCore.Http;
using System.ComponentModel.DataAnnotations;

namespace Interview.Domain.Entities.AuthModels
{
    public class UpdateProfile
    {


        public string? Username { get; set; }

        public string? Email { get; set; }

        public string? OldPassword { get; set; }

        public string? NewPassword { get; set; }

        public string? PhoneNumber { get; set; }

        public IFormFile? ImagePath { get; set; }

    }
}
