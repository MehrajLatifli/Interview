

using Azure.Core;
using Interview.Application.Mapper.DTO.QuestionDTO;
using Interview.Application.Mapper.DTO.SessionQuestionDTO;
using Interview.Domain.Entities.Requests;
using System.Security.Claims;

namespace Interview.Application.Services.Abstract
{
    public interface ISessionQuestionService
    {
        #region SessionQuestion service

        public Task SessionQuestionCreate(SessionQuestionDTOforCreate model, ClaimsPrincipal claimsPrincipal);

        public Task<List<SessionQuestionDTOforGetandGetAll>> GetSessionQuestion(ClaimsPrincipal claimsPrincipal);

        public Task<List<SessionQuestionDTOforGetandGetAll>> GetSessionQuestionBySessionId(int sessionId, ClaimsPrincipal claimsPrincipal);

        public Task<SessionQuestionDTOforGetandGetAll> GetSessionQuestionById(int id, ClaimsPrincipal claimsPrincipal);

        public Task SessionQuestionUpdate(SessionQuestionDTOforUpdate model, ClaimsPrincipal claimsPrincipal);

        public Task<SessionQuestionDTOforGetandGetAll> DeleteSessionQuestionById(int id, ClaimsPrincipal claimsPrincipal);

        public Task<List<QuestionDTOforGetandGetAll>> GetRandomQuestion(RandomQuestionRequestModel model, ClaimsPrincipal claimsPrincipal);
        public Task<List<QuestionDTOforGetandGetAll>> GetRandomQuestion2(RandomQuestionRequestModel2 model, ClaimsPrincipal claimsPrincipal);

        public Task<List<QuestionDTOforGetandGetAll>> GetAllQuestionByPage(QuestionByPageRequestModel questionByPageRequestModel, ClaimsPrincipal claimsPrincipal);



        #endregion
    }


}
