using AutoMapper;
using Interview.Application.Exception;
using Interview.Application.Mapper.DTO.StructureDTO;
using Interview.Application.Repositories.Custom;
using Interview.Application.Services.Abstract;
using Interview.Domain.Entities.Models;
using Microsoft.IdentityModel.Tokens;
using System.Security.Claims;

namespace Interview.Application.Services.Concrete
{
    public class StructureServiceManager : IStructureService
    {

        public readonly IMapper _mapper;



        private readonly IStructureWriteRepository _structureWriteRepository;
        private readonly IStructureReadRepository _structureReadRepository;

        private readonly IStructureTypeWriteRepository _structureTypeWriteRepository;
        private readonly IStructureTypeReadRepository _structureTypeReadRepository;
        private readonly IUserReadRepository _userReadRepository;

        public StructureServiceManager(IMapper mapper, IStructureWriteRepository structureWriteRepository, IStructureReadRepository structureReadRepository, IStructureTypeWriteRepository structureTypeWriteRepository, IStructureTypeReadRepository structureTypeReadRepository, IUserReadRepository userReadRepository)
        {
            _mapper = mapper;
            _structureWriteRepository = structureWriteRepository;
            _structureReadRepository = structureReadRepository;
            _structureTypeWriteRepository = structureTypeWriteRepository;
            _structureTypeReadRepository = structureTypeReadRepository;
            _userReadRepository = userReadRepository;
        }


        #region Structure service manager

        public async Task StructureCreate(StructureDTOforCreate model, ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {


                    var entity = _mapper.Map<Structure>(model);


                    var existing = await _structureTypeReadRepository.GetByIdAsync(model.StructureTypeId.ToString(), false);

                    if (existing is null)
                    {
                        throw new NotFoundException("StructureType not found");

                    }

                    entity = new Structure
                    {
                        Name = model.Name,
                        ParentId = model.ParentId,
                        StructureTypeId = model.StructureTypeId,


                    };


                    await _structureWriteRepository.AddAsync(entity);

                    await _structureWriteRepository.SaveAsync();
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

        public async Task<List<StructureDTOforGetandGetAll>> GetStructure(ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    List<StructureDTOforGetandGetAll> datas = null;

                    await Task.Run(() =>
                    {
                        datas = _mapper.Map<List<StructureDTOforGetandGetAll>>(_structureReadRepository.GetAll(false));
                    });

                    if (datas.Count <= 0)
                    {
                        throw new NotFoundException("Structure not found");
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

        public async Task<StructureDTOforGetandGetAll> GetStructureById(int id, ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    StructureDTOforGetandGetAll item = null;


                    item = _mapper.Map<StructureDTOforGetandGetAll>(await _structureReadRepository.GetByIdAsync(id.ToString(), false));


                    if (item == null)
                    {
                        throw new NotFoundException("Structure not found");
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

        public async Task StructureUpdate(StructureDTOforUpdate model, ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {


                    var existing = await _structureTypeReadRepository.GetByIdAsync(model.StructureTypeId.ToString(), false);

                    if (existing is null)
                    {
                        throw new NotFoundException("StructureType not found");

                    }

                    var existing2 = await _structureReadRepository.GetByIdAsync(model.Id.ToString(), false);


                    if (existing2 is null)
                    {
                        throw new NotFoundException("Structure not found");

                    }

                    var entity = _mapper.Map<Structure>(model);

                    entity = new Structure
                    {
                        Id = model.Id,
                        Name = model.Name,

                    };

                    _structureWriteRepository.Update(entity);
                    await _structureWriteRepository.SaveAsync();
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

        public async Task<StructureDTOforGetandGetAll> DeleteStructureById(int id, ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    if (_structureReadRepository.GetAll().Any(i => i.Id == Convert.ToInt32(id)))
                    {

                        await _structureWriteRepository.RemoveByIdAsync(id.ToString());
                        await _structureWriteRepository.SaveAsync();

                        return null;

                    }

                    else
                    {
                        throw new NotFoundException("Structure not found");
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
