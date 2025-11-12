package com.eldermoraes.aula06;

import com.eldermoraes.aula06.Ex1ThenApplyVsThenCompose.Profile;
import com.eldermoraes.aula06.Ex1ThenApplyVsThenCompose.User;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThenApplyVsThenCompose {


  record User(int id, String name) {}
  record Profile(int userId, String details) {}

  static CompletableFuture<Ex1ThenApplyVsThenCompose.User> getUserById(int id) {
    return CompletableFuture.supplyAsync(() -> {
      System.out.println("Buscando usuário " + id + "...");
      return new Ex1ThenApplyVsThenCompose.User(id, "Usuário " + id);
    });
  }

  static CompletableFuture<Ex1ThenApplyVsThenCompose.Profile> getProfileForUser(
      Ex1ThenApplyVsThenCompose.User user) {
    return CompletableFuture.supplyAsync(() -> {
      System.out.println("Buscando perfil para " + user.name() + "...");
      return new Ex1ThenApplyVsThenCompose.Profile(user.id(), "Detalhes do perfil...");
    });
  }

  public static void main(String[] args) {
    // Uso incorreto com thenApply para encadear operações assíncronas
    CompletableFuture<CompletableFuture<Ex1ThenApplyVsThenCompose.Profile>> nestedFuture =
        getUserById(101).thenApply(user -> getProfileForUser(user));
    System.out.println("Tipo de retorno com thenApply: " + nestedFuture.getClass().getSimpleName());

    // Uso correto com thenCompose para "achatar" o resultado
    CompletableFuture<Ex1ThenApplyVsThenCompose.Profile> flatFuture =
        getUserById(102).thenCompose(user -> getProfileForUser(user));
    System.out.println("Tipo de retorno com thenCompose: " + flatFuture.getClass().getSimpleName());

    Ex1ThenApplyVsThenCompose.Profile profile = flatFuture.join(); // Bloqueia para obter o resultado final
    System.out.println("Perfil obtido: " + profile);
  }
}