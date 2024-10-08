﻿using AutoMapper;
using Interview.Application.Exception;
using Interview.Application.Mapper.DTO.LevelDTO;
using Interview.Application.Mapper.DTO.QuestionDTO;
using Interview.Application.Mapper.DTO.SessionDTO;
using Interview.Application.Mapper.DTO.SessionQuestionDTO;
using Interview.Application.Repositories.Custom;
using Interview.Application.Services.Abstract;
using Interview.Domain.Entities.IdentityAuth;
using Interview.Domain.Entities.Models;
using Microsoft.AspNetCore.Identity;
using Microsoft.IdentityModel.Tokens;
using System.Security.Claims;

namespace Interview.Application.Services.Concrete
{
    public class SessionServiceManager : ISessionService
    {

        public readonly IMapper _mapper;


        private readonly ISessionWriteRepository _sessionWriteRepository;
        private readonly ISessionReadRepository _sessionReadRepository;

        private readonly IVacancyWriteRepository _vacancyWriteRepository;
        private readonly IVacancyReadRepository _vacancyReadRepository;

        private readonly ICandidateWriteRepository _candidateWriteRepository;
        private readonly ICandidateReadRepository _candidateReadRepository;

        private readonly ISessionQuestionWriteRepository _sessionQuestionWriteRepository;
        private readonly ISessionQuestionReadRepository _sessionQuestionReadRepository;


        private readonly IQuestionWriteRepository _questionWriteRepository;
        private readonly IQuestionReadRepository _questionReadRepository;

        private readonly ILevelWriteRepository _levelWriteRepository;
        private readonly ILevelReadRepository _levelReadRepository;

        private readonly IUserWriteRepository _userWriteRepository;
        private readonly IUserReadRepository _userReadRepository;



        public SessionServiceManager(IMapper mapper, ISessionWriteRepository sessionWriteRepository, ISessionReadRepository sessionReadRepository, IVacancyWriteRepository vacancyWriteRepository, IVacancyReadRepository vacancyReadRepository, ICandidateWriteRepository candidateWriteRepository, ICandidateReadRepository candidateReadRepository, ISessionQuestionWriteRepository sessionQuestionWriteRepository, ISessionQuestionReadRepository sessionQuestionReadRepository, IQuestionWriteRepository questionWriteRepository, IQuestionReadRepository questionReadRepository, ILevelWriteRepository levelWriteRepository, ILevelReadRepository levelReadRepository, IUserWriteRepository userWriteRepository, IUserReadRepository userReadRepository)
        {
            _mapper = mapper;
            _sessionWriteRepository = sessionWriteRepository;
            _sessionReadRepository = sessionReadRepository;
            _vacancyWriteRepository = vacancyWriteRepository;
            _vacancyReadRepository = vacancyReadRepository;
            _candidateWriteRepository = candidateWriteRepository;
            _candidateReadRepository = candidateReadRepository;
            _sessionQuestionWriteRepository = sessionQuestionWriteRepository;
            _sessionQuestionReadRepository = sessionQuestionReadRepository;
            _questionWriteRepository = questionWriteRepository;
            _questionReadRepository = questionReadRepository;
            _levelWriteRepository = levelWriteRepository;
            _levelReadRepository = levelReadRepository;
            _userWriteRepository = userWriteRepository;
            _userReadRepository = userReadRepository;
        }









        #region Session service manager

