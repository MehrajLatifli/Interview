


using Interview.Application.Exception;
using Interview.Persistence.Contexts.InterviewDbContext;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.EntityFrameworkCore.Storage;

namespace Interview.API.Attribute
{
   



    public class ApiExceptionFilterAttribute : ExceptionFilterAttribute
    {
        //    private readonly IDictionary<Type, Action<ExceptionContext>> _exceptionHandlers;
        //    private readonly InterviewContext db;
        //    private IDbContextTransaction Transaction => db.Database.CurrentTransaction;
        //    private readonly ILogger<ApiExceptionFilterAttribute> _logger;
        //    public ApiExceptionFilterAttribute(InterviewContext db, ILogger<ApiExceptionFilterAttribute> logger)
        //    {
        //        this.db = db;

        //        // Register known exception types and handlers.
        //        _exceptionHandlers = new Dictionary<Type, Action<ExceptionContext>>
        //        {
        //            {typeof(ValidationException), HandleValidationException},
        //            {typeof(NotFoundException), HandleNotFoundException},
        //            {typeof(UnauthorizedException), HandleUnauthorizedException},
        //            {typeof(BadHttpRequestException), HandleBadHttpRequestException},
        //            {typeof(ConflictException), HandleConflictException},

        //        };
        //        _logger = logger;
        //    }

        //    private void HandleConflictException(ExceptionContext context)
        //    {
        //        var details = new ProblemDetails()
        //        {
        //            Status = StatusCodes.Status409Conflict,
        //            Title = context.Exception.Message
        //        };

        //        context.ExceptionHandled = true;
        //        context.Result = new NotFoundObjectResult(details);
        //    }

        //    public override async Task OnExceptionAsync(ExceptionContext context)
        //    {
        //        HandleException(context);

        //        if (Transaction is not null)
        //            await db.Database.RollbackTransactionAsync();

        //        await base.OnExceptionAsync(context);
        //    }

        //    private void HandleException(ExceptionContext context)
        //    {
        //        Type type = context.Exception.GetType();
        //        if (_exceptionHandlers.ContainsKey(type))
        //        {
        //            _exceptionHandlers[type].Invoke(context);
        //            return;
        //        }

        //        if (!context.ModelState.IsValid)
        //        {
        //            HandleInvalidModelStateException(context);
        //            return;
        //        }

        //        HandleUnknownException(context);
        //    }
        //    private void HandleValidationException(ExceptionContext context)
        //    {
        //        var exception = context.Exception as ValidationException;
        //        var details = new ValidationProblemDetails(exception?.Errors);
        //        context.ExceptionHandled = true;
        //        context.Result = new UnprocessableEntityObjectResult(details);


        //        //_logger.LogError(context.Exception, "ValidationException occurred: {ErrorMessage}", context.Exception.Message);

        //    }

        //    private void HandleNotFoundException(ExceptionContext context)
        //    {
        //        var details = new ProblemDetails()
        //        {
        //            Status = StatusCodes.Status404NotFound,
        //            Title = context.Exception.Message
        //        };

        //        context.ExceptionHandled = true;
        //        context.Result = new NotFoundObjectResult(details);

        //        //_logger.LogError(context.Exception, "NotFoundException occurred: {ErrorMessage}", context.Exception.Message);

        //    }

        //    private void HandleInvalidModelStateException(ExceptionContext context)
        //    {
        //        var details = new ValidationProblemDetails(context.ModelState);

        //        context.ExceptionHandled = true;
        //        context.Result = new BadRequestObjectResult(details);
        //    }

        //    private void HandleUnknownException(ExceptionContext context)
        //    {
        //        var details = new ProblemDetails
        //        {
        //            Status = StatusCodes.Status500InternalServerError,
        //            Title = "Internal Server Error"
        //        };

        //        context.ExceptionHandled = true;
        //        context.Result = new ObjectResult(details);

        //        //_logger.LogError(context.Exception, "UnknownException occurred: {ErrorMessage}", context.Exception.Message);
        //    }

        //    private void HandleUnauthorizedException(ExceptionContext context)
        //    {
        //        var details = new ProblemDetails
        //        {
        //            Status = StatusCodes.Status401Unauthorized,
        //            Title = context.Exception.Message
        //        };

        //        context.ExceptionHandled = true;
        //        context.Result = new ObjectResult(details);

        //        //_logger.LogError(context.Exception, "Unauthorized occurred: {ErrorMessage}", context.Exception.Message);
        //    }

        //    private void HandleBadHttpRequestException(ExceptionContext context)
        //    {
        //        var details = new ProblemDetails
        //        {
        //            Status = StatusCodes.Status400BadRequest,
        //            Title = context.Exception.Message
        //        };

        //        context.ExceptionHandled = true;
        //        context.Result = new ObjectResult(details);

        //        //_logger.LogError(context.Exception, "BadHttpRequest occurred: {ErrorMessage}", context.Exception.Message);
        //    }
        //}
    }
}
