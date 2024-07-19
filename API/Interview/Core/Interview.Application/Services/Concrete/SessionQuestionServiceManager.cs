using AutoMapper;
using Azure.Core;
using Interview.Application.Exception;
using Interview.Application.Mapper.DTO.PositionDTO;
using Interview.Application.Mapper.DTO.QuestionDTO;
using Interview.Application.Mapper.DTO.SessionDTO;
using Interview.Application.Mapper.DTO.SessionQuestionDTO;
using Interview.Application.Mapper.DTO.StructureDTO;
using Interview.Application.Mapper.DTO.VacancyDTO;
using Interview.Application.Repositories.Custom;
using Interview.Application.Services.Abstract;
using Interview.Domain.Entities.Models;
using Interview.Domain.Entities.Requests;
using Microsoft.CodeAnalysis.Elfie.Diagnostics;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Drawing.Printing;
using System.Security.Claims;

namespace Interview.Application.Services.Concrete
{
    public class SessionQuestionServiceManager : ISessionQuestionService
    {

        public readonly IMapper _mapper;

        private readonly ISessionQuestionWriteRepository _sessionQuestionWriteRepository;
        private readonly ISessionQuestionReadRepository _sessionQuestionReadRepository;

        private readonly ISessionWriteRepository _sessionWriteRepository;
        private readonly ISessionReadRepository _sessionReadRepository;

        private readonly IQuestionWriteRepository _questionWriteRepository;
        private readonly IQuestionReadRepository _questionReadRepository;

        private readonly IStructureWriteRepository _structureWriteRepository;
        private readonly IStructureReadRepository _structureReadRepository;

        private readonly IPositionWriteRepository _positionWriteRepository;
        private readonly IPositionReadRepository _positionReadRepository;

        private readonly IVacancyWriteRepository _vacancyWriteRepository;
        private readonly IVacancyReadRepository _vacancyReadRepository;


        private readonly IUserReadRepository _userReadRepository;

        public SessionQuestionServiceManager(IMapper mapper, ISessionQuestionWriteRepository sessionQuestionWriteRepository, ISessionQuestionReadRepository sessionQuestionReadRepository, ISessionWriteRepository sessionWriteRepository, ISessionReadRepository sessionReadRepository, IQuestionWriteRepository questionWriteRepository, IQuestionReadRepository questionReadRepository, IStructureWriteRepository structureWriteRepository, IStructureReadRepository structureReadRepository, IPositionWriteRepository positionWriteRepository, IPositionReadRepository positionReadRepository, IVacancyWriteRepository vacancyWriteRepository, IVacancyReadRepository vacancyReadRepository, IUserReadRepository userReadRepository)
        {
            _mapper = mapper;
            _sessionQuestionWriteRepository = sessionQuestionWriteRepository;
            _sessionQuestionReadRepository = sessionQuestionReadRepository;
            _sessionWriteRepository = sessionWriteRepository;
            _sessionReadRepository = sessionReadRepository;
            _questionWriteRepository = questionWriteRepository;
            _questionReadRepository = questionReadRepository;
            _structureWriteRepository = structureWriteRepository;
            _structureReadRepository = structureReadRepository;
            _positionWriteRepository = positionWriteRepository;
            _positionReadRepository = positionReadRepository;
            _vacancyWriteRepository = vacancyWriteRepository;
            _vacancyReadRepository = vacancyReadRepository;
            _userReadRepository = userReadRepository;
        }




        #region SessionQuestion service manager

