

using Azure.Core;
using Interview.Application.Mapper.DTO.QuestionDTO;
using Interview.Application.Mapper.DTO.SessionQuestionDTO;
using Interview.Domain.Entities.Requests;

namespace Interview.Application.Services.Abstract
{
    public interface ISessionQuestionService
    {
        #region SessionQuestion service

        public Task SessionQuestionCreate(SessionQuestionDTOforCreate model);

        public Task<List<SessionQuestionDTOforGetandGetAll>> GetSessionQuestion();

        public Task<List<SessionQuestionDTOforGetandGetAll>> GetSessionQuestionBySessionId(int sessionId);

        public Task<SessionQuestionDTOforGetandGetAll> GetSessionQuestionById(int id);

        public Task SessionQuestionUpdate(SessionQuestionDTOforUpdate model);

        public Task<SessionQuestionDTOforGetandGetAll> DeleteSessionQuestionById(int id);

        public Task<List<QuestionDTOforGetandGetAll>> GetRandomQuestion(RandomQuestionRequestModel model);
        public Task<List<QuestionDTOforGetandGetAll>> GetRandomQuestion2(RandomQuestionRequestModel2 model);

        public Task<List<QuestionDTOforGetandGetAll>> GetAllQuestionByPage(QuestionByPageRequestModel questionByPageRequestModel);



        #endregion
    }


}
