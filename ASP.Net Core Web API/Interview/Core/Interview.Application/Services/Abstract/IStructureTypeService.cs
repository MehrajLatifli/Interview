using Interview.Application.Mapper.DTO.StructureTypeDTO;

namespace Interview.Application.Services.Abstract
{
    public interface IStructureTypeService
    {
        #region StructureType service

        public Task StructureTypeCreate(StructureTypeDTOforCreate model);

        public Task<List<StructureTypeDTOforGetandGetAll>> GetStructureType();

        public Task<StructureTypeDTOforGetandGetAll> GetStructureTypeById(int id);

        public Task StructureTypeUpdate(StructureTypeDTOforUpdate model);

        public Task<StructureTypeDTOforGetandGetAll> DeleteStructureTypeById(int id);

        #endregion
    }


}
