using Interview.Application.Exception;
using Microsoft.AspNetCore.Mvc.Formatters;
using Microsoft.IdentityModel.Protocols.WSTrust;
using Microsoft.IdentityModel.SecurityTokenService;
using Serilog.Context;
using System.Net;
using System.Net.Mime;
using System.Text.Json;
using System.Web.Http.Filters;

namespace Interview.API.Middlewares
{
    public class ExceptionMiddleware : IMiddleware
    {

    
        private readonly ILogger<ExceptionMiddleware> _logger;

        public ExceptionMiddleware(ILogger<ExceptionMiddleware> logger)
        {
           
            _logger = logger;
        }



        public async Task InvokeAsync(HttpContext context, RequestDelegate next)
        {

            var username = string.Empty;
            try
            {

                 username = context.User?.Identity?.IsAuthenticated != null || true ? context.User.Identity.Name : null;

                LogContext.PushProperty("user_name", username);
                LogContext.PushProperty("machine_name", Environment.MachineName);


                await next(context);
            }
            catch (Exception exception)
            {
                await HandleExceptionAsync(context, exception, username);

            }
        }

        private async Task HandleExceptionAsync(HttpContext context, Exception exception, string username)
        {
            var code = HttpStatusCode.InternalServerError;
            var result = string.Empty;

            switch (exception)
            {
                case ValidationException validationException:
                    code = HttpStatusCode.UnprocessableEntity;

                    if (validationException.Errors.Any())
                        result = JsonSerializer.Serialize(validationException.Errors);
                    break;
                case ForbiddenException:
                    code = HttpStatusCode.Forbidden;
                    break;
                case NotFoundException:
                    code = HttpStatusCode.NotFound;
                    break;

                case ConflictException:
                    code = HttpStatusCode.Conflict;
                    break;

                case UnauthorizedException:
                    code = HttpStatusCode.Unauthorized;
                    break;

                case BadRequestException:
                    code = HttpStatusCode.BadRequest;
                    break;
            }

            context.Response.ContentType = MediaTypeNames.Application.Json;
            context.Response.StatusCode = (int)code;


            if (string.IsNullOrEmpty(result))
            {

                System.Globalization.CultureInfo.CurrentCulture.ClearCachedData();

                TimeZone localZone = TimeZone.CurrentTimeZone;
                DateTime localTime = localZone.ToLocalTime(DateTime.UtcNow);


                var data = new
                {
                    status = code,
                    title = exception.Message,
                    user = username,
                    date = localTime,
                    machine= Environment.MachineName,
                    mode=Environment.GetEnvironmentVariable("ASPNETCORE_ENVIRONMENT"),
                };


                var options = new JsonSerializerOptions
                {
                    WriteIndented = true,
                    PropertyNamingPolicy = JsonNamingPolicy.CamelCase
                };

                result = JsonSerializer.Serialize(data, options);


                _logger.LogError(exception, "Error occurred: {ErrorMessage}", result);
            }

            await context.Response.WriteAsync(result);
        }
    }


}

