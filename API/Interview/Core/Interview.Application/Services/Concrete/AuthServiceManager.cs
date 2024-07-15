using Interview.Application.Mapper.DTO;
using Interview.Application.Services.Abstract;
using Interview.Domain.Entities.AuthModels;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using AutoMapper;
using Microsoft.Extensions.Configuration;
using Interview.Application.Exception;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using Microsoft.IdentityModel.Tokens;
using System.Security.Cryptography;
using Interview.Domain.Entities.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Build.Framework;
using Microsoft.Extensions.Logging;
using System.Net;
using Interview.Application.Repositories.Custom;
using System.Data;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Authorization;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.AspNetCore.Authorization.Infrastructure;
using System.Runtime.ConstrainedExecution;
using System.IO;
using Microsoft.AspNetCore.Http.HttpResults;
using NuGet.Packaging;
using System.Collections;
using Interview.Domain.Entities.IdentityAuth;
using Microsoft.AspNetCore.Cryptography.KeyDerivation;
using System.Linq.Dynamic.Core;
using Elfie.Serialization;
using Humanizer.Localisation;
using Interview.Application.Mapper.DTO.AuthDTO;
using Microsoft.Extensions.Caching.Memory;
using Microsoft.AspNetCore.Routing;
using Microsoft.AspNetCore.Components.Routing;
using Microsoft.CodeAnalysis.Elfie.Serialization;
using static System.Runtime.InteropServices.JavaScript.JSType;
using Interview.Application.Mapper.DTO.CandidateDTO;
using Interview.Application.Mapper.DTO.UserRoleDTO;
using Interview.Application.Mapper.DTO.RoleDTO;
using Interview.Application.Mapper.DTO.UserDTO;
using Interview.Application.Mapper.DTO.RoleClaimDTO;
using Interview.Application.Mapper.DTO.UserClaimDTO;
using Interview.Domain.Entities.Others;
using Azure.Storage.Blobs.Models;
using Azure.Storage.Blobs;

namespace Interview.Application.Services.Concrete
{
    public class AuthServiceManager : IAuthService
    {

        private readonly IConfiguration _configuration;
        public readonly IMapper _mapper;
        readonly ILogger<AuthServiceManager> _logger;
        private readonly IUserWriteRepository _userWriteRepository;
        private readonly IUserReadRepository _userReadRepository;
        private readonly IUserClaimWriteRepository _userClaimWriteRepository;
        private readonly IUserClaimReadRepository _userClaimReadRepository;
        private readonly IRoleWriteRepository _roleWriteRepository;
        private readonly IRoleReadRepository _roleReadRepository;
        private readonly IRoleClaimWriteRepository _roleClaimWriteRepository;
        private readonly IRoleClaimReadRepository _roleClaimReadRepository;
        private readonly IUserRoleWriteRepository _userRoleWriteRepository;
        private readonly IUserRoleReadRepository _userRoleReadRepository;
        private readonly IMemoryCache _memoryCache;

        public AuthServiceManager(IConfiguration configuration, IMapper mapper, ILogger<AuthServiceManager> logger, IUserWriteRepository userWriteRepository, IUserReadRepository userReadRepository, IUserClaimWriteRepository userClaimWriteRepository, IUserClaimReadRepository userClaimReadRepository, IRoleWriteRepository roleWriteRepository, IRoleReadRepository roleReadRepository, IRoleClaimWriteRepository roleClaimWriteRepository, IRoleClaimReadRepository roleClaimReadRepository, IUserRoleWriteRepository userRoleWriteRepository, IUserRoleReadRepository userRoleReadRepository, IMemoryCache memoryCache)
        {
            _configuration = configuration;
            _mapper = mapper;
            _logger = logger;
            _userWriteRepository = userWriteRepository;
            _userReadRepository = userReadRepository;
            _userClaimWriteRepository = userClaimWriteRepository;
            _userClaimReadRepository = userClaimReadRepository;
            _roleWriteRepository = roleWriteRepository;
            _roleReadRepository = roleReadRepository;
            _roleClaimWriteRepository = roleClaimWriteRepository;
            _roleClaimReadRepository = roleClaimReadRepository;
            _userRoleWriteRepository = userRoleWriteRepository;
            _userRoleReadRepository = userRoleReadRepository;
            _memoryCache = memoryCache;
        }

        public async Task<List<UserAccessDTO>> GetUserAccess(ClaimsPrincipal claimsPrincipal)
        {

            return await UserAccessAsync();

        }


        public async Task<List<RoleAccessMethodDTO>> GetMehtods(ClaimsPrincipal claimsPrincipal)
        {


            //CustomPolicy.Policy = userRoles.FirstOrDefault();
            //CustomPolicyRole.PolicyRole = userRoles.FirstOrDefault();

            return await MethodsAsync();

        }


        public async Task<GetAuthDTOModel> Profile(ClaimsPrincipal claimsPrincipal)
        {
            if (claimsPrincipal.Identity.IsAuthenticated)
            {
                var currentUser = claimsPrincipal.Identity.Name;

                var users = _mapper.Map<List<UserDTOforGetandGetAll>>(_userReadRepository.GetAll(false));
                var roles = _mapper.Map<List<RoleDTOforGetandGetAll>>(_roleReadRepository.GetAll(false));
                var userclaims = _mapper.Map<List<UserClaimDTOforGetandGetAll>>(_userClaimReadRepository.GetAll(false));
                var userroles = _mapper.Map<List<UserRoleDTOforGetandGetAll>>(_userRoleReadRepository.GetAll(false));
                var roleclaims = _mapper.Map<List<RoleClaimDTOforGetandGetAll>>(_roleClaimReadRepository.GetAll(false)).Distinct().ToList();

                var currentUserDTO = users.FirstOrDefault(u => u.UserName == currentUser);
                if (currentUserDTO == null)
                {
                    throw new NotFoundException("User not found.");
                }

                var userRolesDTO = userroles.Where(ur => ur.UserId == currentUserDTO.Id).ToList();
                var userClaimsDTO = userclaims.Where(uc => uc.UserId == currentUserDTO.Id).ToList();
                var userRoleIds = userRolesDTO.Select(ur => ur.RoleId).ToList();
                var userRoles = roles.Where(r => userRoleIds.Contains(r.Id)).ToList();

                var permissionsDTO = new List<PermitionsDTOModel>
        
                {
            
                    new PermitionsDTOModel
                    {
                
                        UserClaims = userClaimsDTO,
               
                        RoleClaims = (from ur in userRolesDTO
                                      join r in roles on ur.RoleId equals r.Id
                                      join rc in roleclaims on r.Id equals rc.RoleId
                                      select new RoleClaimDTOforGetandGetAll
                                      {
                                          Id = rc.Id,
                                          RoleId = rc.RoleId,
                                          ClaimType = rc.ClaimType,
                                          ClaimValue = rc.ClaimValue
                                      } ).TakeLast(1).Distinct().ToList(),
                        
                        Roles = userRoles
            
                    }
        
                };

                var userProfile = new GetAuthDTOModel
                {
                    Id = currentUserDTO.Id.ToString(),
                    Username = currentUserDTO.UserName,
                    Email = currentUserDTO.Email,
                    PhoneNumber = currentUserDTO.Phonenumber,
                    ImagePath = currentUserDTO.ImagePath,
                    Permitions = permissionsDTO
                };

                return userProfile;
            }
            else
            {
                throw new UnauthorizedException("Current user is not authenticated.");
            }
        }



