using AutoMapper;
using Interview.Application.Exception;
using Interview.Application.Mapper.DTO.CandidateDTO;
using Interview.Application.Repositories.Custom;
using Interview.Application.Services.Abstract;
using Interview.Domain.Entities.Models;
using Microsoft.IdentityModel.Tokens;
using System.Security.Claims;

namespace Interview.Application.Services.Concrete
{
    public class CandidateServiceManager : ICandidateService
    {

        public readonly IMapper _mapper;



        private readonly ICandidateWriteRepository _candidateWriteRepository;
        private readonly ICandidateReadRepository _candidateReadRepository;
        private readonly ICandidateDocumentWriteRepository _candidateDocumentWriteRepository;
        private readonly ICandidateDocumentReadRepository _candidateDocumentReadRepository;
        private readonly IUserReadRepository _userReadRepository;


        public CandidateServiceManager(IMapper mapper, ICandidateWriteRepository candidateWriteRepository, ICandidateReadRepository candidateReadRepository, ICandidateDocumentWriteRepository candidateDocumentWriteRepository, ICandidateDocumentReadRepository candidateDocumentReadRepository, IUserReadRepository userReadRepository)
        {
            _mapper = mapper;
            _candidateWriteRepository = candidateWriteRepository;
            _candidateReadRepository = candidateReadRepository;
            _candidateDocumentWriteRepository = candidateDocumentWriteRepository;
            _candidateDocumentReadRepository = candidateDocumentReadRepository;
            _userReadRepository = userReadRepository;
        }





        #region Candidate service manager

        public async Task CandidateCreate(CandidateDTOforCreate model, ClaimsPrincipal claimsPrincipal)
        {

          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    var entity = _mapper.Map<Candidate>(model);

                    var existing = await _candidateDocumentReadRepository.GetByIdAsync(model.CandidateDocumentId.ToString(), false);

                    if (existing is null)
                    {
                        throw new NotFoundException("CandidateDocument not found");

                    }

                    entity = new Candidate
                    {
                        CandidateDocumentId = model.CandidateDocumentId,

                    };


                    await _candidateWriteRepository.AddAsync(entity);

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

        public async Task<List<CandidateDTOforGetandGetAll>> GetCandidate(ClaimsPrincipal claimsPrincipal)
        {

          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    List<CandidateDTOforGetandGetAll> datas = null;

                    await Task.Run(() =>
                    {
                        datas = _mapper.Map<List<CandidateDTOforGetandGetAll>>(_candidateReadRepository.GetAll(false));
                    });

                    if (datas.Count <= 0)
                    {
                        throw new NotFoundException("Candidate not found");
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

        public async Task<CandidateDTOforGetandGetAll> GetCandidateById(int id, ClaimsPrincipal claimsPrincipal)
        {

          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    CandidateDTOforGetandGetAll item = null;


                    item = _mapper.Map<CandidateDTOforGetandGetAll>(await _candidateReadRepository.GetByIdAsync(id.ToString(), false));


                    if (item == null)
                    {
                        throw new NotFoundException("Candidate not found");
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

        public async Task CandidateUpdate(CandidateDTOforUpdate model, ClaimsPrincipal claimsPrincipal)
        {

          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    var existing = await _candidateReadRepository.GetByIdAsync(model.Id.ToString(), false);

                    var existing2 = await _candidateDocumentReadRepository.GetByIdAsync(model.CandidateDocumentId.ToString(), false);

                    if (existing is null)
                    {
                        throw new NotFoundException("Candidate not found");

                    }



                    if (existing2 is null)
                    {
                        throw new NotFoundException("CandidateDocument not found");

                    }

                    var entity = _mapper.Map<Candidate>(model);

                    entity = new Candidate
                    {
                        Id = model.Id,
                        CandidateDocumentId = model.CandidateDocumentId,

                    };

                    _candidateWriteRepository.Update(entity);
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

        public async Task<CandidateDTOforGetandGetAll> DeleteCandidateById(int id, ClaimsPrincipal claimsPrincipal)
        {


          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    if (_candidateReadRepository.GetAll().Any(i => i.Id == Convert.ToInt32(id)))
                    {

                        await _candidateWriteRepository.RemoveByIdAsync(id.ToString());
                        await _candidateWriteRepository.SaveAsync();

                        return null;

                    }

                    else
                    {
                        throw new NotFoundException("Candidate not found");
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
