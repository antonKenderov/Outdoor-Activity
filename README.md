# Outdoor Activity

Spring Boot приложение, което следи метеорологичната прогноза (Open-Meteo) и известява по имейл кога условията са подходящи за дадени спортове (напр. бадминтон, футбол), спрямо зададени критерии (температура, вятър, вероятност за валежи, облачност, дневна светлина, минимална продължителност). При намерено подходящо време, освен имейл, приложението създава и събитие в Google Calendar.

## Какво прави програмата

- На фиксиран интервал (`notification.check-interval-ms`) проверява прогнозата за конфигурираните координати.
- За всеки спорт от `sports-config.json` изчислява кои часове от денонощието отговарят на критериите (температура, вятър, дъжд, облачност, дневна светлина).
- Групира последователните подходящи часове в интервали с минимална продължителност (`minDurationHours`).
- За всеки получател от `notification-config.json` филтрира резултатите по ден от седмицата (`weekendOnly`) и часови диапазон (`notifyBetween`).
- Ако има съвпадения, изпраща HTML имейл (през SMTP/Gmail) и създава събитие в Google Calendar с напомняния.
- Изложен е и REST endpoint за директна проверка на подходящите часове за даден спорт и координати:
  - `GET /sports/{sport}?latitude={lat}&longitude={lon}`
- Spring Boot Actuator е активиран за `health` и `info` (`/actuator/health`, `/actuator/info`).

## Изисквания

- **Java 21** (JDK)
- **Maven** (или използвай включения wrapper `mvnw` / `mvnw.cmd`)
- Работещ **Gmail акаунт** с генериран App Password (за SMTP изпращане на имейли)
- **Google Cloud проект** с включено **Google Calendar API** и OAuth 2.0 Client ID (Desktop app) — за файла `credentials.json`
- Интернет достъп до `api.open-meteo.com` (Open-Meteo не изисква API ключ)

## Стартиране

```bash
# от корена на проекта
./mvnw spring-boot:run        # Linux/macOS
mvnw.cmd spring-boot:run       # Windows

# или чрез build + jar
./mvnw clean package
java -jar target/outdoor-activity-0.0.1-SNAPSHOT.jar
```

По подразбиране приложението стартира на `http://localhost:8080` (`server.port`), с активен профил `local` (`spring.profiles.active=local`).

При **първо стартиране** Google OAuth flow-ът ще отвори браузър (`LocalServerReceiver`) за логин и разрешаване на достъп до Google Calendar. След успешна авторизация токенът се пази локално в директория `tokens/` (създава се автоматично) и няма да се иска повторно, докато не бъде изтрит или изтече.

## Конфигурационни файлове

Всички са в `src/main/resources/`.

### `application-local.properties` (профил `local`, **съдържа тайни, не се качва в git**)

| Поле | Описание |
|---|---|
| `spring.mail.username` | Gmail адрес, от който се изпращат имейлите |
| `spring.mail.password` | Gmail **App Password** (не обикновена парола на акаунта) |
| `weather.api.url` | URL шаблон към Open-Meteo API (hourly параметри: вятър, вероятност за дъжд, ден/нощ, температура, облачност) |

### `sports-config.json`
Дефиниция на спортовете и метеорологичните критерии, при които се считат за подходящи.

| Поле | Описание |
|---|---|
| `name` | Име на спорта (използва се като ключ в endpoint-а и в `notification-config.json`) |
| `minTemperature` / `maxTemperature` | Допустим температурен диапазон (°C) |
| `maxWindSpeed` | Максимална скорост на вятъра (пориви, m/s) |
| `maxRainProbability` | Максимална вероятност за валеж (%) |
| `maxCloudCover` | Максимална облачност (%) |
| `requiresDaylight` | Дали е нужна дневна светлина |
| `minDurationHours` | Минимална продължителност на подходящия интервал (часове) |

### `notification-config.json`
Списък с получатели и техните критерии за известяване, за всеки спорт.

| Поле | Описание |
|---|---|
| `sport` | Спорт, за който важи записа (трябва да съвпада с `name` от `sports-config.json`) |
| `weekendOnly` | Дали да се известява само за събота/неделя |
| `notifyBetween.from` / `notifyBetween.to` | Часови диапазон за известяване, формат `HH:mm` |
| `email` | Имейл адрес на получателя |

## Какво трябва да се добави/настрои, за да работи без проблем

1. **`src/main/resources/application-local.properties`** — създай файла (ако липсва) и попълни:
   - `spring.mail.username` с твоя Gmail адрес
   - `spring.mail.password` с Gmail **App Password** (Google акаунт → Security → 2-Step Verification → App passwords), не обикновената парола
   - `weather.api.url` (може да остане както е зададен по подразбиране)
2. **`src/main/resources/credentials.json`** — създай Google Cloud проект, включи Calendar API, генерирай OAuth Client ID (Desktop app) и запази изтегления JSON тук.
3. **Google Calendar достъп** — при първо стартиране в браузъра ще трябва да логнеш акаунта, в чийто календар да се създават събития. Ако акаунтът е "External" в OAuth consent screen и не е publish-нат, трябва да е добавен като test user.
4. **Директория `tokens/`** — създава се автоматично при първи успешен OAuth login; ако сменяш Google акаунт или искаш нова авторизация, изтрий я.
5. **Координати и интервал** — коригирай `notification.latitude`, `notification.longitude` и `notification.check-interval-ms` в `application.properties` според нуждите.
6. **`sports-config.json` / `notification-config.json`** — добави/коригирай спортовете и получателите според нуждите; `sport` стойностите в двата файла трябва да съвпадат.

> ⚠️ И двата файла `application-local.properties` и `credentials.json` съдържат тайни (SMTP парола, OAuth client secret) и **не трябва да се качват в git**. Проверени са в `.gitignore`, но текущо `application-local.properties` е добавен като staged файл (`git add`) — провери и го извади от staging (`git restore --staged src/main/resources/application-local.properties`), за да не изтече паролата в историята на git-а.

