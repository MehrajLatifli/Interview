using System.ComponentModel.DataAnnotations;
using System.Text.RegularExpressions;
using ValidationException = Interview.Application.Exception.ValidationException;

namespace Interview.Application.Validations
{
    public class EmailAttribute : ValidationAttribute
    {
        private readonly string _errorMessage;

        public EmailAttribute(string errorMessage = "Invalid email format.")
        {
            _errorMessage = errorMessage;
        }

        protected override ValidationResult IsValid(object value, ValidationContext validationContext)
        {
            if (value == null || string.IsNullOrWhiteSpace(value.ToString()))
            {
                return ValidationResult.Success; 
            }

            string email = value.ToString();
            if (!IsStrongEmail(email))
            {


                throw new ValidationException(_errorMessage);
            }

            return ValidationResult.Success;
        }

        private bool IsStrongEmail(string email)
        {


            string pattern = @"^(?=.{8,100}$)([a-zA-Z0-9]+[-._+&])*[a-zA-Z0-9]+@([-a-zA-Z0-9]+\.)+[a-zA-Z]{2,20}$";
            return Regex.IsMatch(email, pattern);
        }
    }
}
