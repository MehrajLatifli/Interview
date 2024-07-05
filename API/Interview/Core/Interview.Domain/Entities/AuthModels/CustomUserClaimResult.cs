namespace Interview.Domain.Entities.AuthModels
{
    public class CustomUserClaimResult
    {
        public List<int> UserIds { get; set; }
        public List<string> ClaimTypes { get; set; }
        public List<string> ClaimValues { get; set; }
    }
}
