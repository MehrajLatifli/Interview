using Interview.Application.Mapper.DTO.StructureDTO;

namespace Interview.Application.Services.Abstract
{
    public interface IStructureService
    {
        #region Structure service

        public Task StructureCreate(StructureDTOforCreate model);

        public Task<List<StructureDTOforGetandGetAll>> GetStructure();

        public Task<StructureDTOforGetandGetAll> GetStructureById(int id);

        public Task StructureUpdate(StructureDTOforUpdate model);

        public Task<StructureDTOforGetandGetAll> DeleteStructureById(int id);

        #endregion
    }


}
