
using Interview.Application.Repositories.Custom;
using Interview.Application.Services.Abstract;
using Interview.Application.Services.Concrete;
using Interview.Persistence.Contexts.InterviewDbContext;
using Interview.Persistence.Repositories.Custom;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Http;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using System.Threading.RateLimiting;
using System.Threading.Tasks;
using ConfigurationManager = Microsoft.Extensions.Configuration.ConfigurationManager;
using Swashbuckle.AspNetCore.SwaggerGen;
using Microsoft.OpenApi.Models;
using Interview.Domain.Entities.AuthModels;
using Microsoft.AspNetCore.HttpLogging;
using System.Collections.ObjectModel;
using System.Data;
using Serilog.Core;
using Serilog;
using Serilog.Sinks.PostgreSQL;
using Serilog.Events;
using Interview.Persistence.LogSettings.ColumnWriters;
using System.Diagnostics;
using Microsoft.Extensions.Hosting;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Authorization;
using Interview.Application.Validations;
using Interview.Domain.Entities.IdentityAuth;
using Interview.Application.Repositories.Abstract;

namespace Interview.Persistence.ServiceExtensions
{
    public static class ServiceExtension
    {
        public static string CustomDbConnectionString
        {
            get
            {
                ConfigurationManager configurationManager = new ConfigurationManager();
                //configurationManager.SetBasePath(Path.Combine(Directory.GetCurrentDirectory(), "../../Presentation/Interview.API"));
                configurationManager.AddJsonFile("appsettings.json");

                return configurationManager.GetConnectionString("CustomDbConnection");
            }
        }

        public static string ConnectionString
        {
            get
            {
                ConfigurationManager configurationManager = new ConfigurationManager();
                //configurationManager.SetBasePath(Path.Combine(Directory.GetCurrentDirectory(), "../../Presentation/Interview.API"));
                configurationManager.AddJsonFile("appsettings.json");

                return configurationManager.GetConnectionString("DefaultConnection");
            }
        }


        public static string ConnectionStringAzure
        {
            get
            {
                ConfigurationManager configurationManager = new ConfigurationManager();
                //configurationManager.SetBasePath(Path.Combine(Directory.GetCurrentDirectory(), "../../Presentation/Interview.API"));
                configurationManager.AddJsonFile("appsettings.json");

                return configurationManager["ConnectionAzureStorage"];
            }
        }


     


        public static void AddPersistenceServices(this IServiceCollection services)
        {



            services.AddDbContext<InterviewContext>(options => options.UseSqlServer(CustomDbConnectionString));


            //services.AddIdentity<CurrentUser, CurrentRole>(options =>
            //{
            //    options.User.RequireUniqueEmail = false;
            //})
            //.AddEntityFrameworkStores<InterviewContext>()
            //.AddDefaultTokenProviders();

            services.AddScoped<IUserReadRepository, UserReadRepository>();
            services.AddScoped<IUserWriteRepository, UserWriteRepository>();

            services.AddScoped<IRoleReadRepository, RoleReadRepository>();
            services.AddScoped<IRoleWriteRepository, RoleWriteRepository>();

            services.AddScoped<IUserRoleReadRepository, UserRoleReadRepository>();
            services.AddScoped<IUserRoleWriteRepository, UserRoleWriteRepository>();

            services.AddScoped<IUserClaimReadRepository, UserClaimReadRepository>();
            services.AddScoped<IUserClaimWriteRepository, UserClaimWriteRepository>();

            services.AddScoped<IRoleClaimReadRepository, RoleClaimReadRepository>();
            services.AddScoped<IRoleClaimWriteRepository, RoleClaimWriteRepository>();


            services.AddScoped<IAuthService, AuthServiceManager>();

            services.AddScoped<ICandidateDocumentService, CandidateDocumentServiceManager>();

            services.AddScoped<ICandidateService, CandidateServiceManager>();

            services.AddScoped<ICategoryService, CategoryServiceManager>();

            services.AddScoped<ILevelService, LevelServiceManager>();

            services.AddScoped<IPositionService, PositionServiceManager>();

            services.AddScoped<IQuestionService, QuestionServiceManager>();

            services.AddScoped<ISessionQuestionService, SessionQuestionServiceManager>();

            services.AddScoped<ISessionService, SessionServiceManager>();

            services.AddScoped<IStructureService, StructureServiceManager>();

            services.AddScoped<IStructureTypeService, StructureTypeServiceManager>();

            services.AddScoped<IVacancyService, VacancyServiceManager>();




            services.AddScoped<ICandidateDocumentWriteRepository, CandidateDocumentWriteRepository>();
            services.AddScoped<ICandidateDocumentReadRepository, CandidateDocumentReadRepository>();

            services.AddScoped<ICandidateWriteRepository, CandidateWriteRepository>();
            services.AddScoped<ICandidateReadRepository, CandidateReadRepository>();

            services.AddScoped<IPositionWriteRepository, PositionWriteRepository>();
            services.AddScoped<IPositionReadRepository, PositionReadRepository>();

            services.AddScoped<IVacancyWriteRepository, VacancyWriteRepository>();
            services.AddScoped<IVacancyReadRepository, VacancyReadRepository>();

            services.AddScoped<IStructureTypeWriteRepository, StructureTypeWriteRepository>();
            services.AddScoped<IStructureTypeReadRepository, StructureTypeReadRepository>();

            services.AddScoped<IStructureWriteRepository, StructureWriteRepository>();
            services.AddScoped<IStructureReadRepository, StructureReadRepository>();

            services.AddScoped<ISessionWriteRepository, SessionWriteRepository>();
            services.AddScoped<ISessionReadRepository, SessionReadRepository>();

            services.AddScoped<ISessionQuestionWriteRepository, SessionQuestionWriteRepository>();
            services.AddScoped<ISessionQuestionReadRepository, SessionQuestionReadRepository>();

            services.AddScoped<IQuestionWriteRepository, QuestionWriteRepository>();
            services.AddScoped<IQuestionReadRepository, QuestionReadRepository>();

            services.AddScoped<ICategoryWriteRepository, CategoryWriteRepository>();
            services.AddScoped<ICategoryReadRepository, CategoryReadRepository>();

            services.AddScoped<ILevelWriteRepository, LevelWriteRepository>();
            services.AddScoped<ILevelReadRepository, LevelReadRepository>();




        }


