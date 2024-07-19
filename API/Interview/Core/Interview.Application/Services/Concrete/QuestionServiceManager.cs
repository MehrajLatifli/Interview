using AutoMapper;
using Interview.Application.Exception;
using Interview.Application.Mapper.DTO.QuestionDTO;
using Interview.Application.Repositories.Custom;
using Interview.Application.Services.Abstract;
using Interview.Domain.Entities.Models;
using Microsoft.IdentityModel.Tokens;
using System.Security.Claims;

namespace Interview.Application.Services.Concrete
{
    public class QuestionServiceManager : IQuestionService
    {

        public readonly IMapper _mapper;


        private readonly IQuestionWriteRepository _questionWriteRepository;
        private readonly IQuestionReadRepository _questionReadRepository;

        private readonly ICategoryWriteRepository _categoryWriteRepository;
        private readonly ICategoryReadRepository _categoryReadRepository;

        private readonly IStructureWriteRepository _structureWriteRepository;
        private readonly IStructureReadRepository _structureReadRepository;


        private readonly ILevelWriteRepository _levelWriteRepository;
        private readonly ILevelReadRepository _levelReadRepository;

        private readonly IUserReadRepository _userReadRepository;

        public QuestionServiceManager(IMapper mapper, IQuestionWriteRepository questionWriteRepository, IQuestionReadRepository questionReadRepository, ICategoryWriteRepository categoryWriteRepository, ICategoryReadRepository categoryReadRepository, IStructureWriteRepository structureWriteRepository, IStructureReadRepository structureReadRepository, ILevelWriteRepository levelWriteRepository, ILevelReadRepository levelReadRepository, IUserReadRepository userReadRepository)
        {
            _mapper = mapper;
            _questionWriteRepository = questionWriteRepository;
            _questionReadRepository = questionReadRepository;
            _categoryWriteRepository = categoryWriteRepository;
            _categoryReadRepository = categoryReadRepository;
            _structureWriteRepository = structureWriteRepository;
            _structureReadRepository = structureReadRepository;
            _levelWriteRepository = levelWriteRepository;
            _levelReadRepository = levelReadRepository;
            _userReadRepository = userReadRepository;
        }




        #region Question service manager

        public async Task QuestionCreate(QuestionDTOforCreate model, ClaimsPrincipal claimsPrincipal)
        {
            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {



                    var entity = _mapper.Map<Question>(model);


                    var existing1 = await _categoryReadRepository.GetByIdAsync(model.CategoryId.ToString(), false);
                    var existing2 = await _structureReadRepository.GetByIdAsync(model.StructureId.ToString(), false);
                    var existing3 = await _levelReadRepository.GetByIdAsync(model.LevelId.ToString(), false);

                    if (existing3 is null)
                    {
                        throw new NotFoundException("Level not found");

                    }
                    if (existing1 is null)
                    {
                        throw new NotFoundException("Category not found");

                    }

                    if (existing2 is null)
                    {
                        throw new NotFoundException("Structure not found");

                    }


                    entity = new Question
                    {
                        Text = entity.Text,
                        LevelId = entity.LevelId,
                        CategoryId = entity.CategoryId,
                        StructureId = model.StructureId,


                    };


                    await _questionWriteRepository.AddAsync(entity);

                    await _questionWriteRepository.SaveAsync();
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

        public async Task<List<QuestionDTOforGetandGetAll>> GetQuestion(ClaimsPrincipal claimsPrincipal)
        {
            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    List<QuestionDTOforGetandGetAll> datas = null;

                    await Task.Run(() =>
                    {
                        datas = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false));
                    });

                    if (datas.Count <= 0)
                    {
                        throw new NotFoundException("Question not found");
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



        public async Task<QuestionDTOforGetandGetAll> GetQuestionById(int id, ClaimsPrincipal claimsPrincipal)
        {
            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    QuestionDTOforGetandGetAll item = null;


                    item = _mapper.Map<QuestionDTOforGetandGetAll>(await _questionReadRepository.GetByIdAsync(id.ToString(), false));


                    if (item == null)
                    {
                        throw new NotFoundException("Question not found");
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

        public async Task QuestionUpdate(QuestionDTOforUpdate model, ClaimsPrincipal claimsPrincipal)
        {
            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    var existing = await _questionReadRepository.GetByIdAsync(model.Id.ToString(), false);
                    var existing2 = await _categoryReadRepository.GetByIdAsync(model.CategoryId.ToString(), false);
                    var existing3 = await _structureReadRepository.GetByIdAsync(model.StructureId.ToString(), false);
                    var existing4 = await _levelReadRepository.GetByIdAsync(model.LevelId.ToString(), false);


                    if (existing4 is null)
                    {
                        throw new NotFoundException("Level not found");

                    }
                    if (existing is null)
                    {
                        throw new NotFoundException("Question not found");

                    }

                    if (existing2 is null)
                    {
                        throw new NotFoundException("CategoryId not found");

                    }

                    if (existing3 is null)
                    {
                        throw new NotFoundException("Structure not found");

                    }




                    var entity = _mapper.Map<Question>(model);



                    entity = new Question
                    {
                        Id = model.Id,
                        Text = entity.Text,
                        LevelId = entity.LevelId,
                        CategoryId = entity.CategoryId,
                        StructureId = model.StructureId,


                    };

                    _questionWriteRepository.Update(entity);
                    await _questionWriteRepository.SaveAsync();
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

        public async Task<QuestionDTOforGetandGetAll> DeleteQuestionById(int id, ClaimsPrincipal claimsPrincipal)
        {
            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    if (_questionReadRepository.GetAll().Any(i => i.Id == Convert.ToInt32(id)))
                    {

                        await _questionWriteRepository.RemoveByIdAsync(id.ToString());
                        await _questionWriteRepository.SaveAsync();

                        return null;

                    }

                    else
                    {
                        throw new NotFoundException("Question not found");
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
