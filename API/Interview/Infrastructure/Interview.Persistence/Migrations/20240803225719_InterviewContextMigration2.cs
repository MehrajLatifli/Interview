using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Interview.Persistence.Migrations
{
    /// <inheritdoc />
    public partial class InterviewContextMigration2 : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AlterColumn<decimal>(
                name: "EndValue",
                table: "Sessions",
                type: "decimal(18,2)",
                nullable: true,
                defaultValueSql: "((0.0))",
                oldClrType: typeof(decimal),
                oldType: "decimal(18,18)",
                oldNullable: true,
                oldDefaultValueSql: "((0.0))");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AlterColumn<decimal>(
                name: "EndValue",
                table: "Sessions",
                type: "decimal(18,18)",
                nullable: true,
                defaultValueSql: "((0.0))",
                oldClrType: typeof(decimal),
                oldType: "decimal(18,2)",
                oldNullable: true,
                oldDefaultValueSql: "((0.0))");
        }
    }
}
