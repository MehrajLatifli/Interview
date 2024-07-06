using Interview.Application.Mapper;
using Interview.Persistence;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Http.Features;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;
using System.Text;
using System.Text.Json;
using Interview.Persistence.ServiceExtensions;
using Microsoft.Extensions.Azure;
using Interview.API.Attribute;
using Interview.Domain.Entities.AuthModels;
using Serilog;
using Serilog.Core;
using Serilog.Sinks.PostgreSQL;
using System.Security.Claims;
using Microsoft.IdentityModel.Logging;
using Serilog.Context;
using Microsoft.AspNetCore.HttpLogging;
using static System.Net.WebRequestMethods;
using Microsoft.Data.SqlClient;
using Serilog.Events;
using Org.BouncyCastle.Asn1.IsisMtt.X509;
using Interview.API.Middlewares;
using System.Configuration;
using Microsoft.AspNetCore.Server.Kestrel.Core;
using Interview.Persistence.Contexts.InterviewDbContext;
using Microsoft.AspNetCore.Authorization;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.


builder.Services.AddMvcCore().AddApiExplorer();

builder.Services.AddSwaggerGenServiceExtension();


builder.Services.AddCors();


builder.Services.AddAuthorization();

builder.Services.AddAuthorization(options =>
{
    options.DefaultPolicy = new AuthorizationPolicyBuilder()
        .RequireAuthenticatedUser()
        .Build();
});

//builder.Services.AddSingleton<IAuthorizationPolicyProvider, CustomAuthorizationPolicyProvider>();
//builder.Services.AddSingleton< DefaultAuthorizationPolicyProvider>();


builder.Services.AddCustomAuthorizationPoliciesServiceExtension();




builder.Services.AddControllers(options =>
{
    options.Filters.Add<ApiExceptionFilterAttribute>();
    options.SuppressImplicitRequiredAttributeForNonNullableReferenceTypes = true;
})
.AddJsonOptions(options =>
{
    options.JsonSerializerOptions.PropertyNamingPolicy = JsonNamingPolicy.CamelCase;
})
.AddNewtonsoftJson()
.AddJsonOptions(options =>
{
    options.JsonSerializerOptions.PropertyNamingPolicy = null;
    options.JsonSerializerOptions.DictionaryKeyPolicy = null;
});


builder.Services.Configure<FormOptions>(x =>
{
    x.MultipartBodyLengthLimit = long.MaxValue;
    x.ValueLengthLimit = int.MaxValue;
    x.MultipartHeadersLengthLimit = int.MaxValue;
});

builder.Services.Configure<KestrelServerOptions>(options =>
{
    options.Limits.MaxRequestBodySize = long.MaxValue;
    options.Limits.MaxRequestBufferSize = long.MaxValue;
    options.Limits.MaxRequestLineSize = int.MaxValue;

});

builder.Services.AddHttpClient();

builder.Services.AddHttpContextAccessor();

builder.Services.AddAutoMapper(typeof(MapperProfile).Assembly);

builder.Services.AddMemoryCache();


builder.Services.AddPersistenceServices();


builder.Services.AddAuthentication(options =>
{
    options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultScheme = JwtBearerDefaults.AuthenticationScheme;

}).AddJwtBearer(options =>
{
    options.SaveToken = true;
    options.RequireHttpsMetadata = false;
    options.TokenValidationParameters = new TokenValidationParameters()
    {
        ValidateAudience = true,
        ValidateLifetime = true,
        ValidateIssuerSigningKey = true,
        ClockSkew = TimeSpan.Zero,
        ValidAudience = builder.Configuration["JWT:ValidateAudience"],
        ValidIssuer = builder.Configuration["JWT:ValidateIssuer"],
        IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(builder.Configuration["JWT:Secret"])),
        NameClaimType=ClaimTypes.Name

    };
});



//builder.WebHost.UseUrls("https://localhost:6000");





var env = builder.Environment;

builder.Configuration
    .SetBasePath(env.ContentRootPath)
    .AddJsonFile("appsettings.json", optional: false)
    .AddJsonFile($"appsettings{env.EnvironmentName}.json", optional: true).Build();


builder.Services.AddRateLimiterServiceExtension();


builder.Services.AddAzureClients(clientBuilder =>
{
    clientBuilder.AddBlobServiceClient(builder.Configuration["local-1:blob"], preferMsi: true);
    clientBuilder.AddQueueServiceClient(builder.Configuration["local-1:queue"], preferMsi: true);
});



builder.Services.AddEndpointsApiExplorer();
builder.Services.AddAzureClients(clientBuilder =>
{
    clientBuilder.AddBlobServiceClient(builder.Configuration["AzureConnectionStrings:blob"], preferMsi: true);
    clientBuilder.AddQueueServiceClient(builder.Configuration["AzureConnectionStrings:queue"], preferMsi: true);
});




// Disable when use ApiExceptionFilterAttribute
builder.Services.AddTransient<ExceptionMiddleware>();



builder.Host.UseSerilog(builder.Services.AddCustomSerilog(builder.Configuration.GetConnectionString("LogConnection"), builder.Configuration["Seq:SeqConnection"]));


//builder.WebHost.ConfigureKestrel(options =>
//{
//    options.ListenAnyIP(6000); 
//});

var app = builder.Build();



// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{

    app.UseSwagger();
    app.UseSwaggerUI();
    app.UseHttpsRedirection();
}
if (app.Environment.IsProduction())
{
    app.UseSwagger();
    app.UseSwaggerUI();
    app.UseHttpsRedirection();
}
else
{
    app.UseHttpsRedirection();
    app.UseHsts();
    app.UseSwagger();
    app.UseSwaggerUI(c => c.SwaggerEndpoint("/swagger/v1/swagger.json", "API V1"));
}



app.UseStaticFiles();

app.UseHttpLogging();

app.UseSerilogRequestLogging();



app.UseRateLimiter();

app.UseRouting();



app.UseAuthentication();
app.UseAuthorization();




//app.Use(async (context, next) =>
//{
//    var username = context.User?.Identity?.IsAuthenticated != null || true ? context.User.Identity.Name : null;

//    LogContext.PushProperty("user_name", username);

//    await next();

//});


// Disable when use ApiExceptionFilterAttribute
app.UseMiddleware<ExceptionMiddleware>();



app.UseCors(x => x.AllowAnyMethod().AllowAnyHeader().SetIsOriginAllowed(origin => true).AllowCredentials());

app.MapControllers();

app.MapControllerRoute(name: "default", pattern: "{Interview}/{action=Index}/{id?}");

app.Run();
