using Interview.Domain.Entities.Others;

namespace Interview.Domain.Entities.AuthModels
{
    public class GetAuthModelComparer : IEqualityComparer<GetAuthDTOModel>
    {
        public bool Equals(GetAuthDTOModel x, GetAuthDTOModel y)
        {
            // Compare based on the Username property
            return x.Username == y.Username;
        }

        public int GetHashCode(GetAuthDTOModel obj)
        {
            // Generate a hash code based on the Username property
            return obj.Username.GetHashCode();
        }
    }
}
