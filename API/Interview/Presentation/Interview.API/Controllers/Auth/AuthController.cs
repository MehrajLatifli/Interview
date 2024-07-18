using AutoMapper;
using Azure.Storage.Blobs;
using Azure.Storage.Blobs.Models;
using Interview.Domain.Entities.AuthModels;
using Interview.Domain.Entities.Models;
using Interview.Persistence.ServiceExtensions;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using Newtonsoft.Json;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;
using Interview.Application.Services.Abstract;
using Microsoft.IdentityModel.Claims;
using Interview.API.API_Routes;
using Interview.Application.Validations;
using Interview.Application.Validations.Interview.Application.Validations;
using Interview.Domain.Entities.Requests;
using Interview.Persistence.Contexts.InterviewDbContext;
using Microsoft.EntityFrameworkCore;
using Interview.Persistence.SqlQueries;
using Interview.Application.Mapper.DTO.AuthDTO;
using Asp.Versioning;

namespace Interview.API.Controllers.Auth
{

    [ApiVersion(1, Deprecated = true)]
    [Route("api/v{version:apiVersion}/[controller]")]
    [ApiController]
    public class AuthController : ControllerBase
    {

        public readonly IMapper _mapper;
        private readonly IAuthService _authservice;
        private readonly InterviewContext _interviewContext;


        public AuthController(IMapper mapper, IAuthService authservice, InterviewContext interviewContext)
        {
            _mapper = mapper;
            _authservice = authservice;
            _interviewContext = interviewContext;
        }


        [HttpGet]
        [Route(Routes.GetAdmins)]
        [Authorize]
        public async Task<IActionResult> GetAdmins()
        {
            return Ok(await _authservice.GetAdmins(User));


        }

        [HttpGet]
        [Route(Routes.GetHR)]
        [Authorize]
        public async Task<IActionResult> GetHRs()
        {
            return Ok(await _authservice.GetHRs(User));


        }




        [HttpPost]
        [Route(Routes.RegisterAdmin)]
        public async Task<IActionResult> RegisterAdmin([FromForm] RegisterAdminDTO model)
        {

            await _authservice.RegisterAdmin(model, ServiceExtension.ConnectionStringAzure);


            return Ok(new Response { Status = "Success", Message = "User created successfully!" });
        }


        [HttpPost]
        [Route(Routes.RegisterHR)]
        public async Task<IActionResult> RegisterHR([FromForm] RegisterAdminDTO model)
        {

            await _authservice.RegisterHR(model, ServiceExtension.ConnectionStringAzure);


            return Ok(new Response { Status = "Success", Message = "User created successfully!" });
        }


        [HttpGet]
        [Route(Routes.Profile)]
        [Authorize]
        public async Task<IActionResult> Profile()
        {
            return Ok(await _authservice.Profile(User));



        }


        [HttpPost]
        [Route(Routes.Logout)]
        public async Task<IActionResult> Logout()
        {
            await _authservice.Logout(User);

            return NoContent();
        }


        [HttpPost]
        [Route(Routes.Login)]
        public async Task<IActionResult> Login([FromBody] LoginDTO model)
        {


            return Ok(await _authservice.Login(model));

        }

        [HttpPost]
        [Route(Routes.RefreshToken)]
        public async Task<IActionResult> RefreshToken(TokenModel tokenModel)
        {
            Console.WriteLine(tokenModel);
            return Ok(await _authservice.RefreshToken(tokenModel, User));

        }


        [HttpGet]
        [Route(Routes.GetMehtods)]
        [Authorize]
        public async Task<IActionResult> GetMehtods()
        {
            return Ok(await _authservice.GetMehtods(User));



        }

        [HttpGet]
        [Route(Routes.GetUserAccess)]
        [Authorize]
        public async Task<IActionResult> GetUserAccess()
        {
            return Ok(await _authservice.GetUserAccess(User));


        }


        [HttpPost]
        [Route(Routes.addUser)]
        [Authorize]
        public async Task<IActionResult> AddUser([FromForm] RegisterDTO model)
        {

            await _authservice.AddUser(model, ServiceExtension.ConnectionStringAzure);


            return Ok(new Response { Status = "Success", Message = "User created successfully!" });
        }



        [HttpPost]
        [Route(Routes.addRole)]
        [Authorize]
        public async Task<IActionResult> AddRole([FromBody] CreateRoleRequestModel model)
        {
            await _authservice.AddRole(model.RoleName, User);


            return Ok(new Response { Status = "Success", Message = "Role successfully created!" });
        }

        [HttpPost]
        [Route(Routes.addRoleClaim)]
        [Authorize]
        public async Task<IActionResult> AddRoleClaim([FromBody] CreateRoleClaimRequestModel model)
        {
            await _authservice.AddRoleClaim(model.RoleId,model.RoleAccessMethodId, User);


            return Ok(new Response { Status = "Success", Message = "RoleClaim successfully created!" });
        }

        [HttpPost]
        [Route(Routes.addUserClaim)]
        [Authorize]
        public async Task<IActionResult> AddUserClaim([FromBody] CreateUserClaimRequestModel model)
        {
            await _authservice.AddUserClaim(model.UserId, model.UserAccessId, User);


            return Ok(new Response { Status = "Success", Message = "UserClaim successfully created!" });
        }

        [HttpPost]
        [Route(Routes.addUserRole)]
        [Authorize]
        public async Task<IActionResult> AddUserRole([FromBody] CreateUserRoleRequestModel model)
        {
            await _authservice.AddUserRole(model.UserId, model.RoleId, User);


            return Ok(new Response { Status = "Success", Message = "UserRole successfully created!" });
        }

    }
}
