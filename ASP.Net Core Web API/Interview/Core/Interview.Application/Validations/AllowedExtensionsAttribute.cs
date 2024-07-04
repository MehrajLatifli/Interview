using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Interview.Application.Exception;
using ValidationException = Interview.Application.Exception.ValidationException;
using Microsoft.AspNetCore.Authorization;

namespace Interview.Application.Validations
{
    public class AllowedExtensionsAttribute : ValidationAttribute
    {
        private readonly string[] _extensions;
        public AllowedExtensionsAttribute(string[] extensions)
        {
            _extensions = extensions;
        }

        protected override ValidationResult IsValid(
            object value, ValidationContext validationContext)
        {
            var file = value as IFormFile;
            if (file != null)
            {
                var extension = Path.GetExtension(file.FileName)?.ToLower();
                if (!_extensions.Contains(extension))
                {


                    throw new ValidationException(GetErrorMessage());
                }
            }

            return ValidationResult.Success;
        }

        public string GetErrorMessage()
        {
            string supportedExtensions = string.Join(", ", _extensions);
            return $"Only {supportedExtensions} extensions are allowed!";
        }
    }



}
