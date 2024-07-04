using Interview.Application.Repositories.Abstract;
using Interview.Domain.Entities.Base;
using Interview.Persistence.Contexts.InterviewDbContext;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.ChangeTracking;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Text;
using System.Threading.Tasks;

namespace Interview.Persistence.Repositories.Concrete
{
    public class WriteRepository<T> : IWriteRepository<T> where T : BaseEntity
    {

        private readonly InterviewContext _context;

        public WriteRepository(InterviewContext context)
        {
            _context = context;
        }

        public DbSet<T> Table => _context.Set<T>();

        public async Task<bool> AddAsync(T entity)
        {

            EntityEntry<T> entityEntry = await Table.AddAsync(entity);
            return entityEntry.State == EntityState.Added;


        }

        public async Task<bool> AddRangeAsync(List<T> entities)
        {
            await Table.AddRangeAsync(entities);

            return true;
        }

        public bool Remove(T entity)
        {
            EntityEntry<T> entityEntry = Table.Remove(entity);

            return entityEntry.State == EntityState.Deleted;
        }

        public async Task<bool> RemoveByIdAsync(string id)
        {
            T model = await Table.FirstOrDefaultAsync(data => data.Id == Convert.ToInt32(id));

            return Remove(model);
        }

        public bool RemoveRange(List<T> entities)
        {
            Table.RemoveRange(entities);

            return true;
        }


        public bool Update(T entity)
        {
            EntityEntry<T> entityEntry = Table.Update(entity);

            return entityEntry.State == EntityState.Modified;
        }


        public async Task<int> SaveAsync()
        {
            return await _context.SaveChangesAsync();
        }


    }
}
