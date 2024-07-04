using AutoMapper;
using Interview.Application.Exception;
using Interview.Application.Mapper.DTO.StructureTypeDTO;
using Interview.Application.Repositories.Custom;
using Interview.Application.Services.Abstract;
using Interview.Domain.Entities.Models;

namespace Interview.Application.Services.Concrete
{
    public class StructureTypeServiceManager : IStructureTypeService
    {

        public readonly IMapper _mapper;



        private readonly IStructureTypeWriteRepository _structureTypeWriteRepository;
        private readonly IStructureTypeReadRepository _structureTypeReadRepository;

        public StructureTypeServiceManager(IMapper mapper, IStructureTypeWriteRepository structureTypeWriteRepository, IStructureTypeReadRepository structureTypeReadRepository)
        {
            _mapper = mapper;
            _structureTypeWriteRepository = structureTypeWriteRepository;
            _structureTypeReadRepository = structureTypeReadRepository;
        }


        #region StructureType service manager

        public async Task StructureTypeCreate(StructureTypeDTOforCreate model)
        {



            var entity = _mapper.Map<StructureType>(model);

            entity = new StructureType
            {
                Name = model.Name,


            };


            await _structureTypeWriteRepository.AddAsync(entity);

            await _structureTypeWriteRepository.SaveAsync();
        }

        public async Task<List<StructureTypeDTOforGetandGetAll>> GetStructureType()
        {
            List<StructureTypeDTOforGetandGetAll> datas = null;

            await Task.Run(() =>
            {
                datas = _mapper.Map<List<StructureTypeDTOforGetandGetAll>>(_structureTypeReadRepository.GetAll(false));
            });

            if (datas.Count <= 0)
            {
                throw new NotFoundException("StructureType not found");
            }

            return datas;
        }

        public async Task<StructureTypeDTOforGetandGetAll> GetStructureTypeById(int id)
        {
            StructureTypeDTOforGetandGetAll item = null;


            item = _mapper.Map<StructureTypeDTOforGetandGetAll>(await _structureTypeReadRepository.GetByIdAsync(id.ToString(), false));


            if (item == null)
            {
                throw new NotFoundException("StructureType not found");
            }

            return item;
        }

        public async Task StructureTypeUpdate(StructureTypeDTOforUpdate model)
        {

            var existing = await _structureTypeReadRepository.GetByIdAsync(model.Id.ToString(), false);


            if (existing is null)
            {
                throw new NotFoundException("StructureType not found");

            }



            if (existing is null)
            {
                throw new NotFoundException("StructureType not found");

            }

            var entity = _mapper.Map<StructureType>(model);

            entity = new StructureType
            {
                Id = model.Id,
                Name = model.Name,

            };

            _structureTypeWriteRepository.Update(entity);
            await _structureTypeWriteRepository.SaveAsync();

        }

        public async Task<StructureTypeDTOforGetandGetAll> DeleteStructureTypeById(int id)
        {

            if (_structureTypeReadRepository.GetAll().Any(i => i.Id == Convert.ToInt32(id)))
            {

                await _structureTypeWriteRepository.RemoveByIdAsync(id.ToString());
                await _structureTypeWriteRepository.SaveAsync();

                return null;

            }

            else
            {
                throw new NotFoundException("StructureType not found");
            }
        }

        #endregion


    }


}