        public async Task<List<GetAuthDTOModel>> GetAdmins(ClaimsPrincipal claimsPrincipal)
        {
            if (claimsPrincipal.Identity.IsAuthenticated)
            {

                var list = new List<GetAuthDTOModel>();

                var userroles = _mapper.Map<List<UserRoleDTOforGetandGetAll>>(_userRoleReadRepository.GetAll(false));
                var users = _mapper.Map<List<UserDTOforGetandGetAll>>(_userReadRepository.GetAll(false));
                var roles = _mapper.Map<List<RoleDTOforGetandGetAll>>(_roleReadRepository.GetAll(false));
                var roleclaims = _mapper.Map<List<RoleClaimDTOforGetandGetAll>>(_roleClaimReadRepository.GetAll(false));
                var userclaims = _mapper.Map<List<UserClaimDTOforGetandGetAll>>(_userClaimReadRepository.GetAll(false));


                var currentUser = claimsPrincipal.Identity.Name;

                if (users.Any(i => i.UserName == currentUser))
                {
                    var hascrateRole = roles.FirstOrDefault(i => i.Name == UserRoles.Admin);

                    if (hascrateRole == null)
                    {
                        throw new NotFoundException("No such role has been created.");
                    }

                    if (roles.Any(i => i.Name == UserRoles.Admin))
                    {


                        if (userroles.Any(i => i.UserId == users.Where(i => i.UserName == currentUser).FirstOrDefault().Id && (i.RoleId == roles.Where(i => i.Name == UserRoles.Admin).FirstOrDefault().Id)))
                        {
                            if (userclaims.Any(i => (i.ClaimType == UserAccess.Read_ClaimType || i.ClaimType == UserAccess.AllAccess_ClaimType) && (i.UserId == users.Where(i => i.UserName == currentUser).FirstOrDefault().Id)))
                            {
                                if (roleclaims.Any(i => i.ClaimType == RoleAccessMethodType.Get_Method && i.ClaimValue == RoleAccessMethod.GetAdmins_Method && i.RoleId == roles.Where(i => i.Name == UserRoles.Admin).FirstOrDefault().Id))
                                {
                                    if (roles.FirstOrDefault(i => i.Name == UserRoles.Admin) == null)
                                    {
                                        throw new ForbiddenException("No such role exists");
                                    }


                                    foreach (var item in userroles.Where(i => i.RoleId == roles.Where(i => i.Name == UserRoles.Admin).FirstOrDefault().Id))
                                    {

                                        var rolesbyUser = userroles.Where(i => i.UserId == item.UserId).ToList().Select(userRole => roles.FirstOrDefault(role => role.Id == userRole.RoleId)).ToList();

                                        list.Add(new GetAuthDTOModel()
                                        {
                                            Id = _userReadRepository.GetAll(false).Where(i => i.Id == item.UserId).FirstOrDefault().Id.ToString(),
                                            Username = _userReadRepository.GetAll(false).Where(i => i.Id == item.UserId).FirstOrDefault().UserName,
                                            PhoneNumber = _userReadRepository.GetAll(false).Where(i => i.Id == item.UserId).FirstOrDefault().Phonenumber,
                                            Email = _userReadRepository.GetAll(false).Where(i => i.Id == item.UserId).FirstOrDefault().Email,
                                            ImagePath = _userReadRepository.GetAll(false).Where(i => i.Id == item.UserId).FirstOrDefault().ImagePath,
                                            Permitions = new List<PermitionsDTOModel>
                                            {
                                                 new PermitionsDTOModel
                                                 {
                                                    UserClaims = userclaims.Where(i=>i.UserId==item.UserId).ToList(),
                                                    RoleClaims = roleclaims.Where(i=>i.RoleId==item.RoleId).ToList(),
                                                    Roles = rolesbyUser,

                                                 }

                                            }

                                        
                                        });
                                    }

                                    if (list.Any())
                                    {
                                        return list.Distinct(new GetAuthModelComparer()).ToList().OrderBy(i => i.Id).ToList();
                                    }
                                    else
                                    {
                                        throw new NotFoundException("No users found!");
                                    }
                                }
                                else
                                {
                                    throw new ForbiddenException("You cannot access due to role permissions.");
                                }
                            }
                            else
                            {
                                throw new ForbiddenException("You cannot access due to user permissions.");
                            }




                        }
                        else
                        {
                            throw new ForbiddenException("You cannot log in due to a UserRole mismatch.");
                        }
                    }
                    else
                    {
                        throw new ForbiddenException("Access is denied with the user's existing role.");
                    }
                }
                else
                {
                    throw new ForbiddenException("You cannot access");
                }



            }
            else
            {
                throw new UnauthorizedException("Current user is not authenticated.");
            }

        }

