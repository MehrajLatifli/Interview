using AutoMapper;
using Interview.Application.Exception;
using Interview.Application.Mapper.DTO.VacancyDTO;
using Interview.Application.Repositories.Custom;
using Interview.Application.Services.Abstract;
using Interview.Domain.Entities.Models;
using System.Security.Claims;

namespace Interview.Application.Services.Concrete
{
    public class VacancyServiceManager : IVacancyService
    {

        public readonly IMapper _mapper;



        private readonly IVacancyWriteRepository _vacancyWriteRepository;
        private readonly IVacancyReadRepository _vacancyReadRepository;

        private readonly IStructureWriteRepository _structureWriteRepository;
        private readonly IStructureReadRepository _structureReadRepository;

        private readonly IPositionWriteRepository _positionWriteRepository;
        private readonly IPositionReadRepository _positionReadRepository;

        private readonly ISessionWriteRepository _sessionWriteRepository;
        private readonly ISessionReadRepository  _sessionReadRepository;


        private readonly IUserReadRepository _userReadRepository;

        public VacancyServiceManager(IMapper mapper, IVacancyWriteRepository vacancyWriteRepository, IVacancyReadRepository vacancyReadRepository, IStructureWriteRepository structureWriteRepository, IStructureReadRepository structureReadRepository, IPositionWriteRepository positionWriteRepository, IPositionReadRepository positionReadRepository, IUserReadRepository userReadRepository, ISessionWriteRepository sessionWriteRepository, ISessionReadRepository sessionReadRepository)
        {
            _mapper = mapper;
            _vacancyWriteRepository = vacancyWriteRepository;
            _vacancyReadRepository = vacancyReadRepository;
            _structureWriteRepository = structureWriteRepository;
            _structureReadRepository = structureReadRepository;
            _positionWriteRepository = positionWriteRepository;
            _positionReadRepository = positionReadRepository;
            _userReadRepository = userReadRepository;
            _sessionWriteRepository = sessionWriteRepository;
            _sessionReadRepository = sessionReadRepository;
        }





        #region Vacancy service manager

        public async Task VacancyCreate(VacancyDTOforCreate model, ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {



                    var entity = _mapper.Map<Vacancy>(model);

                    var existing = await _structureReadRepository.GetByIdAsync(model.StructureId.ToString(), false);
                    var existing2 = await _positionReadRepository.GetByIdAsync(model.PositionId.ToString(), false);

                    if (existing2 is null)
                    {
                        throw new NotFoundException("Position not found");

                    }

                    if (existing is null)
                    {
                        throw new NotFoundException("Structure not found");

                    }


                    entity = new Vacancy
                    {
                        Title = model.Title,
                        Description = model.Description,
                        StartDate = model.StartDate,
                        EndDate = model.EndDate,
                        PositionId = model.PositionId,
                        StructureId = model.StructureId,


                    };


                    await _vacancyWriteRepository.AddAsync(entity);

                    await _vacancyWriteRepository.SaveAsync();
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

        public async Task<List<VacancyDTOforGetandGetAll>> GetVacancy(ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    List<VacancyDTOforGetandGetAll> datas = null;

                    await Task.Run(() =>
                    {
                        datas = _mapper.Map<List<VacancyDTOforGetandGetAll>>(_vacancyReadRepository.GetAll(false));
                    });

                    if (datas.Count <= 0)
                    {
                        throw new NotFoundException("Vacancy not found");
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

        public async Task<VacancyDTOforGetandGetAll> GetVacancyById(int id, ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    VacancyDTOforGetandGetAll item = null;


                    item = _mapper.Map<VacancyDTOforGetandGetAll>(await _vacancyReadRepository.GetByIdAsync(id.ToString(), false));


                    if (item == null)
                    {
                        throw new NotFoundException("Vacancy not found");
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

        public async Task VacancyUpdate(VacancyDTOforUpdate model, ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {


                    var existing = await _vacancyReadRepository.GetByIdAsync(model.Id.ToString(), false);
                    var existing2 = await _positionReadRepository.GetByIdAsync(model.PositionId.ToString(), false);
                    var existing3 = await _structureReadRepository.GetByIdAsync(model.StructureId.ToString(), false);




                    if (existing is null)
                    {
                        throw new NotFoundException("Vacancy not found");

                    }

                    if (existing2 is null)
                    {
                        throw new NotFoundException("Position not found");

                    }

                    if (existing3 is null)
                    {
                        throw new NotFoundException("Structure not found");

                    }




                    var entity = _mapper.Map<Vacancy>(model);



                    entity = new Vacancy
                    {
                        Id = model.Id,
                        Title = model.Title,
                        Description = model.Description,
                        StartDate = model.StartDate,
                        EndDate = model.EndDate,
                        PositionId = model.PositionId,
                        StructureId = model.StructureId,


                    };

                    _vacancyWriteRepository.Update(entity);
                    await _vacancyWriteRepository.SaveAsync();
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

        public async Task<VacancyDTOforGetandGetAll> DeleteVacancyById(int id, ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    if (_sessionReadRepository.GetAll(false).AsEnumerable().Any(i => i.VacancyId == id))
                    {
                        throw new ForbiddenException("The vacancy cannot be deleted because a session corresponding to the vacancy exists.");
                    }
                    else
                    {

                        if (_vacancyReadRepository.GetAll().Any(i => i.Id == Convert.ToInt32(id)))
                        {




                            await _vacancyWriteRepository.RemoveByIdAsync(id.ToString());
                            await _vacancyWriteRepository.SaveAsync();

                            return null;

                        }

                        else
                        {
                            throw new NotFoundException("Vacancy not found");
                        }
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
