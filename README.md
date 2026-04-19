app/
├── data/
│   ├── api/
│   │   ├── StackExchangeApi.kt       # Retrofit interface
│   │   └── dto/UserDto.kt            # JSON response model
│   └── repository/
│       └── UserRepositoryImpl.kt     # API calls + Flow emission
├── di/
│   ├── AppModule.kt                  # Binds Repository interface
│   └── NetworkModule.kt             # Provides Retrofit, OkHttpClient
├── domain/
│   ├── model/User.kt                 # Clean domain model
│   ├── repository/UserRepository.kt  # Repository interface
│   └── usecase/
│       ├── SearchUsersUseCase.kt
│       └── GetUserByIdUseCase.kt
└── presentation/
    ├── navigation/AppNavigation.kt   # NavController + routes
    ├── search/
    │   ├── SearchScreen.kt
    │   ├── SearchViewModel.kt
    │   └── SearchUiState.kt
    └── detail/
        ├── UserDetailScreen.kt
        ├── UserDetailViewModel.kt
        └── UserDetailUiState.kt