        public async Task<List<GetAuthDTOModel>> GetHRs(ClaimsPrincipal claimsPrincipal)
        {
            if (claimsPrincipal.Identity.IsAuthenticated)
            {

             //   var list = new List<GetAuthDTOModel>();

                var userroles = _mapper.Map<List<UserRoleDTOforGetandGetAll>>(_userRoleReadRepository.GetAll(false));
                var users = _mapper.Map<List<UserDTOforGetandGetAll>>(_userReadRepository.GetAll(false));
                var roles = _mapper.Map<List<RoleDTOforGetandGetAll>>(_roleReadRepository.GetAll(false));
               var userclaims = _mapper.Map<List<UserClaimDTOforGetandGetAll>>(_userClaimReadRepository.GetAll(false));

                var roleclaims = _mapper.Map<List<RoleClaimDTOforGetandGetAll>>(_roleClaimReadRepository.GetAll(false)).Distinct().ToList();

               

                var currentUser = claimsPrincipal.Identity.Name;

                if (users.Any(i => i.UserName == currentUser))
                {
                        var hascrateRole = roles.FirstOrDefault(i => i.Name == UserRoles.HR);

                        if (hascrateRole == null)
                        {
                            throw new NotFoundException("No such role has been created.");
                        }

                    if (roles.Any(i => i.Name == UserRoles.HR || i.Name == UserRoles.Admin))
                    {
                        if (userroles.Any(i => i.UserId == users.Where(i => i.UserName == currentUser).FirstOrDefault().Id && (i.RoleId == roles.Where(i => i.Name == UserRoles.HR).FirstOrDefault().Id || i.RoleId == roles.Where(i => i.Name == UserRoles.Admin).FirstOrDefault().Id)))
                        {
                            if (userclaims.Any(i => (i.ClaimType == UserAccess.Read_ClaimType || i.ClaimType == UserAccess.AllAccess_ClaimType) && (i.UserId == users.Where(i => i.UserName == currentUser).FirstOrDefault().Id)))
                            {
                                if (roleclaims.Any(i => i.ClaimType == RoleAccessMethodType.Get_Method && i.ClaimValue == RoleAccessMethod.GetHR_Method && i.RoleId == roles.Where(i => i.Name == UserRoles.HR).FirstOrDefault().Id))
                                {
                                    if (roles.FirstOrDefault(i => i.Name == UserRoles.HR) == null || roles.FirstOrDefault(i => i.Name == UserRoles.Admin) == null)
                                    {
                                        throw new ForbiddenException("No such role exists");
                                    }


                                    var query = from u in users
                                                join ur in userroles on u.Id equals ur.UserId
                                                join r in roles on ur.RoleId equals r.Id
                                                join rc in roleclaims on r.Id equals rc.RoleId
                                                select new GetAuthDTOModel
                                                {
                                                    Id = u.Id.ToString(),
                                                    Username = u.UserName,
                                                    Email = u.Email,
                                                    PhoneNumber = u.Phonenumber,
                                                    ImagePath = u.ImagePath,
                                                    Permitions = new List<PermitionsDTOModel>
                                                    {

                                  
                                                        new PermitionsDTOModel
                                                        {

                                      
                                                            UserClaims = userclaims
                                                            .Where(uc => uc.UserId == u.Id)
                                                            .ToList(),


                                                              RoleClaims = (
                       
                                                              from ur in userroles
                                                              join role in roles on ur.RoleId equals role.Id
                                                              join rc_ in roleclaims on role.Id equals rc_.RoleId
                                                              where ur.UserId == u.Id
                                                              select new RoleClaimDTOforGetandGetAll
                                                              {
                                                                  Id=rc_.Id,
                                                                  RoleId= rc_.RoleId,
                                                                  ClaimType = rc_.ClaimType,
                                                                  ClaimValue = rc_.ClaimValue
                                                              }
                                                              ).TakeLast(1).Distinct().ToList(),


                                                            Roles = userroles.Where(ur => ur.UserId == u.Id).Select(ur => roles.FirstOrDefault(role => role.Id == ur.RoleId)).ToList()

                                    
                                                        }

                                
                                                    }
                                                };


                                    var hrUsersDto = new List<GetAuthDTOModel>();


                                    hrUsersDto = query.ToList();

                                    if (hrUsersDto.Any())
                                    {
                                        return hrUsersDto.Distinct(new GetAuthModelComparer()).ToList().OrderBy(i => i.Id).ToList();
                                    }
                                    else
                                    {
                                        throw new NotFoundException("No users found!");
                                    }
                                }
                                else
                                {
                                    throw new ForbiddenException("You cannot access due to role permissions.");
                                }
                            }
                            else
                            {
                                throw new ForbiddenException("You cannot access due to user permissions.");
                            }




                        }
                        else
                        {
                            throw new ForbiddenException("You cannot log in due to a UserRole mismatch.");
                        }
                    }
                    else
                    {
                        throw new ForbiddenException("Access is denied with the user's existing role.");
                    }
                }
                else
                {
                    throw new ForbiddenException("You cannot access");
                }



            }
            else
            {
                throw new UnauthorizedException("Current user is not authenticated.");
            }
        }


        public async Task AddRole(string roleName, ClaimsPrincipal claimsPrincipal)
        {

            if (claimsPrincipal.Identity.IsAuthenticated)
            {


                var roleExists = _roleReadRepository.GetAll(false).AsEnumerable().Any(i => i.Name == roleName);

                if (!roleExists)
                {
                    var newRole = new Role
                    {
                        Name = roleName,
                        ConcurrencyStamp = Guid.NewGuid().ToString(),

                    };

                    await _roleWriteRepository.AddAsync(newRole);

                    var result = await _roleWriteRepository.SaveAsync();

                    if (result == -1)
                    {
                        throw new InvalidOperationException("Failed to create the role.");
                    }



                }
                else
                {
                    throw new ConflictException("Role already exists");
                }




            }
            else
            {
                throw new UnauthorizedException("Current user is not authenticated.");
            }
        }

