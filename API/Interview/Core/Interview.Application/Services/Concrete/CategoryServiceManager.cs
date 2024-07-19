using AutoMapper;
using Interview.Application.Exception;
using Interview.Application.Mapper.DTO.CategoryDTO;
using Interview.Application.Repositories.Custom;
using Interview.Application.Services.Abstract;
using Interview.Domain.Entities.Models;
using Microsoft.IdentityModel.Tokens;
using System.Security.Claims;

namespace Interview.Application.Services.Concrete
{
    public class CategoryServiceManager : ICategoryService
    {

        public readonly IMapper _mapper;



        private readonly ICategoryWriteRepository _categoryWriteRepository;
        private readonly ICategoryReadRepository _categoryReadRepository;
        private readonly IUserReadRepository _userReadRepository;

        public CategoryServiceManager(IMapper mapper, ICategoryWriteRepository categoryWriteRepository, ICategoryReadRepository categoryReadRepository, IUserReadRepository userReadRepository)
        {
            _mapper = mapper;
            _categoryWriteRepository = categoryWriteRepository;
            _categoryReadRepository = categoryReadRepository;
            _userReadRepository = userReadRepository;
        }




        #region Category service manager

        public async Task CategoryCreate(CategoryDTOforCreate model, ClaimsPrincipal claimsPrincipal)
        {

          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    var entity = _mapper.Map<Category>(model);



                    entity = new Category
                    {
                        Name = entity.Name,

                    };


                    await _categoryWriteRepository.AddAsync(entity);

                    await _categoryWriteRepository.SaveAsync();
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

        public async Task<List<CategoryDTOforGetandGetAll>> GetCategory(ClaimsPrincipal claimsPrincipal)
        {
          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    List<CategoryDTOforGetandGetAll> datas = null;

                    await Task.Run(() =>
                    {
                        datas = _mapper.Map<List<CategoryDTOforGetandGetAll>>(_categoryReadRepository.GetAll(false));
                    });

                    if (datas.Count <= 0)
                    {
                        throw new NotFoundException("Category not found");
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

        public async Task<CategoryDTOforGetandGetAll> GetCategoryById(int id, ClaimsPrincipal claimsPrincipal)
        {
          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    CategoryDTOforGetandGetAll item = null;


                    item = _mapper.Map<CategoryDTOforGetandGetAll>(await _categoryReadRepository.GetByIdAsync(id.ToString(), false));


                    if (item == null)
                    {
                        throw new NotFoundException("Category not found");
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

        public async Task CategoryUpdate(CategoryDTOforUpdate model, ClaimsPrincipal claimsPrincipal)
        {
          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    var existing = await _categoryReadRepository.GetByIdAsync(model.Id.ToString(), false);

                    if (existing is null)
                    {
                        throw new NotFoundException("Category not found");

                    }


                    var entity = _mapper.Map<Category>(model);


                    entity = new Category
                    {
                        Id = existing.Id,
                        Name = entity.Name,

                    };


                    _categoryWriteRepository.Update(entity);
                    await _categoryWriteRepository.SaveAsync();
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

        public async Task<CategoryDTOforGetandGetAll> DeleteCategoryById(int id, ClaimsPrincipal claimsPrincipal)
        {
          if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    if (_categoryReadRepository.GetAll().Any(i => i.Id == Convert.ToInt32(id)))
                    {

                        await _categoryWriteRepository.RemoveByIdAsync(id.ToString());
                        await _categoryWriteRepository.SaveAsync();

                        return null;

                    }

                    else
                    {
                        throw new NotFoundException("Category not found");
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
