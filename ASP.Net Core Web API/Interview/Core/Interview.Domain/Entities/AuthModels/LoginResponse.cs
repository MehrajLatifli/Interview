namespace Interview.Domain.Entities.AuthModels
{
    public class LoginResponse
    {

        public string Token { get; set; }

        public string RefreshToken { get; set; }

        public string Expiration { get; set; }
    }
}
