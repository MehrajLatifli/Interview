using Interview.Application.Mapper.DTO.PositionDTO;

namespace Interview.Application.Services.Abstract
{
    public interface IPositionService
    {


        #region Position service

        public Task PositionCreate(PositionDTOforCreate model);

        public Task<List<PositionDTOforGetandGetAll>> GetPosition();

        public Task<PositionDTOforGetandGetAll> GetPositionById(int id);

        public Task PositionUpdate(PositionDTOforUpdate model);

        public Task<PositionDTOforGetandGetAll> DeletePositionById(int id);

        #endregion

    }

}
