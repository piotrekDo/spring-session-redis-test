Aby zainstalować Dockera na Ubuntu (w tym np. WSL2 Ubuntu na Windows), wykonaj poniższe kroki — są proste i szybkie:

✅ Krok 1: Zaktualizuj system
sudo apt update
sudo apt upgrade -y

✅ Krok 2: Zainstaluj zależności
sudo apt install -y ca-certificates curl gnupg lsb-release


✅ Krok 3: Dodaj klucz GPG Dockera
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | \
sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg


✅ Krok 4: Dodaj repozytorium Dockera
echo \
"deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
https://download.docker.com/linux/ubuntu \
$(lsb_release -cs) stable" | \
sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

✅ Krok 5: Zainstaluj Dockera
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

✅ Krok 6: Uruchom Dockera i dodaj siebie do grupy (opcjonalnie)
sudo usermod -aG docker $USER

✅ Sprawdź, czy Docker działa:
docker --version
docker run hello-world

## MQ RABBIT

dla spring boot 3.4.4 konieczna jest zależnosć dla Netty.

Netty to asynchroniczny framework do programowania sieciowego w Javie, który umożliwia wydajną i nieblokującą obsługę komunikacji TCP/UDP. W Spring Boot 3, gdy używasz StompBrokerRelay do komunikacji ze zdalnym brokerem STOMP (np. RabbitMQ), Spring wykorzystuje Netty jako klienta TCP do połączenia i wymiany wiadomości ze STOMP brokerem. Bez Netty nie ma niskopoziomowego mechanizmu transportu TCP potrzebnego do tego połączenia, dlatego dodanie zależności na spring-boot-starter-reactor-netty jest konieczne.
Netty pełni rolę tego niskopoziomowego, asynchronicznego łącza (connectora), który realizuje komunikację TCP między  aplikacją Spring a RabbitMQ. Dzięki temu Spring może wysyłać i odbierać wiadomości przez STOMP Relay, niezależnie od lokalizacji brokera. Teoretycznie Rabbit MQ może znajdować się na innej maszynie niż localhost

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-reactor-netty</artifactId>
</dependency>


1. Uruchom RabbitMQ w Dockerze
   W terminalu (tam, gdzie masz działającego Dockera) wpisz:
   docker run -d --hostname rabbit --name rabbitmq -p 15672:15672 -p 61613:61613 rabbitmq:3-management
   -d — uruchamia w tle
   --hostname rabbit — nazwa hosta w kontenerze
   --name rabbitmq — nazwa kontenera
   -p 15672:15672 — port panelu zarządzania RabbitMQ
   -p 61613:61613 — port STOMP (do komunikacji WebSocket w Springu)
   rabbitmq:3-management — oficjalny obraz RabbitMQ z panelem zarządzania

2. Sprawdź, czy RabbitMQ działa
   Otwórz w przeglądarce:
   http://localhost:15672
   login: guest
   haslo: guest

3. Konfiguracja Spring Boot do korzystania z RabbitMQ jako STOMP Brokera
   W Twojej klasie konfiguracji WebSocket (WebSocketConfig) zamień:
   config.enableSimpleBroker("/topic", "/queue");
   NA:
   config.enableStompBrokerRelay("/topic", "/queue")
   .setRelayHost("localhost")
   .setRelayPort(61613)
   .setClientLogin("guest")
   .setClientPasscode("guest");

3.A Rabbit prawdopodobnie ma wyłączoną obsługę STOMP to uniemożliwi działanie websocket.
Jak sprawdzić aktywne pluginy RabbitMQ w kontenerze Docker?
Wejdź do działającego kontenera RabbitMQ:
docker exec -it bab5d1bf182a bash
(zamień bab5d1bf182a na swój aktualny CONTAINER ID z docker ps)

W kontenerze uruchom:
rabbitmq-plugins list

Poszukaj w liście czy jest włączony plugin rabbitmq_stomp — powinien mieć status [E*] (enabled).
Jeśli plugin STOMP nie jest włączony:

rabbitmq-plugins enable rabbitmq_stomp
rabbitmq-plugins enable rabbitmq_management
Po tym najlepiej jest zrestartować kontener
docker restart bab5d1bf182a


4. Uruchom swoją aplikację Spring Boot
   Teraz Twoja aplikacja WebSocket korzysta z RabbitMQ jako brokera.

5. Testowanie
   Klient React łączy się tak samo z Twoim endpointem STOMP (/ws)
   Wiadomości są przesyłane przez RabbitMQ
   Możesz monitorować kolejki, połączenia, itp. w panelu RabbitMQ na localhost:15672

Podsumowanie poleceń:
# Uruchom RabbitMQ
docker run -d --hostname rabbit --name rabbitmq -p 15672:15672 -p 61613:61613 rabbitmq:3-management
# Sprawdź działanie panelu zarządzania: http://localhost:15672 (guest/guest)