        public async Task AddUserRole(string userId, string roleId, ClaimsPrincipal claimsPrincipal)
        {

            if (claimsPrincipal.Identity.IsAuthenticated)
            {


                var roleExists = _roleReadRepository.GetAll(false).AsEnumerable().Any(i => i.Id == Convert.ToInt32(roleId));

                var userExists = _userReadRepository.GetAll(false).AsEnumerable().Any(i => i.Id == Convert.ToInt32(userId));

                if (roleExists && userExists)
                {

                    var userroleExists = _userRoleReadRepository.GetAll(false).AsEnumerable().Any(i => i.RoleId == Convert.ToInt32(roleId) && i.UserId == Convert.ToInt32(userId));



                    if (!userroleExists)
                    {
                        var userRole = new UserRole
                        {
                            UserId = Convert.ToInt32(userId),
                            RoleId = Convert.ToInt32(roleId),

                        };

                        await _userRoleWriteRepository.AddAsync(userRole);

                        var result = await _roleWriteRepository.SaveAsync();

                        if (result == -1)
                        {
                            throw new InvalidOperationException("Failed to create the UserRole.");
                        }
                    }

                    if (userroleExists)
                    {
                        throw new ConflictException("UserRole already exists.");
                    }


                }
                if (!userExists)
                {
                    throw new ConflictException("User does not exist.");
                }
                if (!roleExists)
                {
                    throw new ConflictException("Role does not exist.");
                }




            }
            else
            {
                throw new UnauthorizedException("Current user is not authenticated.");
            }
        }


        public async Task AddRoleClaim(string roleId, string roleAccessMethodId, ClaimsPrincipal claimsPrincipal)
        {

            if (claimsPrincipal.Identity.IsAuthenticated)
            {


                var roleAccessMethods = await MethodsAsync();

                if (!roleAccessMethods.Any(i => i.Id == Convert.ToInt32(roleAccessMethodId)))
                {
                    throw new NotFoundException("roleAccessMethodId Not Found!");
                }

                var roleClaimExists = _roleClaimReadRepository.GetAll(false).AsEnumerable().Any(i => i.RoleId == Convert.ToInt32(roleId) && i.ClaimType == roleAccessMethods.Where(i => i.Id == Convert.ToInt32(roleAccessMethodId)).FirstOrDefault().MethodType && i.ClaimValue == roleAccessMethods.Where(i => i.Id == Convert.ToInt32(roleAccessMethodId)).FirstOrDefault().Method);

                if (!roleClaimExists)
                {

                    var roleClaims = new List<RoleClaim>();


                    var roleExist = _roleReadRepository.GetAll(false).Any(i => i.Id == Convert.ToInt32(roleId));

                    if (roleExist)
                    {



                        roleClaims.Add(new RoleClaim()
                        {
                            ClaimType = roleAccessMethods.Where(i => i.Id == Convert.ToInt32(roleAccessMethodId)).FirstOrDefault().MethodType,
                            ClaimValue = roleAccessMethods.Where(i => i.Id == Convert.ToInt32(roleAccessMethodId)).FirstOrDefault().Method,
                            RoleId = Convert.ToInt32(roleId),
                        });



                        //await _roleManager.AddClaimAsync(role, claim);


                        await _roleClaimWriteRepository.AddRangeAsync(roleClaims);

                        var result2 = await _roleClaimWriteRepository.SaveAsync();

                        if (result2 == -1)
                        {
                            throw new InvalidOperationException("Failed to create the RoleClaim.");
                        }



                    }
                    else
                    {
                        throw new NotFoundException("roleId Not Found!");
                    }

                }
                else
                {
                    throw new ConflictException("RoleClaim already exists");
                }



            }
            else
            {
                throw new UnauthorizedException("Current user is not authenticated.");
            }
        }

        public async Task AddUserClaim(string userId, string userAccessId, ClaimsPrincipal claimsPrincipal)
        {

            if (claimsPrincipal.Identity.IsAuthenticated)
            {
                var userAccesses = await UserAccessAsync();



                if (!userAccesses.Any(i => i.Id == Convert.ToInt32(userAccessId)))
                {
                    throw new NotFoundException("userAccessId Not Found!");
                }

                var userClaimExists = _userClaimReadRepository.GetAll(false).AsEnumerable().Any(i => i.UserId == Convert.ToInt32(userId) && i.ClaimType == userAccesses.Where(i => i.Id == Convert.ToInt32(userAccessId)).FirstOrDefault().UserAccess && i.ClaimValue == userAccesses.Where(i => i.Id == Convert.ToInt32(userAccessId)).FirstOrDefault().UserAccessDescription);

                if (!userClaimExists)
                {

                    var userClaims = new List<UserClaim>();


                    var usersExists = _userReadRepository.GetAll(false).Any(i => i.Id == Convert.ToInt32(userId));

                    if (usersExists)
                    {



                        userClaims.Add(new UserClaim()
                        {
                            ClaimType = userAccesses.Where(i => i.Id == Convert.ToInt32(userAccessId)).FirstOrDefault().UserAccess,
                            ClaimValue = userAccesses.Where(i => i.Id == Convert.ToInt32(userAccessId)).FirstOrDefault().UserAccessDescription,
                            UserId = Convert.ToInt32(userId),
                        });


                        await _userClaimWriteRepository.AddRangeAsync(userClaims);

                        var result2 = await _userClaimWriteRepository.SaveAsync();

                        if (result2 == -1)
                        {
                            throw new InvalidOperationException("Failed to create the UserClaim.");
                        }



                    }
                    else
                    {
                        throw new NotFoundException("userId Not Found!");
                    }
                }
                else
                {
                    throw new ConflictException("UserClaim already exists");
                }




            }
            else
            {
                throw new UnauthorizedException("Current user is not authenticated.");
            }
        }


