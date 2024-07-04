using AutoMapper;
using Azure.Storage.Blobs;
using Azure.Storage.Blobs.Models;
using Interview.Application.Exception;
using Interview.Application.Mapper.DTO.CandidateDocumentDTO;
using Interview.Application.Repositories.Custom;
using Interview.Application.Services.Abstract;
using Interview.Domain.Entities.Models;
using System;
using System.Diagnostics;
using System.Diagnostics.Metrics;
using System.Linq;

namespace Interview.Application.Services.Concrete
{

    public class CandidateDocumentServiceManager : ICandidateDocumentService
    {

        public readonly IMapper _mapper;

        private readonly ICandidateDocumentWriteRepository _candidateDocumentWriteRepository;
        private readonly ICandidateDocumentReadRepository _candidateDocumentReadRepository;

        public CandidateDocumentServiceManager(IMapper mapper, ICandidateDocumentWriteRepository candidateDocumentWriteRepository, ICandidateDocumentReadRepository candidateDocumentReadRepository)
        {
            _mapper = mapper;
            _candidateDocumentWriteRepository = candidateDocumentWriteRepository;
            _candidateDocumentReadRepository = candidateDocumentReadRepository;
        }




        #region CandidateDocument service manager

        public async Task CandidateDocumentCreate(CandidateDocumentDTOforCreate model, string AzureconnectionString)
        {


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
        }

        public async Task<List<CandidateDocumentDTOforGetandGetAll>> GetCandidateDocument()
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

        public async Task<CandidateDocumentDTOforGetandGetAll> GetCandidateDocumentById(int id)
        {
            CandidateDocumentDTOforGetandGetAll item = null;


            item = _mapper.Map<CandidateDocumentDTOforGetandGetAll>(await _candidateDocumentReadRepository.GetByIdAsync(id.ToString(), false));


            if (item == null)
            {
                throw new NotFoundException("CandidateDocument not found");
            }

            return item;
        }

        public async Task CandidateDocumentUpdate(CandidateDocumentDTOforUpdate model, string AzureconnectionString)
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

        public async Task<CandidateDocumentDTOforGetandGetAll> DeleteCandidateDocumentById(int id)
        {

            if (_candidateDocumentReadRepository.GetAll().Any(i => i.Id == Convert.ToInt32(id)))
            {

                await _candidateDocumentWriteRepository.RemoveByIdAsync(id.ToString());
                await _candidateDocumentWriteRepository.SaveAsync();

                return null;

            }

            else
            {
                throw new NotFoundException("CandidateDocument not found");
            }
        }

        #endregion


    }


}
