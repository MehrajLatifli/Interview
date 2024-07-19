using AutoMapper;
using Interview.Application.Exception;
using Interview.Application.Mapper.DTO.LevelDTO;
using Interview.Application.Repositories.Custom;
using Interview.Application.Services.Abstract;
using Interview.Domain.Entities.Models;
using System.Security.Claims;

namespace Interview.Application.Services.Concrete
{
    public class LevelServiceManager : ILevelService
    {

        public readonly IMapper _mapper;



        private readonly ILevelWriteRepository _levelWriteRepository;
        private readonly ILevelReadRepository _levelReadRepository;
        private readonly IUserReadRepository _userReadRepository;

        public LevelServiceManager(IMapper mapper, ILevelWriteRepository levelWriteRepository, ILevelReadRepository levelReadRepository, IUserReadRepository userReadRepository)
        {
            _mapper = mapper;
            _levelWriteRepository = levelWriteRepository;
            _levelReadRepository = levelReadRepository;
            _userReadRepository = userReadRepository;
        }



        #region Level service manager

        public async Task LevelCreate(LevelDTOforCreate model, ClaimsPrincipal claimsPrincipal)
        {
          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {


                    var entity = _mapper.Map<Level>(model);

                    entity = new Level
                    {
                        Name = model.Name,
                        Coefficient = model.Coefficient,

                    };


                    await _levelWriteRepository.AddAsync(entity);

                    await _levelWriteRepository.SaveAsync();
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

        public async Task<List<LevelDTOforGetandGetAll>> GetLevel(ClaimsPrincipal claimsPrincipal)
        {
          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    List<LevelDTOforGetandGetAll> datas = null;

                    await Task.Run(() =>
                    {
                        datas = _mapper.Map<List<LevelDTOforGetandGetAll>>(_levelReadRepository.GetAll(false));
                    });

                    if (datas.Count <= 0)
                    {
                        throw new NotFoundException("Level not found");
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

        public async Task<LevelDTOforGetandGetAll> GetLevelById(int id, ClaimsPrincipal claimsPrincipal)
        {
          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    LevelDTOforGetandGetAll item = null;


                    item = _mapper.Map<LevelDTOforGetandGetAll>(await _levelReadRepository.GetByIdAsync(id.ToString(), false));


                    if (item == null)
                    {
                        throw new NotFoundException("Level not found");
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

        public async Task LevelUpdate(LevelDTOforUpdate model, ClaimsPrincipal claimsPrincipal)
        {
          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    var existing = await _levelReadRepository.GetByIdAsync(model.Id.ToString(), false);


                    if (existing is null)
                    {
                        throw new NotFoundException("Level not found");

                    }



                    if (existing is null)
                    {
                        throw new NotFoundException("Level not found");

                    }

                    var entity = _mapper.Map<Level>(model);

                    entity = new Level
                    {
                        Id = model.Id,
                        Name = model.Name,
                        Coefficient = model.Coefficient,

                    };

                    _levelWriteRepository.Update(entity);
                    await _levelWriteRepository.SaveAsync();
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

        public async Task<LevelDTOforGetandGetAll> DeleteLevelById(int id, ClaimsPrincipal claimsPrincipal)
        {
          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    if (_levelReadRepository.GetAll().Any(i => i.Id == Convert.ToInt32(id)))
                    {

                        await _levelWriteRepository.RemoveByIdAsync(id.ToString());
                        await _levelWriteRepository.SaveAsync();

                        return null;

                    }

                    else
                    {
                        throw new NotFoundException("Level not found");
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