        public async Task AddUser(RegisterDTO model, string ConnectionStringAzure)
        {


            var entity = _mapper.Map<Register>(model);

            var userExists = _userReadRepository.GetAll(false).AsEnumerable().Any(i => i.UserName == model.Username);

            if (userExists == true)
            {
                throw new ConflictException("User already exists!");
            }

            string connectionString = ConnectionStringAzure;

            string azuriteConnectionString = Environment.GetEnvironmentVariable("CUSTOMCONNSTR_AZURE_STORAGE_CONNECTION_STRING");
            if (!string.IsNullOrEmpty(azuriteConnectionString))
            {
                connectionString = azuriteConnectionString;
            }

            string containerName = "profile-images";
            string blobName = entity.Username + "_" + Guid.NewGuid() + Path.GetExtension(entity.ImagePath.FileName);

            // Set content type and disposition based on file extension
            var blobHttpHeaders = new BlobHttpHeaders
            {
                ContentType = GetContentType(Path.GetExtension(entity.ImagePath.FileName)),
                ContentDisposition = "inline" // Ensures the image is viewable in the browser
            };

            // Create a BlobServiceClient and get a reference to the container
            var blobServiceClient = new BlobServiceClient(connectionString);
            var containerClient = blobServiceClient.GetBlobContainerClient(containerName);
            await containerClient.CreateIfNotExistsAsync(PublicAccessType.Blob);

            // Get a reference to the blob and upload the image with headers
            var blobClient = containerClient.GetBlobClient(blobName);
            using (var stream = entity.ImagePath.OpenReadStream())
            {
                await blobClient.UploadAsync(stream, new BlobUploadOptions
                {
                    HttpHeaders = blobHttpHeaders
                });
            }

            string imageUrl = blobClient.Uri.ToString();


            User user = new()
            {
                UserName = entity.Username,
                Email = entity.Email,
                Password = PasswordComputeHash(model.Password, Environment.GetEnvironmentVariable("Salt")),
                Phonenumber = entity.PhoneNumber,
                ConcurrencyStamp = Guid.NewGuid().ToString(),
                ImagePath = imageUrl,

            };



            await _userWriteRepository.AddAsync(user);

            await _userWriteRepository.SaveAsync();







            System.Globalization.CultureInfo.CurrentCulture.ClearCachedData();

            TimeZone localZone = TimeZone.CurrentTimeZone;
            DateTime localTime = localZone.ToLocalTime(DateTime.UtcNow);


        }

        private string GetContentType(string extension)
        {
            return extension.ToLower() switch
            {
                ".png" => "image/png",
                ".jpg" or ".jpeg" => "image/jpeg",
                ".gif" => "image/gif",
                _ => "application/octet-stream"
            };
        }

        public async Task RegisterAdmin(RegisterAdminDTO model, string ConnectionStringAzure)
        {
            var userAccesses = await UserAccessAsync();


            var roleAccessMethods = await MethodsAsync();



            var entity = _mapper.Map<Register>(model);

            var userExists = _userReadRepository.GetAll(false).AsEnumerable().Any(i => i.UserName == model.Username);

            if (userExists == true)
            {
                throw new ConflictException("User already exists!");
            }

            string connectionString = ConnectionStringAzure;

            string azuriteConnectionString = Environment.GetEnvironmentVariable("CUSTOMCONNSTR_AZURE_STORAGE_CONNECTION_STRING");
            if (!string.IsNullOrEmpty(azuriteConnectionString))
            {
                connectionString = azuriteConnectionString;
            }

            string containerName = "profile-images";
            string blobName = entity.Username + "_" + Guid.NewGuid() + Path.GetExtension(entity.ImagePath.FileName);

            // Set content type and disposition based on file extension
            var blobHttpHeaders = new BlobHttpHeaders
            {
                ContentType = GetContentType(Path.GetExtension(entity.ImagePath.FileName)),
                ContentDisposition = "inline" // Ensures the image is viewable in the browser
            };

            // Create a BlobServiceClient and get a reference to the container
            var blobServiceClient = new BlobServiceClient(connectionString);
            var containerClient = blobServiceClient.GetBlobContainerClient(containerName);
            await containerClient.CreateIfNotExistsAsync(PublicAccessType.Blob);

            // Get a reference to the blob and upload the image with headers
            var blobClient = containerClient.GetBlobClient(blobName);
            using (var stream = entity.ImagePath.OpenReadStream())
            {
                await blobClient.UploadAsync(stream, new BlobUploadOptions
                {
                    HttpHeaders = blobHttpHeaders
                });
            }

            string imageUrl = blobClient.Uri.ToString();


            User user = new()
            {
                UserName = entity.Username,
                Email = entity.Email,
                Password = PasswordComputeHash(model.Password, Environment.GetEnvironmentVariable("Salt")),
                Phonenumber = entity.PhoneNumber,
                ConcurrencyStamp = Guid.NewGuid().ToString(),
                ImagePath = imageUrl,

            };


            var roleExists = _roleReadRepository.GetAll(false).AsEnumerable().Any(i => i.Name == UserRoles.Admin);




            if (!roleExists)
            {

                await _userWriteRepository.AddAsync(user);

                await _userWriteRepository.SaveAsync();

                var result1 = await _roleWriteRepository.SaveAsync();

                if (result1 == -1)
                {
                    throw new InvalidOperationException("Failed to create the user.");
                }



                var newRole = new Role
                {
                    Name = UserRoles.Admin,
                    ConcurrencyStamp = Guid.NewGuid().ToString(),

                };



                await _roleWriteRepository.AddAsync(newRole);

                var result = await _roleWriteRepository.SaveAsync();

                if (result == -1)
                {
                    throw new InvalidOperationException("Failed to create the role.");
                }



            }
            else
            {
                throw new ConflictException("Role already exists");
            }




            var userRole = new UserRole();

            var roleExists2 = _roleReadRepository.GetAll(false).AsEnumerable().Any(i => i.Name == UserRoles.Admin);

            var userExists2 = _userReadRepository.GetAll(false).AsEnumerable().Any(i => i.UserName == user.UserName);

            if (roleExists2 && userExists2)
            {


                userRole = new UserRole
                {
                    UserId = _userReadRepository.GetAll(false).AsEnumerable().Where(i => i.UserName == user.UserName).FirstOrDefault().Id,
                    RoleId = _roleReadRepository.GetAll(false).AsEnumerable().Where(i => i.Name == UserRoles.Admin).FirstOrDefault().Id,

                };

                await _userRoleWriteRepository.AddAsync(userRole);

                var result3 = await _roleWriteRepository.SaveAsync();

                if (result3 == -1)
                {
                    throw new InvalidOperationException("Failed to create the UserRole.");
                }



            }
            if (!userExists2)
            {
                throw new ConflictException("User does not exist.");
            }
            if (!roleExists2)
            {
                throw new ConflictException("Role does not exist.");
            }

            var roleClaims = new List<RoleClaim>();

            var methods = await MethodsAsync();





            var role = _roleReadRepository.GetAll(false).Where(i => i.Id == _roleReadRepository.GetAll(false).AsEnumerable().Where(i => i.Name == UserRoles.Admin).FirstOrDefault().Id).ToList();

            if (role != null)
            {


                foreach (var item in roleAccessMethods)
                {
                    roleClaims.Add(new RoleClaim()
                    {
                        ClaimType = methods.Where(i => i.Id == Convert.ToInt32(item.Id)).FirstOrDefault().MethodType,
                        ClaimValue = methods.Where(i => i.Id == Convert.ToInt32(item.Id)).FirstOrDefault().Method,
                        RoleId = _roleReadRepository.GetAll(false).AsEnumerable().Where(i => i.Name == UserRoles.Admin).FirstOrDefault().Id,
                    });
                }





                //await _roleManager.AddClaimAsync(role, claim);


                await _roleClaimWriteRepository.AddRangeAsync(roleClaims);

                var result4 = await _roleClaimWriteRepository.SaveAsync();

                if (result4 == -1)
                {
                    throw new InvalidOperationException("Failed to create the RoleClaim.");
                }



            }



            var userClaims = new List<UserClaim>();






            var users = _userReadRepository.GetAll(false).Where(i => i.Id == Convert.ToInt32(_userReadRepository.GetAll(false).AsEnumerable().Where(i => i.UserName == model.Username).FirstOrDefault().Id));

            if (users != null)
            {

                foreach (var item in userAccesses)
                {

                    userClaims.Add(new UserClaim()
                    {
                        ClaimType = userAccesses.Where(i => i.Id == Convert.ToInt32(item.Id)).FirstOrDefault().UserAccess,
                        ClaimValue = userAccesses.Where(i => i.Id == Convert.ToInt32(item.Id)).FirstOrDefault().UserAccessDescription,
                        UserId = Convert.ToInt32(_userReadRepository.GetAll(false).AsEnumerable().Where(i => i.UserName == model.Username).FirstOrDefault().Id),
                    });
                };


                await _userClaimWriteRepository.AddRangeAsync(userClaims);

                var result5 = await _userClaimWriteRepository.SaveAsync();

                if (result5 == -1)
                {
                    throw new InvalidOperationException("Failed to create the UserClaim.");
                }



            }




        }

