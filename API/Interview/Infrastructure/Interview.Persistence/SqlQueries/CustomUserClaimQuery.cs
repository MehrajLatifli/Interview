using Interview.Domain.Entities.AuthModels;
using Interview.Persistence.Contexts.InterviewDbContext;
using Microsoft.EntityFrameworkCore;

namespace Interview.Persistence.SqlQueries
{
    public static class CustomUserClaimQuery
    {


        public static async Task<CustomUserClaimResult> GetCustomUserClaimsAsync(InterviewContext interviewContext)
        {
            var UserId = interviewContext.Database.SqlQuery<int>($"SELECT UserId  FROM CustomUserClaim cuc ");

            var ClaimType = interviewContext.Database.SqlQuery<string>($"SELECT ClaimType  FROM CustomUserClaim cuc ");

            var ClaimValue = interviewContext.Database.SqlQuery<string>($"SELECT ClaimValue  FROM CustomUserClaim cuc ");

            // Assuming you have a class to hold the result
            var result = new CustomUserClaimResult
            {
                UserIds = UserId.ToList(),
                ClaimTypes = ClaimType.ToList(),
                ClaimValues = ClaimValue.ToList()
            };

            return result;
        }
    }
}
