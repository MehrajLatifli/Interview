#See https://aka.ms/customizecontainer to learn how to customize your debug container and how Visual Studio uses this Dockerfile to build your images for faster debugging.

FROM mcr.microsoft.com/dotnet/aspnet:7.0 AS base
WORKDIR /app
EXPOSE 80
EXPOSE 443

FROM mcr.microsoft.com/dotnet/sdk:7.0 AS build
ARG BUILD_CONFIGURATION=Release
WORKDIR /src
COPY ["Presentation/Interview.API/Interview.API.csproj", "Presentation/Interview.API/"]
COPY ["Core/Interview.Application/Interview.Application.csproj", "Core/Interview.Application/"]
COPY ["Core/Interview.Domain/Interview.Domain.csproj", "Core/Interview.Domain/"]
COPY ["Infrastructure/Interview.Infrastructure/Interview.Infrastructure.csproj", "Infrastructure/Interview.Infrastructure/"]
COPY ["Infrastructure/Interview.Persistence/Interview.Persistence.csproj", "Infrastructure/Interview.Persistence/"]
RUN dotnet restore "./Presentation/Interview.API/Interview.API.csproj"
COPY . .
WORKDIR "/src/Presentation/Interview.API"
RUN dotnet build "./Interview.API.csproj" -c $BUILD_CONFIGURATION -o /app/build

FROM build AS publish
ARG BUILD_CONFIGURATION=Release
RUN dotnet publish "./Interview.API.csproj" -c $BUILD_CONFIGURATION -o /app/publish /p:UseAppHost=false

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "Interview.API.dll"]