        public async Task SessionQuestionCreate(SessionQuestionDTOforCreate model, ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {


                    var entity = _mapper.Map<SessionQuestion>(model);


                    var existing1 = await _sessionReadRepository.GetByIdAsync(model.SessionId.ToString(), false);
                    var existing2 = await _questionReadRepository.GetByIdAsync(model.QuestionId.ToString(), false);

                    if (existing1 is null)
                    {
                        throw new NotFoundException("Session not found");

                    }

                    if (existing2 is null)
                    {
                        throw new NotFoundException("Question not found");

                    }

                    entity = new SessionQuestion
                    {
                        Value = entity.Value,

                        SessionId = entity.SessionId,
                        QuestionId = model.QuestionId,


                    };


                    await _sessionQuestionWriteRepository.AddAsync(entity);

                    await _sessionQuestionWriteRepository.SaveAsync();
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

        public async Task<List<SessionQuestionDTOforGetandGetAll>> GetSessionQuestion(ClaimsPrincipal claimsPrincipal)
        {
            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    List<SessionQuestionDTOforGetandGetAll> datas = null;

                    await Task.Run(() =>
                    {
                        datas = _mapper.Map<List<SessionQuestionDTOforGetandGetAll>>(_sessionQuestionReadRepository.GetAll(false));
                    });

                    if (datas.Count <= 0)
                    {
                        throw new NotFoundException("SessionQuestion not found");
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

        public async Task<SessionQuestionDTOforGetandGetAll> GetSessionQuestionById(int id, ClaimsPrincipal claimsPrincipal)
        {
            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    SessionQuestionDTOforGetandGetAll item = null;


                    item = _mapper.Map<SessionQuestionDTOforGetandGetAll>(await _sessionQuestionReadRepository.GetByIdAsync(id.ToString(), false));


                    if (item == null)
                    {
                        throw new NotFoundException("SessionQuestion not found");
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

        public async Task<List<SessionQuestionDTOforGetandGetAll>> GetSessionQuestionBySessionId(int sessionId, ClaimsPrincipal claimsPrincipal)
        {
            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    List<SessionQuestionDTOforGetandGetAll> datas = null;

                    await Task.Run(() =>
                    {
                        datas = _mapper.Map<List<SessionQuestionDTOforGetandGetAll>>(_sessionQuestionReadRepository.GetAll(false).Where(i => i.SessionId == sessionId));
                    });

                    if (datas.Count <= 0)
                    {
                        throw new NotFoundException("SessionQuestion not found");
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

        public async Task SessionQuestionUpdate(SessionQuestionDTOforUpdate model, ClaimsPrincipal claimsPrincipal)
        {
            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {


                    var existing = await _sessionQuestionReadRepository.GetByIdAsync(model.Id.ToString(), false);
                    var existing2 = await _sessionReadRepository.GetByIdAsync(model.SessionId.ToString(), false);
                    var existing3 = await _questionReadRepository.GetByIdAsync(model.QuestionId.ToString(), false);




                    if (existing is null)
                    {
                        throw new NotFoundException("SessionQuestion not found");

                    }

                    if (existing2 is null)
                    {
                        throw new NotFoundException("Session not found");

                    }

                    if (existing3 is null)
                    {
                        throw new NotFoundException("Question not found");

                    }




                    var entity = _mapper.Map<SessionQuestion>(model);



                    entity = new SessionQuestion
                    {
                        Id = entity.Id,
                        Value = entity.Value,

                        SessionId = entity.SessionId,
                        QuestionId = entity.QuestionId,


                    };

                    _sessionQuestionWriteRepository.Update(entity);
                    await _sessionQuestionWriteRepository.SaveAsync();
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

        public async Task<SessionQuestionDTOforGetandGetAll> DeleteSessionQuestionById(int id, ClaimsPrincipal claimsPrincipal)
        {
            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {


                    if (_sessionQuestionReadRepository.GetAll().Any(i => i.Id == Convert.ToInt32(id)))
                    {

                        await _sessionQuestionWriteRepository.RemoveByIdAsync(id.ToString());
                        await _sessionQuestionWriteRepository.SaveAsync();

                        return null;

                    }

                    else
                    {
                        throw new NotFoundException("SessionQuestion not found");
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

        async Task<List<QuestionDTOforGetandGetAll>> SelectRandomItems(List<QuestionDTOforGetandGetAll> sourceList, int count, Random random, string levelname, ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    if (sourceList.Count <= count)
                    {
                        //return sourceList;

                        throw new NotFoundException($"Not enough questions on {levelname} level.");
                    }

                    var selectedItems = new List<QuestionDTOforGetandGetAll>();
                    var tempList = new List<QuestionDTOforGetandGetAll>(sourceList);

                    for (int i = 0; i < count; i++)
                    {
                        int randomIndex = random.Next(tempList.Count);
                        QuestionDTOforGetandGetAll randomItem = tempList[randomIndex];
                        selectedItems.Add(randomItem);
                        tempList.RemoveAt(randomIndex);
                    }

                    return selectedItems;
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


        public async Task<List<QuestionDTOforGetandGetAll>> GetRandomQuestion(RandomQuestionRequestModel model, ClaimsPrincipal claimsPrincipal)
        {
            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    //int questionCount, int structureId, int positionId, int vacantionId, int sessionId

                    List<QuestionDTOforGetandGetAll> easyList = new List<QuestionDTOforGetandGetAll>();
                    List<QuestionDTOforGetandGetAll> mediumList = new List<QuestionDTOforGetandGetAll>();
                    List<QuestionDTOforGetandGetAll> hardList = new List<QuestionDTOforGetandGetAll>();
                    List<QuestionDTOforGetandGetAll> randomList = new List<QuestionDTOforGetandGetAll>();

                    Random rnd = null;

                    await Task.Run(() =>
                    {
                        if (_mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false)).Count < model.QuestionCount)
                        {
                            throw new NotFoundException("Not enough questions");
                        }
                    });


                    await Task.Run(() =>
                    {
                        if (!_mapper.Map<List<StructureDTOforGetandGetAll>>(_structureReadRepository.GetAll(false)).Any(i => i.Id == model.StructureId))
                        {

                            throw new NotFoundException("Structure not found");
                        }
                    });

                    await Task.Run(() =>
                    {
                        if (!_mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false)).Any(i => i.StructureId == model.StructureId))
                        {
                            throw new NotFoundException("There are no questions about the selected structure.");
                        }
                    });

                    await Task.Run(() =>
                    {

                        if (!_mapper.Map<List<PositionDTOforGetandGetAll>>(_positionReadRepository.GetAll(false)).Any(i => i.Id == model.PositionId))
                        {
                            throw new NotFoundException("Position not found");
                        }
                    });

                    await Task.Run(() =>
                    {
                        if (!_mapper.Map<List<VacancyDTOforGetandGetAll>>(_vacancyReadRepository.GetAll(false)).Any(i => i.Id == model.VacantionId && i.PositionId == model.PositionId && i.StructureId == model.StructureId))
                        {
                            throw new NotFoundException("Vacancy not found");
                        }
                    });

                    await Task.Run(() =>
                    {
                        if (!_mapper.Map<List<SessionDTOforGetandGetAll>>(_sessionReadRepository.GetAll(false)).Any(i => i.Id == model.SessionId && i.VacancyId == model.VacantionId))
                        {
                            throw new NotFoundException("Session not found");
                        }
                    });



                    if (_mapper.Map<List<StructureDTOforGetandGetAll>>(_structureReadRepository.GetAll(false)).Any(i => i.Id == model.StructureId))
                    {
                        if (_mapper.Map<List<PositionDTOforGetandGetAll>>(_positionReadRepository.GetAll(false)).Any(i => i.Id == model.PositionId))
                        {
                            if (_mapper.Map<List<VacancyDTOforGetandGetAll>>(_vacancyReadRepository.GetAll(false)).Any(i => i.Id == model.VacantionId && i.PositionId == model.PositionId))
                            {
                                if (_mapper.Map<List<SessionDTOforGetandGetAll>>(_sessionReadRepository.GetAll(false)).Any(i => i.Id == model.SessionId && i.VacancyId == model.VacantionId))
                                {



                                    if (_mapper.Map<PositionDTOforGetandGetAll>(await _positionReadRepository.GetByIdAsync(model.PositionId.ToString(), false)).Name == "Junior")
                                    {
                                        rnd = new Random();

                                        await Task.Run(() =>
                                        {
                                            easyList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 1 && i.StructureId == model.StructureId));
                                        });

                                        await Task.Run(() =>
                                        {
                                            mediumList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 2 && i.StructureId == model.StructureId));
                                        });

                                        await Task.Run(() =>
                                        {
                                            hardList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 3 && i.StructureId == model.StructureId));
                                        });

                                
                                        randomList.AddRange(await SelectRandomItems(easyList, Convert.ToInt32(Math.Round(Convert.ToDouble(model.QuestionCount) * 50 / 100, MidpointRounding.AwayFromZero)), rnd, "easy", claimsPrincipal));
                                        randomList.AddRange(await SelectRandomItems(mediumList, Convert.ToInt32(Math.Round(Convert.ToDouble(model.QuestionCount) * 30 / 100, MidpointRounding.AwayFromZero)), rnd, "medium", claimsPrincipal));
                                        randomList.AddRange(await SelectRandomItems(hardList, Convert.ToInt32(Math.Round(Convert.ToDouble(model.QuestionCount) * 20 / 100, MidpointRounding.AwayFromZero)), rnd, "difficult", claimsPrincipal));


                                        await Task.Run(() =>
                                        {
                                            if (randomList.Count > model.QuestionCount)
                                            {
                                                var c = randomList.Count - model.QuestionCount;

                                                for (int i = 0; i < c; i++)
                                                {

                                                    randomList.RemoveAt(randomList.Count - 1);

                                                }
                                            }
                                        });


                                        //List<SessionQuestionDTOforGetandGetAll> sessionQuestions = null;

                                        //await Task.Run(() =>
                                        //{
                                        //    sessionQuestions = _mapper.Map<List<SessionQuestionDTOforGetandGetAll>>(_sessionQuestionReadRepository.GetAll(false));
                                        //});


                                        //foreach (var entity in sessionQuestions)
                                        //{
                                        //    await _sessionQuestionWriteRepository.RemoveByIdAsync(entity.Id.ToString());
                                        //}

                                        //await _sessionQuestionWriteRepository.SaveAsync();


                                        foreach (var entity in randomList)
                                        {
                                            var sessionQuestion = new SessionQuestion
                                            {
                                                QuestionId = entity.Id,
                                                SessionId = model.SessionId
                                            };

                                            await _sessionQuestionWriteRepository.AddAsync(sessionQuestion);
                                        }


                                        await _sessionQuestionWriteRepository.SaveAsync();





                                        //await Task.Run(() =>
                                        //{
                                        //    easyList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 1));

                                        //    var easyrandomNumbers = Enumerable.Range(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 1).FirstOrDefault().Id, _questionReadRepository.GetAll(false).Where(i => i.LevelId == 1).Count() - 1)
                                        //    .OrderBy(x => rnd.Next())
                                        //    .Take(Convert.ToInt32(Math.Round(Convert.ToDouble(questionCount) * 50 / 100, MidpointRounding.AwayFromZero)))
                                        //    .ToList();


                                        //    foreach (var id in easyrandomNumbers)
                                        //    {
                                        //        foreach (var item in easyList)
                                        //        {
                                        //            if (item.Id == id)
                                        //            {
                                        //                randomList.Add(item);
                                        //                break;
                                        //            }
                                        //        }
                                        //    }
                                        //});


                                        //await Task.Run(() =>
                                        //{
                                        //    mediumList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 2));

                                        //    var mediumrandomNumbers = Enumerable.Range(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 2).FirstOrDefault().Id, _questionReadRepository.GetAll(false).Where(i => i.LevelId == 2).Count() - 1)
                                        //   .OrderBy(x => rnd.Next())
                                        //   .Take(Convert.ToInt32(Math.Round(Convert.ToDouble(questionCount) * 30 / 100, MidpointRounding.AwayFromZero)))
                                        //   .ToList();

                                        //    foreach (var id in mediumrandomNumbers)
                                        //    {
                                        //        foreach (var item in mediumList)
                                        //        {
                                        //            if (item.Id == id)
                                        //            {
                                        //                randomList.Add(item);
                                        //                break;
                                        //            }
                                        //        }
                                        //    }

                                        //});


                                        //await Task.Run(() =>
                                        //{
                                        //    hardList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 3));

                                        //    var hardrandomNumbers = Enumerable.Range(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 3).FirstOrDefault().Id, _questionReadRepository.GetAll(false).Where(i => i.LevelId == 3).Count() - 1)
                                        //   .OrderBy(x => rnd.Next())
                                        //   .Take(Convert.ToInt32(Math.Round(Convert.ToDouble(questionCount) * 20 / 100, MidpointRounding.AwayFromZero)))
                                        //   .ToList();


                                        //    foreach (var id in hardrandomNumbers)
                                        //    {
                                        //        foreach (var item in hardList)
                                        //        {
                                        //            if (item.Id == id)
                                        //            {
                                        //                randomList.Add(item);
                                        //                break;
                                        //            }
                                        //        }
                                        //    }

                                        //});




                                    }

                                    if (_mapper.Map<PositionDTOforGetandGetAll>(await _positionReadRepository.GetByIdAsync(model.PositionId.ToString(), false)).Name == "Middle")
                                    {
                                        rnd = new Random();


                                        await Task.Run(() =>
                                        {
                                            easyList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 1 && i.StructureId == model.StructureId));
                                        });

                                        await Task.Run(() =>
                                        {
                                            mediumList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 2 && i.StructureId == model.StructureId));
                                        });

                                        await Task.Run(() =>
                                        {
                                            hardList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 3 && i.StructureId == model.StructureId));
                                        });


                                        randomList.AddRange(await SelectRandomItems(easyList, Convert.ToInt32(Math.Round(Convert.ToDouble(model.QuestionCount) * 50 / 100, MidpointRounding.AwayFromZero)), rnd, "easy", claimsPrincipal));
                                        randomList.AddRange(await SelectRandomItems(mediumList, Convert.ToInt32(Math.Round(Convert.ToDouble(model.QuestionCount) * 30 / 100, MidpointRounding.AwayFromZero)), rnd, "medium", claimsPrincipal));
                                        randomList.AddRange(await SelectRandomItems(hardList, Convert.ToInt32(Math.Round(Convert.ToDouble(model.QuestionCount) * 20 / 100, MidpointRounding.AwayFromZero)), rnd, "difficult", claimsPrincipal));




                                        //List<SessionQuestionDTOforGetandGetAll> sessionQuestions = null;

                                        //await Task.Run(() =>
                                        //{
                                        //    sessionQuestions = _mapper.Map<List<SessionQuestionDTOforGetandGetAll>>(_sessionQuestionReadRepository.GetAll(false));
                                        //});


                                        //foreach (var entity in sessionQuestions)
                                        //{
                                        //    await _sessionQuestionWriteRepository.RemoveByIdAsync(entity.Id.ToString());
                                        //}

                                        //await _sessionQuestionWriteRepository.SaveAsync();


                                        await Task.Run(() =>
                                        {
                                            if (randomList.Count > model.QuestionCount)
                                            {
                                                var c = randomList.Count - model.QuestionCount;

                                                for (int i = 0; i < c; i++)
                                                {

                                                    randomList.RemoveAt(randomList.Count - 1);

                                                }
                                            }
                                        });


                                        foreach (var entity in randomList)
                                        {
                                            var sessionQuestion = new SessionQuestion
                                            {
                                                QuestionId = entity.Id,
                                                SessionId = model.SessionId
                                            };

                                            await _sessionQuestionWriteRepository.AddAsync(sessionQuestion);
                                        }


                                        await _sessionQuestionWriteRepository.SaveAsync();


                                        //await Task.Run(() =>
                                        //{
                                        //    easyList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 1));

                                        //    var easyrandomNumbers = Enumerable.Range(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 1).FirstOrDefault().Id, _questionReadRepository.GetAll(false).Where(i => i.LevelId == 1).Count() - 1)
                                        //    .OrderBy(x => rnd.Next())
                                        //    .Take(Convert.ToInt32(Math.Round(Convert.ToDouble(questionCount) * 20 / 100, MidpointRounding.AwayFromZero)))
                                        //    .ToList();

                                        //    foreach (var id in easyrandomNumbers)
                                        //    {
                                        //        foreach (var item in easyList)
                                        //        {
                                        //            if (item.Id == id)
                                        //            {
                                        //                randomList.Add(item);
                                        //                break;
                                        //            }
                                        //        }
                                        //    }
                                        //});


                                        //await Task.Run(() =>
                                        //{
                                        //    mediumList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 2));

                                        //    var mediumrandomNumbers = Enumerable.Range(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 2).FirstOrDefault().Id, _questionReadRepository.GetAll(false).Where(i => i.LevelId == 2).Count() - 1)
                                        //   .OrderBy(x => rnd.Next())
                                        //   .Take(Convert.ToInt32(Math.Round(Convert.ToDouble(questionCount) * 50 / 100, MidpointRounding.AwayFromZero)))
                                        //   .ToList();

                                        //    foreach (var id in mediumrandomNumbers)
                                        //    {
                                        //        foreach (var item in mediumList)
                                        //        {
                                        //            if (item.Id == id)
                                        //            {
                                        //                randomList.Add(item);
                                        //                break;
                                        //            }
                                        //        }
                                        //    }

                                        //});


                                        //await Task.Run(() =>
                                        //{
                                        //    hardList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 3));

                                        //    var hardrandomNumbers = Enumerable.Range(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 3).FirstOrDefault().Id, _questionReadRepository.GetAll(false).Where(i => i.LevelId == 3).Count() - 1)
                                        //   .OrderBy(x => rnd.Next())
                                        //   .Take(Convert.ToInt32(Math.Round(Convert.ToDouble(questionCount) * 30 / 100, MidpointRounding.AwayFromZero)))
                                        //   .ToList();


                                        //    foreach (var id in hardrandomNumbers)
                                        //    {
                                        //        foreach (var item in hardList)
                                        //        {
                                        //            if (item.Id == id)
                                        //            {
                                        //                randomList.Add(item);
                                        //                break;
                                        //            }
                                        //        }
                                        //    }

                                        //});

                                    }

                                    if (_mapper.Map<PositionDTOforGetandGetAll>(await _positionReadRepository.GetByIdAsync(model.PositionId.ToString(), false)).Name == "Senior")
                                    {
                                        rnd = new Random();


                                        await Task.Run(() =>
                                        {
                                            easyList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 1 && i.StructureId == model.StructureId));
                                        });

                                        await Task.Run(() =>
                                        {
                                            mediumList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 2 && i.StructureId == model.StructureId));
                                        });

