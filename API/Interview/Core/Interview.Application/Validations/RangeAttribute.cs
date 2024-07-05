using System.ComponentModel.DataAnnotations;
using ValidationException = Interview.Application.Exception.ValidationException;

namespace Interview.Application.Validations
{
    public class RangeAttribute : ValidationAttribute
    {
        private readonly double _minValue;
        private readonly double _maxValue;

        public RangeAttribute(double minValue, double maxValue)
        {
            _minValue = minValue;
            _maxValue = maxValue;
        }

        protected override ValidationResult IsValid(object value, ValidationContext validationContext)
        {
            if (value is double || value is decimal || value is int)
            {
                var number = Convert.ToDouble(value);
                
                if (number >= _minValue && number <= _maxValue)
                {
                    return ValidationResult.Success;
                }
                else
                {
                    throw new ValidationException($"The {validationContext.DisplayName} must be between {_minValue} and {_maxValue}.");

                }
            }

            throw new ValidationException("Invalid input type. This attribute is only applicable to double numbers.");
        }
    }

}
