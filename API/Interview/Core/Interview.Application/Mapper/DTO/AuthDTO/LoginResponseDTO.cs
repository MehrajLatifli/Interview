namespace Interview.Application.Mapper.DTO.AuthDTO
{
    public class LoginResponseDTO
    {
        public string Token { get; set; }

        public string RefreshToken { get; set; }

        public string Expiration { get; set; }
    }
}

