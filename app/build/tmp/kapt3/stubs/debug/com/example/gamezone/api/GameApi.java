package com.example.gamezone.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J$\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0014\b\u0001\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00070\u0006H\'J\u0014\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0003H\'\u00a8\u0006\u000b"}, d2 = {"Lcom/example/gamezone/api/GameApi;", "", "addGame", "Lretrofit2/Call;", "Lokhttp3/ResponseBody;", "gameData", "", "", "getGames", "", "Lcom/example/gamezone/models/Game;", "app_debug"})
public abstract interface GameApi {
    
    @retrofit2.http.GET(value = "games")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<java.util.List<com.example.gamezone.models.Game>> getGames();
    
    @retrofit2.http.POST(value = "games")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<okhttp3.ResponseBody> addGame(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.String> gameData);
}