        public async Task RegisterHR(RegisterAdminDTO model, string ConnectionStringAzure)
        {
            var userAccesses = await UserAccessAsync();


            var roleAccessMethods = await MethodsAsync();



            var entity = _mapper.Map<Register>(model);

            var userExists = _userReadRepository.GetAll(false).AsEnumerable().Any(i => i.UserName == model.Username);

            if (userExists == true)
            {
                throw new ConflictException("User already exists!");
            }

            string connectionString = ConnectionStringAzure;

            string azuriteConnectionString = Environment.GetEnvironmentVariable("CUSTOMCONNSTR_AZURE_STORAGE_CONNECTION_STRING");
            if (!string.IsNullOrEmpty(azuriteConnectionString))
            {
                connectionString = azuriteConnectionString;
            }

            string containerName = "profile-images";
            string blobName = entity.Username + "_" + Guid.NewGuid() + Path.GetExtension(entity.ImagePath.FileName);

            // Set content type and disposition based on file extension
            var blobHttpHeaders = new BlobHttpHeaders
            {
                ContentType = GetContentType(Path.GetExtension(entity.ImagePath.FileName)),
                ContentDisposition = "inline" // Ensures the image is viewable in the browser
            };

            // Create a BlobServiceClient and get a reference to the container
            var blobServiceClient = new BlobServiceClient(connectionString);
            var containerClient = blobServiceClient.GetBlobContainerClient(containerName);
            await containerClient.CreateIfNotExistsAsync(PublicAccessType.Blob);

            // Get a reference to the blob and upload the image with headers
            var blobClient = containerClient.GetBlobClient(blobName);
            using (var stream = entity.ImagePath.OpenReadStream())
            {
                await blobClient.UploadAsync(stream, new BlobUploadOptions
                {
                    HttpHeaders = blobHttpHeaders
                });
            }

            string imageUrl = blobClient.Uri.ToString();


            User user = new()
            {
                UserName = entity.Username,
                Email = entity.Email,
                Password = PasswordComputeHash(model.Password, Environment.GetEnvironmentVariable("Salt")),
                Phonenumber = entity.PhoneNumber,
                ConcurrencyStamp = Guid.NewGuid().ToString(),
                ImagePath = imageUrl,

            };


            var roleExists = _roleReadRepository.GetAll(false).AsEnumerable().Any(i => i.Name == UserRoles.HR);


            await _userWriteRepository.AddAsync(user);

            await _userWriteRepository.SaveAsync();

            var result1 = await _roleWriteRepository.SaveAsync();

            if (result1 == -1)
            {
                throw new InvalidOperationException("Failed to create the user.");
            }


            if (!roleExists)
            {


            



                var newRole = new Role
                {
                    Name = UserRoles.HR,
                    ConcurrencyStamp = Guid.NewGuid().ToString(),

                };



                await _roleWriteRepository.AddAsync(newRole);

                var result = await _roleWriteRepository.SaveAsync();

                if (result == -1)
                {
                    throw new InvalidOperationException("Failed to create the role.");
                }

            }






            var userRole = new UserRole();

            var roleExists2 = _roleReadRepository.GetAll(false).AsEnumerable().Any(i => i.Name == UserRoles.HR);

            var userExists2 = _userReadRepository.GetAll(false).AsEnumerable().Any(i => i.UserName == user.UserName);

            if (roleExists2 && userExists2)
            {


                userRole = new UserRole
                {
                    UserId = _userReadRepository.GetAll(false).AsEnumerable().Where(i => i.UserName == user.UserName).FirstOrDefault().Id,
                    RoleId = _roleReadRepository.GetAll(false).AsEnumerable().Where(i => i.Name == UserRoles.HR).FirstOrDefault().Id,

                };

                await _userRoleWriteRepository.AddAsync(userRole);

                var result3 = await _roleWriteRepository.SaveAsync();

                if (result3 == -1)
                {
                    throw new InvalidOperationException("Failed to create the UserRole.");
                }



            }
            if (!userExists2)
            {
                throw new ConflictException("User does not exist.");
            }
            if (!roleExists2)
            {
                throw new ConflictException("Role does not exist.");
            }

            var roleClaims = new List<RoleClaim>();

            var methods = await MethodsAsync();





            var role = _roleReadRepository.GetAll(false).Where(i => i.Id == _roleReadRepository.GetAll(false).AsEnumerable().Where(i => i.Name == UserRoles.HR).FirstOrDefault().Id).ToList();

            if (role != null)
            {

                var roleclaims = _mapper.Map<List<RoleClaimDTOforGetandGetAll>>(_roleClaimReadRepository.GetAll(false));



                roleclaims.Clear();

                roleClaims.Add(new RoleClaim()
                    {
                        ClaimType = "Get",
                        ClaimValue = "GetHRs",
                        RoleId = _roleReadRepository.GetAll(false).AsEnumerable().Where(i => i.Name == UserRoles.HR).FirstOrDefault().Id,
                    });






                //await _roleManager.AddClaimAsync(role, claim);



                await _roleClaimWriteRepository.AddRangeAsync(roleClaims);

                var result4 = await _roleClaimWriteRepository.SaveAsync();

                if (result4 == -1)
                {
                    throw new InvalidOperationException("Failed to create the RoleClaim.");
                }



            }



            var userClaims = new List<UserClaim>();






            var users = _userReadRepository.GetAll(false).Where(i => i.Id == Convert.ToInt32(_userReadRepository.GetAll(false).AsEnumerable().Where(i => i.UserName == model.Username).FirstOrDefault().Id));

            if (users != null)
            {


                var userclaims = _mapper.Map<List<UserClaimDTOforGetandGetAll>>(_userClaimReadRepository.GetAll(false));



                userclaims.Clear();

                foreach (var item in userAccesses)
                {

                    userClaims.Add(new UserClaim()
                    {
                        ClaimType = userAccesses.Where(i => i.Id == Convert.ToInt32(item.Id)).FirstOrDefault().UserAccess,
                        ClaimValue = userAccesses.Where(i => i.Id == Convert.ToInt32(item.Id)).FirstOrDefault().UserAccessDescription,
                        UserId = Convert.ToInt32(_userReadRepository.GetAll(false).AsEnumerable().Where(i => i.UserName == model.Username).FirstOrDefault().Id),
                    });
                };


                await _userClaimWriteRepository.AddRangeAsync(userClaims);

                var result5 = await _userClaimWriteRepository.SaveAsync();

                if (result5 == -1)
                {
                    throw new InvalidOperationException("Failed to create the UserClaim.");
                }



            }




        }