        public static IServiceCollection AddSwaggerGenServiceExtension(this IServiceCollection services)
        {
            services.AddSwaggerGen(c =>
            {
                c.SwaggerDoc("v1", new OpenApiInfo
                {
                    Title = "Interview WebAPI",
                    Version = "v1",
                    Description = "It is intended for the convenience of the interview process."
                });

                c.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme
                {
                    Name = "Authorization",
                    Type = SecuritySchemeType.ApiKey,
                    Scheme = "Bearer",
                    BearerFormat = "JWT",
                    In = ParameterLocation.Header,
                    Description = "Enter `Bearer` [space] and then your valid token in the text input below. \r\n\r\n Example: \"Bearer apikey \""
                });

                c.AddSecurityRequirement(new OpenApiSecurityRequirement
        {
                {
                    new OpenApiSecurityScheme
                    {
                        Reference = new OpenApiReference
                        {
                            Type = ReferenceType.SecurityScheme,
                            Id = "Bearer"
                        }
                    }, new string[]{}
                }
        });
            });

            return services;
        }




        public static void AddCustomAuthorizationPoliciesServiceExtension(this IServiceCollection services)
        {
            services.AddAuthorization(options =>
            {
                //options.AddPolicy("AdminOnly", policy =>
                //    policy.RequireRole(UserRoles.Admin));

                //options.AddPolicy("HROnly", policy =>
                //    policy.RequireRole(UserRoles.HR));
             

                //options.AddPolicy("AllRoles", policy =>
                //{
                //    policy.RequireRole(UserRoles.Admin);
                //    policy.RequireRole(UserRoles.HR);
                //});

                ////options.AddPolicy(CustomPolicy.Policy, policy =>
                ////{
                ////         policy.RequireRole(CustomPolicyRole.PolicyRole);
                ////});


            });
        }




        public static Logger AddCustomSerilog(this IServiceCollection services, string LogConnection, string SeqConnection)
        {



            services.AddHttpLogging(logging =>
            {
                logging.LoggingFields = HttpLoggingFields.All;
                logging.RequestHeaders.Add("sec-ch-ua");
                logging.ResponseHeaders.Add("Interview.API");
                logging.MediaTypeOptions.AddText("application/javascript");
                logging.RequestBodyLogLimit = 40960;
                logging.ResponseBodyLogLimit = 40960;

            });


            var fileName = "log.txt";
            var webRootPath = Path.Combine(Directory.GetCurrentDirectory(), "wwwroot");
            var logDirectory = Path.Combine(webRootPath, "logs");
            var logFilePath = Path.Combine(logDirectory, fileName);



            Logger log = new LoggerConfiguration()
                .WriteTo.Console()
                .WriteTo.File(logFilePath)
                .WriteTo.PostgreSQL(LogConnection, "Logs",
                         needAutoCreateTable: true,
                         columnOptions: new Dictionary<string, ColumnWriterBase>
                         {
                             { "message", new RenderedMessageColumnWriter() },
                             { "message_template", new MessageTemplateColumnWriter() },
                             { "level", new LevelColumnWriter() },
                             { "time_stamp", new TimestampColumnWriter() },
                             { "exeptions", new ExceptionColumnWriter() },
                             { "log_event", new LogEventSerializedColumnWriter() },
                             { "user_name", new UsernameColumnWriter() },
                             { "machine_name", new MachinenameColumnWriter() },
                         })
                .WriteTo.Seq(SeqConnection, restrictedToMinimumLevel: LogEventLevel.Information)
                .Enrich.FromLogContext()
                .MinimumLevel.Information()
                .CreateLogger();




            return log;
        }

        public static void AddRateLimiterServiceExtension(this IServiceCollection services)
        {
            services.AddRateLimiter(options =>
            {
                options.GlobalLimiter = PartitionedRateLimiter.Create<HttpContext, string>(httpContext =>
                {

                    if (httpContext.Request.Path.StartsWithSegments("/api/Auth/login"))
                    {
                        return RateLimitPartition.GetFixedWindowLimiter(partitionKey: httpContext.Request.Headers.Host.ToString(), partition =>
                            new FixedWindowRateLimiterOptions
                            {
                                PermitLimit = 1,
                                AutoReplenishment = true,
                                Window = TimeSpan.FromSeconds(1)
                            });
                    }



                    return RateLimitPartition.GetFixedWindowLimiter(partitionKey: "default", partition =>
                        new FixedWindowRateLimiterOptions
                        {
                            PermitLimit = int.MaxValue,
                            AutoReplenishment = true,
                            Window = TimeSpan.FromSeconds(1)
                        });
                });

                options.OnRejected = async (context, token) =>
                {
                    context.HttpContext.Response.StatusCode = 429;
                    await context.HttpContext.Response.WriteAsync("Too many requests. Please try again later... ", cancellationToken: token);
                };
            });
        }
    }
}

