using AutoMapper;
using Interview.Application.Exception;
using Interview.Application.Mapper.DTO.LevelDTO;
using Interview.Application.Repositories.Custom;
using Interview.Application.Services.Abstract;
using Interview.Domain.Entities.Models;

namespace Interview.Application.Services.Concrete
{
    public class LevelServiceManager : ILevelService
    {

        public readonly IMapper _mapper;



        private readonly ILevelWriteRepository _levelWriteRepository;
        private readonly ILevelReadRepository _levelReadRepository;

        public LevelServiceManager(IMapper mapper, ILevelWriteRepository levelWriteRepository, ILevelReadRepository levelReadRepository)
        {
            _mapper = mapper;
            _levelWriteRepository = levelWriteRepository;
            _levelReadRepository = levelReadRepository;
        }



        #region Level service manager

        public async Task LevelCreate(LevelDTOforCreate model)
        {



            var entity = _mapper.Map<Level>(model);

            entity = new Level
            {
                Name = model.Name,
                Coefficient = model.Coefficient,

            };


            await _levelWriteRepository.AddAsync(entity);

            await _levelWriteRepository.SaveAsync();
        }

        public async Task<List<LevelDTOforGetandGetAll>> GetLevel()
        {
            List<LevelDTOforGetandGetAll> datas = null;

            await Task.Run(() =>
            {
                datas = _mapper.Map<List<LevelDTOforGetandGetAll>>(_levelReadRepository.GetAll(false));
            });

            if (datas.Count <= 0)
            {
                throw new NotFoundException("Level not found");
            }

            return datas;
        }

        public async Task<LevelDTOforGetandGetAll> GetLevelById(int id)
        {
            LevelDTOforGetandGetAll item = null;


            item = _mapper.Map<LevelDTOforGetandGetAll>(await _levelReadRepository.GetByIdAsync(id.ToString(), false));


            if (item == null)
            {
                throw new NotFoundException("Level not found");
            }

            return item;
        }

        public async Task LevelUpdate(LevelDTOforUpdate model)
        {

            var existing = await _levelReadRepository.GetByIdAsync(model.Id.ToString(), false);


            if (existing is null)
            {
                throw new NotFoundException("Level not found");

            }



            if (existing is null)
            {
                throw new NotFoundException("Level not found");

            }

            var entity = _mapper.Map<Level>(model);

            entity = new Level
            {
                Id = model.Id,
                Name = model.Name,
                Coefficient = model.Coefficient,

            };

            _levelWriteRepository.Update(entity);
            await _levelWriteRepository.SaveAsync();

        }

        public async Task<LevelDTOforGetandGetAll> DeleteLevelById(int id)
        {

            if (_levelReadRepository.GetAll().Any(i => i.Id == Convert.ToInt32(id)))
            {

                await _levelWriteRepository.RemoveByIdAsync(id.ToString());
                await _levelWriteRepository.SaveAsync();

                return null;

            }

            else
            {
                throw new NotFoundException("Level not found");
            }
        }

        #endregion






    }


}