        public async Task<LoginResponse> Login(LoginDTO model)
        {


            var user = _userReadRepository.GetAll(false).AsEnumerable().Where(i => i.UserName == model.Username).FirstOrDefault();

            var passwordHash = PasswordComputeHash(model.Password, Environment.GetEnvironmentVariable("Salt"));



            if (user != null)
            {

                if (user.Password != passwordHash)
                {
                    throw new UnauthorizedException("Username or password is incorrect.");
                }

                var authClaims = new List<Claim>
                {
                    new Claim(ClaimTypes.Name, user.UserName),
                    new Claim(Microsoft.IdentityModel.JsonWebTokens.JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()),
                };



                var token = CreateToken(authClaims);
                var refreshToken = GenerateRefreshToken();

                _ = int.TryParse(_configuration["JWT:RefreshTokenValidityInHours"], out int refreshTokenValidityInHours);

                DateTime utcDateTime = DateTime.UtcNow;

                TimeZoneInfo bakuTimeZone = TimeZoneInfo.FindSystemTimeZoneById("Azerbaijan Standard Time");

                // Convert UTC DateTime to Baku DateTime
                DateTime bakuDateTime = TimeZoneInfo.ConvertTimeFromUtc(utcDateTime, bakuTimeZone);

                User user_ = new()
                {
                    Id = user.Id,
                    UserName = user.UserName,
                    Email = user.Email,
                    Password = user.Password,
                    Phonenumber = user.Phonenumber,
                    ConcurrencyStamp = user.ConcurrencyStamp,
                    ImagePath = user.ImagePath,
                    RefreshToken = refreshToken,
                    RefreshTokenExpiryTime = bakuDateTime.AddDays(refreshTokenValidityInHours),
                };


                _userWriteRepository.Update(user_);
                await _userWriteRepository.SaveAsync();


                System.Globalization.CultureInfo.CurrentCulture.ClearCachedData();

                TimeZone localZone = TimeZone.CurrentTimeZone;
                DateTime localTime = localZone.ToLocalTime(DateTime.UtcNow);




                LoginResponseDTO loginResponse = new LoginResponseDTO
                {

                    Token = new JwtSecurityTokenHandler().WriteToken(token),
                    RefreshToken = refreshToken,
                    Expiration = token.ValidTo.ToString(),

                };


                var loginresult = _mapper.Map<LoginResponse>(loginResponse);


                return loginresult;


            }

            else
            {
                throw new UnauthorizedException("Username or password is incorrect.");

            }
        }



