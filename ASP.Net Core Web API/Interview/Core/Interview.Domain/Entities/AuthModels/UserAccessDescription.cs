namespace Interview.Domain.Entities.AuthModels
{
    public struct UserAccessDescription
    {

        public static readonly string WriteDescription_ClaimValue = "Only gives write permissions.";
        public static readonly string ReadDescription_ClaimValue = "Only gives read permissions.";
        public static readonly string AllDescription_ClaimValue = "Allows all permissions.";

    }
}
