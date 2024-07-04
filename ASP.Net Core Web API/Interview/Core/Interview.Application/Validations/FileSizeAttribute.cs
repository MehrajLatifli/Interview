using Microsoft.AspNetCore.Http;
using System.ComponentModel.DataAnnotations;
using ValidationException = Interview.Application.Exception.ValidationException;

namespace Interview.Application.Validations
{
    [AttributeUsage(AttributeTargets.Property | AttributeTargets.Field, AllowMultiple = false)]
    public class FileSizeAttribute : ValidationAttribute
    {
        private readonly int _maxFileSizeInMB;
        private readonly int _minFileSizeInBytes;

        public FileSizeAttribute(int maxFileSizeInMB, int minFileSizeInKB)
        {
            _maxFileSizeInMB = maxFileSizeInMB;
            _minFileSizeInBytes = minFileSizeInKB * 1024;
        }

        protected override ValidationResult IsValid(object value, ValidationContext validationContext)
        {
            var file = value as IFormFile;

            if (file != null)
            {
                if (file.Length > _maxFileSizeInMB * 1024 * 1024 || file.Length < _minFileSizeInBytes)
                {
                    throw new ValidationException(GetErrorMessage());


                }
            }

            return ValidationResult.Success;
        }

        public string GetErrorMessage()
        {
            return $"The file size should be between {_minFileSizeInBytes / 1024:0.00} KB and {_maxFileSizeInMB} MB.";
        }
    }
}