        public async Task<TokenModel> RefreshToken(TokenModel tokenModel)
        {
            if (tokenModel is null)
            {

                throw new BadHttpRequestException("Invalid client request");

            }

            string? accessToken = tokenModel.AccessToken;
            string? refreshToken = tokenModel.RefreshToken;

            var principal = GetPrincipalFromExpiredToken(accessToken);
            if (principal == null)
            {

                throw new BadHttpRequestException("Invalid access token or refresh token");

            }

            string username = principal.Identity.Name;


            var user = _userReadRepository.GetAll(false).Where(i => i.UserName == username).FirstOrDefault();

            DateTime utcDateTime = DateTime.UtcNow;

            TimeZoneInfo bakuTimeZone = TimeZoneInfo.FindSystemTimeZoneById("Azerbaijan Standard Time");

            // Convert UTC DateTime to Baku DateTime
            DateTime bakuDateTime = TimeZoneInfo.ConvertTimeFromUtc(utcDateTime, bakuTimeZone);

            if (user == null || user.RefreshToken != refreshToken || user.RefreshTokenExpiryTime <= bakuDateTime)
            {

                throw new BadHttpRequestException("Invalid access token or refresh token");

            }

            var newAccessToken = CreateToken(principal.Claims.ToList());
            var newRefreshToken = GenerateRefreshToken();

            user.RefreshToken = newRefreshToken;



            User user_ = new()
            {
                Id = user.Id,
                UserName = user.UserName,
                Email = user.Email,
                Password = user.Password,
                Phonenumber = user.Phonenumber,
                ConcurrencyStamp = user.ConcurrencyStamp,
                ImagePath = user.ImagePath,
                RefreshToken = newRefreshToken,
                RefreshTokenExpiryTime = user.RefreshTokenExpiryTime,
            };


            _userWriteRepository.Update(user_);
            await _userWriteRepository.SaveAsync();

            TokenModel model = new TokenModel
            {
                AccessToken = new JwtSecurityTokenHandler().WriteToken(newAccessToken),
                RefreshToken = newRefreshToken
            };

            return model;
        }

        public async Task Logout(string username)
        {
            var user = _userReadRepository.GetAll(false).Where(i => i.UserName == username).FirstOrDefault();

            if (user == null)
            {

                throw new BadHttpRequestException("Invalid user name");

            }

            user.RefreshToken = null;
            user.RefreshTokenExpiryTime = null;

            _userWriteRepository.Update(user);
            await _userWriteRepository.SaveAsync();
        }


        public async Task<List<RoleAccessMethodDTO>> MethodsAsync()
        {

            await Task.Delay(100);

            var roleAccessMethods = new List<RoleAccessMethodDTO>();

            roleAccessMethods.Add(new RoleAccessMethodDTO
            {
                Id = 1,
                Method = RoleAccessMethod.GetAdmins_Method,
                MethodType = RoleAccessMethodType.Get_Method,
            });

            roleAccessMethods.Add(new RoleAccessMethodDTO
            {
                Id = 2,
                Method = RoleAccessMethod.GetHR_Method,
                MethodType = RoleAccessMethodType.Get_Method,
            });

            if (roleAccessMethods.Any())
            {
                return roleAccessMethods;
            }
            else
            {
                throw new NotFoundException("Nothing found!");
            }
        }


        public async Task<List<UserAccessDTO>> UserAccessAsync()
        {

            var userAccessTypes = new List<UserAccessDTO>();

            var userAccessType = new UserAccessDTO();



            userAccessTypes.Add(new UserAccessDTO
            {
                Id = 1,
                UserAccess = UserAccess.Write_ClaimType,
                UserAccessDescription = UserAccessDescription.WriteDescription_ClaimValue,

            });

            userAccessTypes.Add(new UserAccessDTO
            {
                Id = 2,
                UserAccess = UserAccess.Read_ClaimType,
                UserAccessDescription = UserAccessDescription.ReadDescription_ClaimValue,
            });

            userAccessTypes.Add(new UserAccessDTO
            {
                Id = 3,
                UserAccess = UserAccess.AllAccess_ClaimType,
                UserAccessDescription = UserAccessDescription.AllDescription_ClaimValue,
            });


            if (userAccessTypes.Any())
            {

                return userAccessTypes;
            }

            else
            {

                throw new NotFoundException("Nothing found!");

            }
        }


        public static string PasswordComputeHash(string password, string pepper)
        {
            using (var sha256 = SHA256.Create())
            {
                var passwordSaltPepper = $"{password}{pepper}";
                var byteValue = Encoding.UTF8.GetBytes(passwordSaltPepper);
                var byteHash = sha256.ComputeHash(byteValue);
                var hash = Convert.ToBase64String(byteHash);

                return hash;
            }
        }




        private JwtSecurityToken CreateToken(List<Claim> authClaims)
        {
            var authSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["JWT:Secret"]));
            _ = int.TryParse(_configuration["JWT:TokenValidityInHour"], out int TokenValidityInHour);

            DateTime utcDateTime = DateTime.UtcNow;

            TimeZoneInfo bakuTimeZone = TimeZoneInfo.FindSystemTimeZoneById("Azerbaijan Standard Time");

            // Convert UTC DateTime to Baku DateTime
            DateTime bakuDateTime = TimeZoneInfo.ConvertTimeFromUtc(utcDateTime, bakuTimeZone);

            var token = new JwtSecurityToken(
                issuer: _configuration["JWT:ValidateIssuer"],
                audience: _configuration["JWT:ValidateAudience"],
                expires: bakuDateTime.AddHours(TokenValidityInHour),
                claims: authClaims,
                signingCredentials: new SigningCredentials(authSigningKey, SecurityAlgorithms.HmacSha256)
                );

            return token;
        }

        private static string GenerateRefreshToken()
        {
            var randomNumber = new byte[64];
            using var rng = RandomNumberGenerator.Create();
            rng.GetBytes(randomNumber);
            return Convert.ToBase64String(randomNumber);
        }

        private ClaimsPrincipal? GetPrincipalFromExpiredToken(string? token)
        {
            var tokenValidationParameters = new TokenValidationParameters
            {
                ValidateAudience = false,
                ValidateIssuer = false,
                ValidateIssuerSigningKey = true,
                IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["JWT:Secret"])),
                ValidateLifetime = false
            };

            var tokenHandler = new JwtSecurityTokenHandler();
            var principal = tokenHandler.ValidateToken(token, tokenValidationParameters, out SecurityToken securityToken);
            if (securityToken is not JwtSecurityToken jwtSecurityToken || !jwtSecurityToken.Header.Alg.Equals(SecurityAlgorithms.HmacSha256, StringComparison.InvariantCultureIgnoreCase))
            {

                throw new SecurityTokenException("Invalid token");
            }

            return principal;

        }
    }
}
