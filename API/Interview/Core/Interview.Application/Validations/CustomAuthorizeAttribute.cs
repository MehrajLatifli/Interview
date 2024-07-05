using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.AspNetCore.Mvc;
using Interview.Application.Exception;

namespace Interview.Application.Validations
{
    //    [AttributeUsage(AttributeTargets.Method | AttributeTargets.Class, AllowMultiple = true)]
    //public class CustomAuthorizeAttribute : AuthorizeAttribute, IAuthorizationFilter
    //{
    //        public CustomAuthorizeAttribute(string policy) : base(policy) { }

    //        public string CustomRole { get; set; }

    //        public void OnAuthorization(AuthorizationFilterContext context)
    //        {
    //            if (context == null)
    //            {
    //                throw new ArgumentNullException(nameof(context));
    //            }

    //            var authorized = context.HttpContext.User.Identity?.IsAuthenticated;
    //            if (authorized != true)
    //            {
    //                throw new UnauthorizedAccessException("User is not authenticated.");
    //            }

    //            if (!string.IsNullOrEmpty(CustomRole))
    //            {
    //                var customPolicy = $"{Policy}";
    //                Policy = customPolicy;
    //            }

    //            var authorizedRoles = CustomRole;
    //            var user = context.HttpContext.User;

    //            if (user != null)
    //            {
    //                var isInRole = authorizedRoles.Any(role => user.IsInRole(CustomRole));

    //                if (!isInRole)
    //                {
    //                    throw new ForbiddenException($"{user.Identity.Name} is not authorized for this operation.");
    //                }
    //            }
    //        }

    //    }




    namespace Interview.Application.Validations
    {
        [AttributeUsage(AttributeTargets.Method | AttributeTargets.Class, AllowMultiple = true)]
        public class CustomAuthorizeAttribute : Attribute, IAuthorizationFilter
        {
            public string[] CustomRoles { get; set; }

            public void OnAuthorization(AuthorizationFilterContext context)
            {
                if (context == null)
                {
                    throw new ArgumentNullException(nameof(context));
                }

                var authorized = context.HttpContext.User.Identity?.IsAuthenticated;
                if (authorized != true)
                {
                    throw new UnauthorizedAccessException("User is not authenticated.");
                }

                var user = context.HttpContext.User;

                if (user != null)
                {
                    bool isAuthorized = false;
                    foreach (var role in CustomRoles)
                    {
                        if (user.IsInRole(role))
                        {
                            isAuthorized = true;
                            break;
                        }
                    }

                    if (!isAuthorized)
                    {
                        throw new ForbiddenException($"{user.Identity.Name} is not authorized for this operation.");
                    }
                }
            }
        }
    }

}
