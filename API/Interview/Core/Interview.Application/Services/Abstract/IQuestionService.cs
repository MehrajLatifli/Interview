using Interview.Application.Mapper.DTO.QuestionDTO;
using System.Security.Claims;

namespace Interview.Application.Services.Abstract
{
    public interface IQuestionService
    {

        #region Question service

        public Task QuestionCreate(QuestionDTOforCreate model, ClaimsPrincipal claimsPrincipal);

        public Task<List<QuestionDTOforGetandGetAll>> GetQuestion(ClaimsPrincipal claimsPrincipal);

        public Task<QuestionDTOforGetandGetAll> GetQuestionById(int id, ClaimsPrincipal claimsPrincipal);

        public Task QuestionUpdate(QuestionDTOforUpdate model, ClaimsPrincipal claimsPrincipal);

        public Task<QuestionDTOforGetandGetAll> DeleteQuestionById(int id, ClaimsPrincipal claimsPrincipal);

        #endregion
    }


}
