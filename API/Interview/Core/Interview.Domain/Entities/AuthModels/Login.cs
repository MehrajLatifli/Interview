using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Interview.Domain.Entities.AuthModels
{
    public class Login
    {

        public string Username { get; set; }

        public string Password { get; set; }
    }
}
