package com.example.gamezone.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'J\u0014\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\u0003H\'J\u0018\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00032\b\b\u0001\u0010\f\u001a\u00020\rH\'J\u0018\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00032\b\b\u0001\u0010\f\u001a\u00020\u0010H\'J\"\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\f\u001a\u00020\u0013H\'\u00a8\u0006\u0014"}, d2 = {"Lcom/example/gamezone/api/AuthApi;", "", "deleteUser", "Lretrofit2/Call;", "Ljava/lang/Void;", "userId", "", "getAllUsers", "", "Lcom/example/gamezone/models/UserData;", "login", "Lcom/example/gamezone/models/LoginResponse;", "request", "Lcom/example/gamezone/models/LoginRequest;", "register", "Lcom/example/gamezone/models/RegisterResponse;", "Lcom/example/gamezone/models/RegisterRequest;", "updateUser", "Lcom/example/gamezone/models/UpdateUserResponse;", "Lcom/example/gamezone/models/UpdateUserRequest;", "app_debug"})
public abstract interface AuthApi {
    
    @retrofit2.http.POST(value = "login")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<com.example.gamezone.models.LoginResponse> login(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.gamezone.models.LoginRequest request);
    
    @retrofit2.http.POST(value = "register")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<com.example.gamezone.models.RegisterResponse> register(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.gamezone.models.RegisterRequest request);
    
    @retrofit2.http.PUT(value = "users/{id}")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<com.example.gamezone.models.UpdateUserResponse> updateUser(@retrofit2.http.Path(value = "id")
    int userId, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.gamezone.models.UpdateUserRequest request);
    
    @retrofit2.http.DELETE(value = "users/{id}")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<java.lang.Void> deleteUser(@retrofit2.http.Path(value = "id")
    int userId);
    
    @retrofit2.http.GET(value = "users")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<java.util.List<com.example.gamezone.models.UserData>> getAllUsers();
}