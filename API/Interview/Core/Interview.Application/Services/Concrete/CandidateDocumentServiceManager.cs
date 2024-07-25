using AutoMapper;
using Azure.Storage.Blobs;
using Azure.Storage.Blobs.Models;
using Interview.Application.Exception;
using Interview.Application.Mapper.DTO.CandidateDocumentDTO;
using Interview.Application.Mapper.DTO.CandidateDTO;
using Interview.Application.Repositories.Custom;
using Interview.Application.Services.Abstract;
using Interview.Domain.Entities.Models;
using Microsoft.IdentityModel.Tokens;
using System;
using System.Diagnostics;
using System.Diagnostics.Metrics;
using System.Linq;
using System.Security.Claims;

namespace Interview.Application.Services.Concrete
{

    public class CandidateDocumentServiceManager : ICandidateDocumentService
    {

        public readonly IMapper _mapper;

        private readonly ICandidateDocumentWriteRepository _candidateDocumentWriteRepository;
        private readonly ICandidateDocumentReadRepository _candidateDocumentReadRepository;
        private readonly IUserReadRepository _userReadRepository;
        private readonly ICandidateWriteRepository _candidateWriteRepository;
        private readonly ICandidateReadRepository _candidateReadRepository;

        public CandidateDocumentServiceManager(IMapper mapper, ICandidateDocumentWriteRepository candidateDocumentWriteRepository, ICandidateDocumentReadRepository candidateDocumentReadRepository, IUserReadRepository userReadRepository, ICandidateWriteRepository candidateWriteRepository, ICandidateReadRepository candidateReadRepository)
        {
            _mapper = mapper;
            _candidateDocumentWriteRepository = candidateDocumentWriteRepository;
            _candidateDocumentReadRepository = candidateDocumentReadRepository;
            _userReadRepository = userReadRepository;
            _candidateWriteRepository = candidateWriteRepository;
            _candidateReadRepository = candidateReadRepository;
        }




        #region CandidateDocument service manager

