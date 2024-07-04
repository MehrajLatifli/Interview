namespace Interview.Application.Exception
{
    public class ForbiddenException : ApplicationException
    {
        public ForbiddenException() { }

        public ForbiddenException(string message) : base(message) { }

        public ForbiddenException(string message, ApplicationException innerException) : base(message, innerException) { }
    }
}
