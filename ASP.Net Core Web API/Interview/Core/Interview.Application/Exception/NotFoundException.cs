using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Interview.Application.Exception
{
    public class NotFoundException : ApplicationException
    {
        public NotFoundException(string message) : base(message) { }

    }
}
