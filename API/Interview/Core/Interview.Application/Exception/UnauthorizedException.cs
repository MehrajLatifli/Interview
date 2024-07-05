namespace Interview.Application.Exception
{
    public class UnauthorizedException : ApplicationException
    {
        public UnauthorizedException(string message) : base(message) { }
    }
}