                                        await Task.Run(() =>
                                        {
                                            hardList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 3 && i.StructureId == model.StructureId));
                                        });


                                        randomList.AddRange(await SelectRandomItems(easyList, Convert.ToInt32(Math.Round(Convert.ToDouble(model.QuestionCount) * 50 / 100, MidpointRounding.AwayFromZero)), rnd, "easy", claimsPrincipal));
                                        randomList.AddRange(await SelectRandomItems(mediumList, Convert.ToInt32(Math.Round(Convert.ToDouble(model.QuestionCount) * 30 / 100, MidpointRounding.AwayFromZero)), rnd, "medium", claimsPrincipal));
                                        randomList.AddRange(await SelectRandomItems(hardList, Convert.ToInt32(Math.Round(Convert.ToDouble(model.QuestionCount) * 20 / 100, MidpointRounding.AwayFromZero)), rnd, "difficult", claimsPrincipal));



                                        await Task.Run(() =>
                                        {
                                            if (randomList.Count > model.QuestionCount)
                                            {
                                                var c = randomList.Count - model.QuestionCount;

                                                for (int i = 0; i < c; i++)
                                                {

                                                    randomList.RemoveAt(randomList.Count - 1);

                                                }
                                            }
                                        });


                                        //List<SessionQuestionDTOforGetandGetAll> sessionQuestions = null;

                                        //await Task.Run(() =>
                                        //{
                                        //    sessionQuestions = _mapper.Map<List<SessionQuestionDTOforGetandGetAll>>(_sessionQuestionReadRepository.GetAll(false));
                                        //});


                                        //foreach (var entity in sessionQuestions)
                                        //{
                                        //    await _sessionQuestionWriteRepository.RemoveByIdAsync(entity.Id.ToString());
                                        //}

                                        //await _sessionQuestionWriteRepository.SaveAsync();


                                        foreach (var entity in randomList)
                                        {
                                            var sessionQuestion = new SessionQuestion
                                            {
                                                QuestionId = entity.Id,
                                                SessionId = model.SessionId
                                            };

                                            await _sessionQuestionWriteRepository.AddAsync(sessionQuestion);
                                        }


                                        await _sessionQuestionWriteRepository.SaveAsync();




                                        //await Task.Run(() =>
                                        //{
                                        //    easyList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 1));

                                        //    var easyrandomNumbers = Enumerable.Range(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 1).FirstOrDefault().Id, _questionReadRepository.GetAll(false).Where(i => i.LevelId == 1).Count() - 1)
                                        //    .OrderBy(x => rnd.Next())
                                        //    .Take(Convert.ToInt32(Math.Round(Convert.ToDouble(questionCount) * 20 / 100, MidpointRounding.AwayFromZero)))
                                        //    .ToList();

                                        //    foreach (var id in easyrandomNumbers)
                                        //    {
                                        //        foreach (var item in easyList)
                                        //        {
                                        //            if (item.Id == id)
                                        //            {
                                        //                randomList.Add(item);
                                        //                break;
                                        //            }
                                        //        }
                                        //    }
                                        //});


                                        //await Task.Run(() =>
                                        //{
                                        //    mediumList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 2));

                                        //    var mediumrandomNumbers = Enumerable.Range(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 2).FirstOrDefault().Id, _questionReadRepository.GetAll(false).Where(i => i.LevelId == 2).Count() - 1)
                                        //   .OrderBy(x => rnd.Next())
                                        //   .Take(Convert.ToInt32(Math.Round(Convert.ToDouble(questionCount) * 30 / 100, MidpointRounding.AwayFromZero)))
                                        //   .ToList();

                                        //    foreach (var id in mediumrandomNumbers)
                                        //    {
                                        //        foreach (var item in mediumList)
                                        //        {
                                        //            if (item.Id == id)
                                        //            {
                                        //                randomList.Add(item);
                                        //                break;
                                        //            }
                                        //        }
                                        //    }

                                        //});


                                        //await Task.Run(() =>
                                        //{
                                        //    hardList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 3));

                                        //    var hardrandomNumbers = Enumerable.Range(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 3).FirstOrDefault().Id, _questionReadRepository.GetAll(false).Where(i => i.LevelId == 3).Count() - 1)
                                        //   .OrderBy(x => rnd.Next())
                                        //   .Take(Convert.ToInt32(Math.Round(Convert.ToDouble(questionCount) * 50 / 100, MidpointRounding.AwayFromZero)))
                                        //   .ToList();


                                        //    foreach (var id in hardrandomNumbers)
                                        //    {
                                        //        foreach (var item in hardList)
                                        //        {
                                        //            if (item.Id == id)
                                        //            {
                                        //                randomList.Add(item);
                                        //                break;
                                        //            }
                                        //        }
                                        //    }

                                        //});

                                    }





                                }
                            }

                        }

                    }
                    if (randomList.Count <= 0)
                    {
                        throw new NotFoundException("Question not found");
                    }


                    //if (randomList.Count < questionCount)
                    //{

                    //    throw new NotFoundException("Not enough questions");

                    //}



                    return randomList;
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

        public async Task<List<QuestionDTOforGetandGetAll>> GetRandomQuestion2(RandomQuestionRequestModel2 model, ClaimsPrincipal claimsPrincipal)
        {
            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    //int questionCount, int structureId, int positionId, int vacantionId, int sessionId

                    List<QuestionDTOforGetandGetAll> easyList = new List<QuestionDTOforGetandGetAll>();
                    List<QuestionDTOforGetandGetAll> mediumList = new List<QuestionDTOforGetandGetAll>();
                    List<QuestionDTOforGetandGetAll> hardList = new List<QuestionDTOforGetandGetAll>();
                    List<QuestionDTOforGetandGetAll> randomList = new List<QuestionDTOforGetandGetAll>();

                    Random rnd = null;



                    IEnumerable<QuestionDTOforGetandGetAll> questionQuery = null;
                    IEnumerable<PositionDTOforGetandGetAll> positonQuery = null;

                    await Task.Run(() =>
                    {

                        questionQuery =
                             from q in _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false))
                             join s in _mapper.Map<List<StructureDTOforGetandGetAll>>(_structureReadRepository.GetAll(false)) on q.StructureId equals s.Id
                             join v in _mapper.Map<List<VacancyDTOforGetandGetAll>>(_vacancyReadRepository.GetAll(false)) on s.Id equals v.StructureId
                             join p in _mapper.Map<List<PositionDTOforGetandGetAll>>(_positionReadRepository.GetAll(false)) on v.PositionId equals p.Id
                             join se in _mapper.Map<List<SessionDTOforGetandGetAll>>(_sessionReadRepository.GetAll(false)) on v.Id equals se.VacancyId
                             where v.Id == model.VacantionId && se.Id == model.SessionId && v.StructureId == s.Id
                             select new QuestionDTOforGetandGetAll
                             {
                                 Id = q.Id,
                                 Text = q.Text,
                                 LevelId = q.LevelId,
                                 CategoryId = q.CategoryId,
                                 StructureId = q.StructureId
                             };



                        positonQuery = from p in _mapper.Map<List<PositionDTOforGetandGetAll>>(_positionReadRepository.GetAll(false))
                                       join v in _mapper.Map<List<VacancyDTOforGetandGetAll>>(_vacancyReadRepository.GetAll(false)) on p.Id equals v.PositionId
                                       where v.Id == model.VacantionId
                                       select new PositionDTOforGetandGetAll
                                       {
                                           Id = p.Id,
                                           Name = p.Name,
                                       };
                    });

                    await Task.Run(() =>
                    {
                        if (_mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false)).Count < model.QuestionCount)
                        {
                            throw new NotFoundException("Not enough questions");
                        }
                    });





                    if (questionQuery.ToList().Count > 0)
                    {
                        if (positonQuery.ToList().Count > 0)
                        {





                            if (positonQuery.ToList().FirstOrDefault().Name == "Junior")
                            {
                                rnd = new Random();

                                await Task.Run(() =>
                                {
                                    easyList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 1 && i.StructureId == questionQuery.ToList().FirstOrDefault().StructureId));
                                });

                                await Task.Run(() =>
                                {
                                    mediumList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 2 && i.StructureId == questionQuery.ToList().FirstOrDefault().StructureId));
                                });

                                await Task.Run(() =>
                                {
                                    hardList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 3 && i.StructureId == questionQuery.ToList().FirstOrDefault().StructureId));
                                });

                                randomList.AddRange(await SelectRandomItems(easyList, Convert.ToInt32(Math.Round(Convert.ToDouble(model.QuestionCount) * 50 / 100, MidpointRounding.AwayFromZero)), rnd, "easy", claimsPrincipal));
                                randomList.AddRange(await SelectRandomItems(mediumList, Convert.ToInt32(Math.Round(Convert.ToDouble(model.QuestionCount) * 30 / 100, MidpointRounding.AwayFromZero)), rnd, "medium", claimsPrincipal));
                                randomList.AddRange(await SelectRandomItems(hardList, Convert.ToInt32(Math.Round(Convert.ToDouble(model.QuestionCount) * 20 / 100, MidpointRounding.AwayFromZero)), rnd, "difficult", claimsPrincipal));



                                await Task.Run(() =>
                                {
                                    if (randomList.Count > model.QuestionCount)
                                    {
                                        var c = randomList.Count - model.QuestionCount;

                                        for (int i = 0; i < c; i++)
                                        {

                                            randomList.RemoveAt(randomList.Count - 1);

                                        }
                                    }
                                });





                                foreach (var entity in randomList)
                                {
                                    var sessionQuestion = new SessionQuestion
                                    {
                                        QuestionId = entity.Id,
                                        SessionId = model.SessionId
                                    };

                                    await _sessionQuestionWriteRepository.AddAsync(sessionQuestion);
                                }


                                await _sessionQuestionWriteRepository.SaveAsync();










                            }

                            if (positonQuery.ToList().FirstOrDefault().Name == "Middle")
                            {
                                rnd = new Random();


                                await Task.Run(() =>
                                {
                                    easyList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 1 && i.StructureId == questionQuery.ToList().FirstOrDefault().StructureId));
                                });

                                await Task.Run(() =>
                                {
                                    mediumList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 2 && i.StructureId == questionQuery.ToList().FirstOrDefault().StructureId));
                                });

                                await Task.Run(() =>
                                {
                                    hardList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 3 && i.StructureId == questionQuery.ToList().FirstOrDefault().StructureId));
                                });


                                randomList.AddRange(await SelectRandomItems(easyList, Convert.ToInt32(Math.Round(Convert.ToDouble(model.QuestionCount) * 50 / 100, MidpointRounding.AwayFromZero)), rnd, "easy", claimsPrincipal));
                                randomList.AddRange(await SelectRandomItems(mediumList, Convert.ToInt32(Math.Round(Convert.ToDouble(model.QuestionCount) * 30 / 100, MidpointRounding.AwayFromZero)), rnd, "medium", claimsPrincipal));
                                randomList.AddRange(await SelectRandomItems(hardList, Convert.ToInt32(Math.Round(Convert.ToDouble(model.QuestionCount) * 20 / 100, MidpointRounding.AwayFromZero)), rnd, "difficult", claimsPrincipal));






                                await Task.Run(() =>
                                {
                                    if (randomList.Count > model.QuestionCount)
                                    {
                                        var c = randomList.Count - model.QuestionCount;

                                        for (int i = 0; i < c; i++)
                                        {

                                            randomList.RemoveAt(randomList.Count - 1);

                                        }
                                    }
                                });


                                foreach (var entity in randomList)
                                {
                                    var sessionQuestion = new SessionQuestion
                                    {
                                        QuestionId = entity.Id,
                                        SessionId = model.SessionId
                                    };

                                    await _sessionQuestionWriteRepository.AddAsync(sessionQuestion);
                                }


                                await _sessionQuestionWriteRepository.SaveAsync();



                            }

                            if (positonQuery.ToList().FirstOrDefault().Name == "Senior")
                            {
                                rnd = new Random();


                                await Task.Run(() =>
                                {
                                    easyList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 1 && i.StructureId == questionQuery.ToList().FirstOrDefault().StructureId));
                                });

                                await Task.Run(() =>
                                {
                                    mediumList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 2 && i.StructureId == questionQuery.ToList().FirstOrDefault().StructureId));
                                });

                                await Task.Run(() =>
                                {
                                    hardList = _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false).Where(i => i.LevelId == 3 && i.StructureId == questionQuery.ToList().FirstOrDefault().StructureId));
                                });


                                randomList.AddRange(await SelectRandomItems(easyList, Convert.ToInt32(Math.Round(Convert.ToDouble(model.QuestionCount) * 50 / 100, MidpointRounding.AwayFromZero)), rnd, "easy", claimsPrincipal));
                                randomList.AddRange(await SelectRandomItems(mediumList, Convert.ToInt32(Math.Round(Convert.ToDouble(model.QuestionCount) * 30 / 100, MidpointRounding.AwayFromZero)), rnd, "medium", claimsPrincipal));
                                randomList.AddRange(await SelectRandomItems(hardList, Convert.ToInt32(Math.Round(Convert.ToDouble(model.QuestionCount) * 20 / 100, MidpointRounding.AwayFromZero)), rnd, "difficult", claimsPrincipal));



                                await Task.Run(() =>
                                {
                                    if (randomList.Count > model.QuestionCount)
                                    {
                                        var c = randomList.Count - model.QuestionCount;

                                        for (int i = 0; i < c; i++)
                                        {

                                            randomList.RemoveAt(randomList.Count - 1);

                                        }
                                    }
                                });



                                foreach (var entity in randomList)
                                {
                                    var sessionQuestion = new SessionQuestion
                                    {
                                        QuestionId = entity.Id,
                                        SessionId = model.SessionId
                                    };

                                    await _sessionQuestionWriteRepository.AddAsync(sessionQuestion);
                                }


                                await _sessionQuestionWriteRepository.SaveAsync();




                            }


                        }
                    }



                    if (randomList.Count <= 0)
                    {
                        throw new NotFoundException("Question not found");
                    }






                    return randomList;
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

        public async Task<List<QuestionDTOforGetandGetAll>> GetAllQuestionByPage(QuestionByPageRequestModel questionByPageRequestModel, ClaimsPrincipal claimsPrincipal)
        {
            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {


                    await Task.Run(() =>
{
if (!_mapper.Map<List<VacancyDTOforGetandGetAll>>(_vacancyReadRepository.GetAll(false)).Any(i => i.Id == questionByPageRequestModel.VacantionId))
{
throw new NotFoundException("Vacancy not found");
}
});


                    IEnumerable<QuestionDTOforGetandGetAll> questionQuery = null;


                    await Task.Run(() =>
                    {

                        questionQuery = from q in _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false))
                                        join s in _mapper.Map<List<StructureDTOforGetandGetAll>>(_structureReadRepository.GetAll(false))
                                        on q.StructureId equals s.Id
                                        join v in _mapper.Map<List<VacancyDTOforGetandGetAll>>(_vacancyReadRepository.GetAll(false))
                                        on s.Id equals v.StructureId
                                        where v.Id == questionByPageRequestModel.VacantionId
                                        select new QuestionDTOforGetandGetAll
                                        {
                                            Id = q.Id,
                                            Text = q.Text,
                                            LevelId = q.LevelId,
                                            CategoryId = q.CategoryId,
                                            StructureId = q.StructureId,
                                        };

                    });





                    var questions = questionQuery.ToList();

                    List<QuestionDTOforGetandGetAll> pagequestion = null;

                    pagequestion = questions
                   .OrderBy(q => q.Id)
                   .Skip((questionByPageRequestModel.Page - 1) * questionByPageRequestModel.PageSize)
                    .Take(questionByPageRequestModel.PageSize)
                    .ToList();

                    if (pagequestion.Count == 0)
                    {

                        throw new NotFoundException("Page not found");
                    }

                    if (pagequestion.Count < questionByPageRequestModel.PageSize)
                    {

                        throw new NotFoundException("The page size is not enough");
                    }

                    return pagequestion;
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
