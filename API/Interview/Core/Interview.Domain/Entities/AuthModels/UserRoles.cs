namespace Interview.Domain.Entities.AuthModels
{
    public struct UserRoles
    {
        public const string Admin = "Admin";
        public const string HR = "HR";
    }


    public static class CustomPolicy
    {
        public static string Policy = "Policy";
    }

    public static class CustomPolicyRole
    {
        public static string PolicyRole = "PolicyRole";
    }
}
