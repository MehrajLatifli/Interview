using Interview.Application.Mapper.DTO.LevelDTO;

namespace Interview.Application.Services.Abstract
{
    public interface ILevelService
    {
        #region Level service

        public Task LevelCreate(LevelDTOforCreate model);

        public Task<List<LevelDTOforGetandGetAll>> GetLevel();

        public Task<LevelDTOforGetandGetAll> GetLevelById(int id);

        public Task LevelUpdate(LevelDTOforUpdate model);

        public Task<LevelDTOforGetandGetAll> DeleteLevelById(int id);

        #endregion
    }


}
