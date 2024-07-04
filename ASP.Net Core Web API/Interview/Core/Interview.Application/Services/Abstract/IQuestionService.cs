using Interview.Application.Mapper.DTO.QuestionDTO;

namespace Interview.Application.Services.Abstract
{
    public interface IQuestionService
    {

        #region Question service

        public Task QuestionCreate(QuestionDTOforCreate model);

        public Task<List<QuestionDTOforGetandGetAll>> GetQuestion();

        public Task<QuestionDTOforGetandGetAll> GetQuestionById(int id);

        public Task QuestionUpdate(QuestionDTOforUpdate model);

        public Task<QuestionDTOforGetandGetAll> DeleteQuestionById(int id);

        #endregion
    }


}