        public async Task SessionCreate(SessionDTOforCreate model, ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {


                    var entity = _mapper.Map<Session>(model);


                    var existing1 = await _vacancyReadRepository.GetByIdAsync(model.VacancyId.ToString(), false);
                    var existing2 = await _candidateReadRepository.GetByIdAsync(model.CandidateId.ToString(), false);

                    if (existing1 is null)
                    {
                        throw new NotFoundException("Vacancy not found");

                    }

                    if (existing2 is null)
                    {
                        throw new NotFoundException("Candidate not found");

                    }

                    var username = claimsPrincipal.Identity.Name;



                    entity = new Session
                    {

                        EndValue = entity.EndValue,
                        StartDate = entity.StartDate,
                        EndDate = entity.EndDate,
                        VacancyId = model.VacancyId,
                        CandidateId = model.CandidateId,
                        UserId = _userReadRepository.GetAll(false).AsEnumerable().Where(i => i.UserName == username).FirstOrDefault().Id,


                    };


                    await _sessionWriteRepository.AddAsync(entity);

                    await _sessionWriteRepository.SaveAsync();
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

        public async Task<List<SessionDTOforGetandGetAll>> GetSession(ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    List<SessionDTOforGetandGetAll> datas = null;

                    await Task.Run(() =>
                    {
                        datas = _mapper.Map<List<SessionDTOforGetandGetAll>>(_sessionReadRepository.GetAll(false));
                    });

                    if (datas.Count <= 0)
                    {
                        throw new NotFoundException("Session not found");
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

        public async Task<List<SessionDTOforGetandGetAll>> GetOwnSession(ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    

                    List<SessionDTOforGetandGetAll> datas = null;

                    await Task.Run(() =>
                    {
                        datas = _mapper.Map<List<SessionDTOforGetandGetAll>>(_sessionReadRepository.GetAll(false));
                    });

                    if (datas.Count <= 0)
                    {
                        throw new NotFoundException("Session not found");
                    }

                    var currentuserid = _userReadRepository.GetAll(false).AsEnumerable().Where(i => i.UserName == claimsPrincipal.Identity.Name).FirstOrDefault().Id;

                    if (_sessionReadRepository.GetAll(false).AsEnumerable().Any(i => i.UserId == currentuserid))
                    {

                        return datas.Where(i=>i.UserId==currentuserid).ToList();
                    }
                    else
                    {

                        throw new NotFoundException("You have not any sessions");
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


        public async Task<SessionDTOforGetandGetAll> GetSessionById(int id, ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {
                    SessionDTOforGetandGetAll item = null;


                    item = _mapper.Map<SessionDTOforGetandGetAll>(await _sessionReadRepository.GetByIdAsync(id.ToString(), false));


                    if (item == null)
                    {
                        throw new NotFoundException("Session not found");
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

        public async Task SessionUpdate(SessionDTOforUpdate model, ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {



                    await Task.Run(() =>
                    {

                        if (!_mapper.Map<List<SessionDTOforGetandGetAll>>(_sessionReadRepository.GetAll(false)).Any(i => i.Id == model.Id))
                        {
                            throw new NotFoundException("Session not found");
                        }
                    });


                    var currentuserid = _userReadRepository.GetAll(false).AsEnumerable().Where(i => i.UserName == claimsPrincipal.Identity.Name).FirstOrDefault().Id;

                    if (_sessionReadRepository.GetAll(false).AsEnumerable().Any(i => i.UserId == currentuserid))
                    {



                        var sessionQuery = from sq in _mapper.Map<List<SessionQuestionDTOforGetandGetAll>>(_sessionQuestionReadRepository.GetAll(false))
                                           join q in _mapper.Map<List<QuestionDTOforGetandGetAll>>(_questionReadRepository.GetAll(false)) on sq.QuestionId equals q.Id
                                           join s in _mapper.Map<List<SessionDTOforGetandGetAll>>(_sessionReadRepository.GetAll(false)) on sq.SessionId equals s.Id
                                           join l in _mapper.Map<List<LevelDTOforGetandGetAll>>(_levelReadRepository.GetAll(false)) on q.LevelId equals l.Id
                                           where s.Id == model.Id
                                           select new
                                           {
                                               SessionId = model.Id,
                                               EndValue = l.Coefficient * sq.Value,
                                               SessionEndDate = model.EndDate,

                                           };





                        var totalEndValue = sessionQuery.ToList().Sum(session => session.EndValue);

                        var entity = _mapper.Map<Session>(model);



                        entity = new Session
                        {
                            Id = model.Id,
                            EndValue = totalEndValue,
                            StartDate = _mapper.Map<SessionDTOforGetandGetAll>(await _sessionReadRepository.GetByIdAsync(model.Id.ToString(), false)).StartDate,
                            EndDate = model.EndDate,
                            VacancyId = _sessionReadRepository.GetAll(false).Where(i => i.Id == model.Id).FirstOrDefault().VacancyId,
                            CandidateId = _sessionReadRepository.GetAll(false).Where(i => i.Id == model.Id).FirstOrDefault().CandidateId,
                            UserId = _sessionReadRepository.GetAll(false).Where(i => i.Id == model.Id).FirstOrDefault().UserId,


                        };

                        _sessionWriteRepository.Update(entity);
                        await _sessionWriteRepository.SaveAsync();
                    }
                    else
                    {

                        throw new ForbiddenException("You don't have update permission");
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

        public async Task<SessionDTOforGetandGetAll> DeleteSessionById(int id, ClaimsPrincipal claimsPrincipal)
        {

            if (!_userReadRepository.GetAll(false).AsEnumerable().Any(i => string.IsNullOrEmpty(i.RefreshToken) && i.UserName == claimsPrincipal.Identity.Name))
            {
                if (claimsPrincipal.Identity.IsAuthenticated)
                {

                    if (_sessionReadRepository.GetAll().Any(i => i.Id == Convert.ToInt32(id)))
                    {
                      var currentuserid =  _userReadRepository.GetAll(false).AsEnumerable().Where(i => i.UserName == claimsPrincipal.Identity.Name).FirstOrDefault().Id;

                        if (_sessionReadRepository.GetAll(false).AsEnumerable().Any(i => i.UserId == currentuserid && i.Id==id))
                        {
                            var sessinquestions = _sessionQuestionReadRepository.GetAll(false).AsEnumerable().Where(i => i.SessionId == id);
                            foreach (var item in sessinquestions.ToList())
                            {
                                await _sessionQuestionWriteRepository.RemoveByIdAsync(item.Id.ToString());
                                await _sessionQuestionWriteRepository.SaveAsync();
                            }

                            await _sessionWriteRepository.RemoveByIdAsync(id.ToString());
                            await _sessionWriteRepository.SaveAsync();

                            return null;
                        }
                        else
                        {

                            throw new ForbiddenException("You don't have delete permission");
                        }

                    }

                    else
                    {
                        throw new NotFoundException("Session not found");
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
