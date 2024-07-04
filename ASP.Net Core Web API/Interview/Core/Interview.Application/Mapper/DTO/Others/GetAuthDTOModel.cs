
using Microsoft.AspNetCore.Http;

namespace Interview.Domain.Entities.Others
{
    public class GetAuthDTOModel
    {
        public string Id { get; set; }

        public string Username { get; set; }

        public string Email { get; set; }

        public string? PhoneNumber { get; set; }

        public string ImagePath { get; set; }

        public List <PermitionsDTOModel> Permitions { get; set; }

    }
}
