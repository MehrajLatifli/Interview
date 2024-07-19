using AutoMapper;
using Interview.Application.Exception;
using Interview.Application.Mapper.DTO.PositionDTO;
using Interview.Application.Repositories.Custom;
using Interview.Application.Services.Abstract;
using Interview.Domain.Entities.Models;
using System.Security.Claims;

namespace Interview.Application.Services.Concrete
{
    public class PositionServiceManager : IPositionService
    {

        public readonly IMapper _mapper;



        private readonly IPositionWriteRepository _positionWriteRepository;
        private readonly IPositionReadRepository _positionReadRepository;
        private readonly IUserReadRepository _userReadRepository;

        public PositionServiceManager(IMapper mapper, IPositionWriteRepository positionWriteRepository, IPositionReadRepository positionReadRepository, IUserReadRepository userReadRepository)
        {
            _mapper = mapper;
            _positionWriteRepository = positionWriteRepository;
            _positionReadRepository = positionReadRepository;
            _userReadRepository = userReadRepository;
        }






        #region Position service manager

        public async Task PositionCreate(PositionDTOforCreate model, ClaimsPrincipal claimsPrincipal)
        {
            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {


                    var entity = _mapper.Map<Position>(model);

                    entity = new Position
                    {
                        Name = model.Name,


                    };


                    await _positionWriteRepository.AddAsync(entity);

                    await _positionWriteRepository.SaveAsync();
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

        public async Task<List<PositionDTOforGetandGetAll>> GetPosition(ClaimsPrincipal claimsPrincipal)
        {
            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    List<PositionDTOforGetandGetAll> datas = null;

                    await Task.Run(() =>
                    {
                        datas = _mapper.Map<List<PositionDTOforGetandGetAll>>(_positionReadRepository.GetAll(false));
                    });

                    if (datas.Count <= 0)
                    {
                        throw new NotFoundException("Position not found");
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

        public async Task<PositionDTOforGetandGetAll> GetPositionById(int id, ClaimsPrincipal claimsPrincipal)
        {
            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    PositionDTOforGetandGetAll item = null;


                    item = _mapper.Map<PositionDTOforGetandGetAll>(await _positionReadRepository.GetByIdAsync(id.ToString(), false));


                    if (item == null)
                    {
                        throw new NotFoundException("Position not found");
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

        public async Task PositionUpdate(PositionDTOforUpdate model, ClaimsPrincipal claimsPrincipal)
        {
            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    var existing = await _positionReadRepository.GetByIdAsync(model.Id.ToString(), false);


                    if (existing is null)
                    {
                        throw new NotFoundException("Position not found");

                    }



                    if (existing is null)
                    {
                        throw new NotFoundException("Position not found");

                    }

                    var entity = _mapper.Map<Position>(model);

                    entity = new Position
                    {
                        Id = model.Id,
                        Name = model.Name,

                    };

                    _positionWriteRepository.Update(entity);
                    await _positionWriteRepository.SaveAsync();
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

        public async Task<PositionDTOforGetandGetAll> DeletePositionById(int id, ClaimsPrincipal claimsPrincipal)
        {
            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    if (_positionReadRepository.GetAll().Any(i => i.Id == Convert.ToInt32(id)))
                    {

                        await _positionWriteRepository.RemoveByIdAsync(id.ToString());
                        await _positionWriteRepository.SaveAsync();

                        return null;

                    }

                    else
                    {
                        throw new NotFoundException("Position not found");
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
