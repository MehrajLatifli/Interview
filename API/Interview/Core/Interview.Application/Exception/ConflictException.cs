using System;

namespace Interview.Application.Exception
{
    public class ConflictException : ApplicationException
    {
        public ConflictException(string message) : base(message) { }

    }
}
