using AutoMapper;
using Interview.Application.Exception;
using Interview.Application.Mapper.DTO.StructureTypeDTO;
using Interview.Application.Repositories.Custom;
using Interview.Application.Services.Abstract;
using Interview.Domain.Entities.Models;
using Microsoft.IdentityModel.Tokens;
using System.Security.Claims;

namespace Interview.Application.Services.Concrete
{
    public class StructureTypeServiceManager : IStructureTypeService
    {

        public readonly IMapper _mapper;



        private readonly IStructureTypeWriteRepository _structureTypeWriteRepository;
        private readonly IStructureTypeReadRepository _structureTypeReadRepository;

        private readonly IUserReadRepository _userReadRepository;

        public StructureTypeServiceManager(IMapper mapper, IStructureTypeWriteRepository structureTypeWriteRepository, IStructureTypeReadRepository structureTypeReadRepository, IUserReadRepository userReadRepository)
        {
            _mapper = mapper;
            _structureTypeWriteRepository = structureTypeWriteRepository;
            _structureTypeReadRepository = structureTypeReadRepository;
            _userReadRepository = userReadRepository;
        }


        #region StructureType service manager

        public async Task StructureTypeCreate(StructureTypeDTOforCreate model, ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {



                    var entity = _mapper.Map<StructureType>(model);

                    entity = new StructureType
                    {
                        Name = model.Name,


                    };


                    await _structureTypeWriteRepository.AddAsync(entity);

                    await _structureTypeWriteRepository.SaveAsync();
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

        public async Task<List<StructureTypeDTOforGetandGetAll>> GetStructureType(ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    List<StructureTypeDTOforGetandGetAll> datas = null;

                    await Task.Run(() =>
                    {
                        datas = _mapper.Map<List<StructureTypeDTOforGetandGetAll>>(_structureTypeReadRepository.GetAll(false));
                    });

                    if (datas.Count <= 0)
                    {
                        throw new NotFoundException("StructureType not found");
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

        public async Task<StructureTypeDTOforGetandGetAll> GetStructureTypeById(int id, ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    StructureTypeDTOforGetandGetAll item = null;


                    item = _mapper.Map<StructureTypeDTOforGetandGetAll>(await _structureTypeReadRepository.GetByIdAsync(id.ToString(), false));


                    if (item == null)
                    {
                        throw new NotFoundException("StructureType not found");
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

        public async Task StructureTypeUpdate(StructureTypeDTOforUpdate model, ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    var existing = await _structureTypeReadRepository.GetByIdAsync(model.Id.ToString(), false);


                    if (existing is null)
                    {
                        throw new NotFoundException("StructureType not found");

                    }



                    if (existing is null)
                    {
                        throw new NotFoundException("StructureType not found");

                    }

                    var entity = _mapper.Map<StructureType>(model);

                    entity = new StructureType
                    {
                        Id = model.Id,
                        Name = model.Name,

                    };

                    _structureTypeWriteRepository.Update(entity);
                    await _structureTypeWriteRepository.SaveAsync();
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

        public async Task<StructureTypeDTOforGetandGetAll> DeleteStructureTypeById(int id, ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    if (_structureTypeReadRepository.GetAll().Any(i => i.Id == Convert.ToInt32(id)))
                    {

                        await _structureTypeWriteRepository.RemoveByIdAsync(id.ToString());
                        await _structureTypeWriteRepository.SaveAsync();

                        return null;

                    }

                    else
                    {
                        throw new NotFoundException("StructureType not found");
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
