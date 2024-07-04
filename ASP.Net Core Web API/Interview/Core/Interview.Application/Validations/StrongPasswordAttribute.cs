using System.ComponentModel.DataAnnotations;
using System.Text.RegularExpressions;
using ValidationException = Interview.Application.Exception.ValidationException;

namespace Interview.Application.Validations
{
    public class StrongPasswordAttribute : ValidationAttribute
    {
        private readonly string _errorMessage;

        public StrongPasswordAttribute(string errorMessage = "Minimum eight characters, at least one uppercase letter, one lowercase letter, one special character and one number")
        {
            _errorMessage = errorMessage;
        }

        protected override ValidationResult IsValid(object value, ValidationContext validationContext)
        {
            if (value == null || string.IsNullOrWhiteSpace(value.ToString()))
            {
                return ValidationResult.Success;
            }

            string password = value.ToString();
            if (!IsStrongPassword(password))
            {


                throw new ValidationException(_errorMessage);
            }

            return ValidationResult.Success;
        }

        private bool IsStrongPassword(string password)
        {


            string pattern = @"^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[#?!@$%^&*-_])(?=.*?[0-9]).{8,}$";
            return Regex.IsMatch(password, pattern);
        }
    }
}