        public async Task CandidateDocumentCreate(CandidateDocumentDTOforCreate model, string AzureconnectionString, ClaimsPrincipal claimsPrincipal)
        {

          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    string connectionString = AzureconnectionString;

                    string azuriteConnectionString = Environment.GetEnvironmentVariable("CUSTOMCONNSTR_AZURE_STORAGE_CONNECTION_STRING");
                    if (!string.IsNullOrEmpty(azuriteConnectionString))
                    {
                        connectionString = azuriteConnectionString;
                    }

                    string containerName = "profile-images";
                    string blobName = model.Name + "_" + Guid.NewGuid() + Path.GetExtension(model.Cv.FileName);

                    // Set content type and disposition based on file extension
                    var blobHttpHeaders = new BlobHttpHeaders
                    {
                        ContentType = GetContentType(Path.GetExtension(model.Cv.FileName)),
                        ContentDisposition = "inline" // Ensures the image is viewable in the browser
                    };

                    // Create a BlobServiceClient and get a reference to the container
                    var blobServiceClient = new BlobServiceClient(connectionString);
                    var containerClient = blobServiceClient.GetBlobContainerClient(containerName);
                    await containerClient.CreateIfNotExistsAsync(PublicAccessType.Blob);

                    // Get a reference to the blob and upload the image with headers
                    var blobClient = containerClient.GetBlobClient(blobName);
                    using (var stream = model.Cv.OpenReadStream())
                    {
                        await blobClient.UploadAsync(stream, new BlobUploadOptions
                        {
                            HttpHeaders = blobHttpHeaders
                        });
                    }

                    string Url = blobClient.Uri.ToString();

                    var entity = _mapper.Map<CandidateDocument>(model);

                    entity = new CandidateDocument
                    {
                        Surname = model.Surname,
                        Name = model.Name,
                        Phonenumber = model.Phonenumber,
                        Email = model.Email,
                        CV = Url,
                        Address = model.Address,
                        Patronymic = model.Patronymic,

                    };

               


                    await _candidateDocumentWriteRepository.AddAsync(entity);

                    await _candidateDocumentWriteRepository.SaveAsync();

                   var candidate =  _mapper.Map<List<CandidateDocumentDTOforGetandGetAll>>(_candidateDocumentReadRepository.GetAll(false)).TakeLast(1).FirstOrDefault().Id;

                    var entity2 = new Candidate
                    {
                        CandidateDocumentId = candidate,

                    };

                    await _candidateWriteRepository.AddAsync(entity2);

                    await _candidateWriteRepository.SaveAsync();
                }
                else
                {
                    throw new UnauthorizedException("Current user is not authenticated.");
                }
            }
            else
            {
                throw new UnauthorizedException("Current user is not authenticated.");
            }
        }

        private string GetContentType(string extension)
        {
            return extension.ToLower() switch
            {
                ".doc" => "application/msword",
                ".pdf" => "application/pdf",
                ".epub" => "application/epub+zip",
                ".docx" => "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                _ => "application/octet-stream"
            };
        }

        public async Task<List<CandidateDocumentDTOforGetandGetAll>> GetCandidateDocument(ClaimsPrincipal claimsPrincipal)
        {
          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    List<CandidateDocumentDTOforGetandGetAll> datas = null;

                    await Task.Run(() =>
                    {
                        datas = _mapper.Map<List<CandidateDocumentDTOforGetandGetAll>>(_candidateDocumentReadRepository.GetAll(false));
                    });

                    if (datas.Count <= 0)
                    {
                        throw new NotFoundException("CandidateDocument not found");
                    }

                    return datas;
                }
                else
                {
                    throw new UnauthorizedException("Current user is not authenticated.");
                }
            }
            else
            {
                throw new UnauthorizedException("Current user is not authenticated.");
            }
        }

        public async Task<CandidateDocumentDTOforGetandGetAll> GetCandidateDocumentById(int id, ClaimsPrincipal claimsPrincipal)
        {
          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    CandidateDocumentDTOforGetandGetAll item = null;


                    item = _mapper.Map<CandidateDocumentDTOforGetandGetAll>(await _candidateDocumentReadRepository.GetByIdAsync(id.ToString(), false));


                    if (item == null)
                    {
                        throw new NotFoundException("CandidateDocument not found");
                    }

                    return item;
                }
                else
                {
                    throw new UnauthorizedException("Current user is not authenticated.");
                }
            }
            else
            {
                throw new UnauthorizedException("Current user is not authenticated.");
            }
        }

        public async Task CandidateDocumentUpdate(CandidateDocumentDTOforUpdate model, string AzureconnectionString, ClaimsPrincipal claimsPrincipal)
        {
          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    var existing = await _candidateDocumentReadRepository.GetByIdAsync(model.Id.ToString(), false);


                    if (existing is null)
                    {
                        throw new NotFoundException("CandidateDocument not found");

                    }

                    string connectionString = AzureconnectionString;

                    string azuriteConnectionString = Environment.GetEnvironmentVariable("CUSTOMCONNSTR_AZURE_STORAGE_CONNECTION_STRING");
                    if (!string.IsNullOrEmpty(azuriteConnectionString))
                    {
                        connectionString = azuriteConnectionString;
                    }

                    string containerName = "cv-files";

                    string blobName = model.Name + "_" + model.Email + "_" + Guid.NewGuid().ToString() + Path.GetExtension(model.Cv.FileName);

                    BlobServiceClient blobServiceClient = new BlobServiceClient(connectionString);
                    BlobContainerClient containerClient = blobServiceClient.GetBlobContainerClient(containerName);

                    await containerClient.CreateIfNotExistsAsync(PublicAccessType.Blob);

                    BlobClient blobClient = containerClient.GetBlobClient(blobName);

                    using (Stream stream = model.Cv.OpenReadStream())
                    {
                        await blobClient.UploadAsync(stream, true);
                    }

                    string Url = blobClient.Uri.ToString();



                    if (existing is null)
                    {
                        throw new NotFoundException("CandidateDocument not found");

                    }

                    var entity = _mapper.Map<CandidateDocument>(model);

                    entity = new CandidateDocument
                    {
                        Id = model.Id,
                        Surname = model.Surname,
                        Name = model.Name,
                        Phonenumber = model.Phonenumber,
                        Email = model.Email,
                        CV = Url,
                        Address = model.Address,
                        Patronymic = model.Patronymic,

                    };

                    _candidateDocumentWriteRepository.Update(entity);
                    await _candidateDocumentWriteRepository.SaveAsync();
                }
                else
                {
                    throw new UnauthorizedException("Current user is not authenticated.");
                }
            }
            else
            {
                throw new UnauthorizedException("Current user is not authenticated.");
            }

        }

        public async Task<CandidateDocumentDTOforGetandGetAll> DeleteCandidateDocumentById(int id, ClaimsPrincipal claimsPrincipal)
        {
          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    if (_candidateDocumentReadRepository.GetAll().Any(i => i.Id == Convert.ToInt32(id)))
                    {



                        var candidateDocumentId = _mapper.Map<List<CandidateDocumentDTOforGetandGetAll>>(_candidateDocumentReadRepository.GetAll(false)).Where(i=>i.Id==id).FirstOrDefault().Id;
                        var candidateId = _mapper.Map<List<CandidateDTOforGetandGetAll>>(_candidateReadRepository.GetAll(false)).Where(i => i.CandidateDocumentId == id).FirstOrDefault().Id;

                        var entity2 = new Candidate
                        {
                            Id = candidateId,
                            CandidateDocumentId = candidateDocumentId,

                        };

                        await _candidateWriteRepository.RemoveByIdAsync(entity2.Id.ToString());

                        await _candidateWriteRepository.SaveAsync();



                        await _candidateDocumentWriteRepository.RemoveByIdAsync(id.ToString());
                        await _candidateDocumentWriteRepository.SaveAsync();


                        return null;

                    }

                    else
                    {
                        throw new NotFoundException("CandidateDocument not found");
                    }
                }
                else
                {
                    throw new UnauthorizedException("Current user is not authenticated.");
                }
            }
            else
            {
                throw new UnauthorizedException("Current user is not authenticated.");
            }
        }

        #endregion


    }


}
