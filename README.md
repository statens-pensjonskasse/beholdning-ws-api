# beholdning-ws-api

OpenAPI-spesifikasjon og ferdig klientbibliotek for å gjøre kall mot tjenesten.

## Utvikling

### Programvarekrav

Krav for å kunne bygge prosjektet lokalt:

* JDK
* Maven
* NPM
* Node
* Docker (valgfritt)

### Bygg

Kjør følgende kommando for å bygge prosjektet lokalt:

```shell
mvn clean install
```

### Contract-first API-utvikling

API-et skrives contract-first ved at man gjør nødvendige endringer i selve
OpenAPI-spesifikasjonen [her](api/openapi/openapi.yaml), som genereres til
klientbiblioteker for kjøremiljøene som typisk skal konsumere tjenesten.

For å se OpenAPI-definisjonen underveis mens den skrives, enten åpne filen i et
støttet verktøy (for eksempel IntelliJ), eller åpne en terminal og kjør:

```shell
docker compose up
```

Dette starter SwaggerUI på http://localhost:8099/ . Ha denne åpen og se
dokumentasjonen oppdatere seg automatisk mens du endrer på spesifikasjonen.

### Branch og release

1. Branch fra `main`.
2. Opprett pull-request og merge til `main`.
3. Release job [CI](http://jenkins.spk.no/job/PU_RET/job/CI/) vil starte automatisk.

## Hvordan bruke biblioteket?

### beholdning-ws-api-jvm-client

Dersom du skal gjøre kall mot tjenesten fra et JVM-basert kjøremiljø (Java,
Kotlin, osv), kan du legge til det ferdige klientbiblioteket som en avhengighet
i ditt prosjekt. Denne inneholder en tynn klient generert fra OpenAPI-specen.

```xml
<!-- add this to your pom.xml -->
<dependency>
    <groupId>no.spk.beholdning.ws</groupId>
    <artifactId>beholdning-ws-api-jvm-client</artifactId>
    <version>0.0.1</version>
</dependency>
```

Sjekk [GitHub](https://github.com/statens-pensjonskasse/beholdning-ws-api/releases)
for nyeste versjon av biblioteket.

#### Spring configuration

Opprett eksempelvis en `@Bean` ett sted i prosjektet ditt som bruker
`BeholdningHttpClientBuilder` til å bygge en `BeholdningHttpClient`:

```kotlin
@Configuration
class OutboundConfiguration {

    @Bean
    fun beholdningHttpClient(): BeholdningHttpClient {
        return BeholdningHttpClientBuilder()
            .withBaseUrl("https://beholdning-ws.kpt.spk.no")
            .withHeaderProvider(object : OutboundHeaderProvider {
                override fun getApplicationId() = "junit"
                override fun getCorrelationId() = "f8c"
                override fun getRequestOrigin() = "junit"
                override fun getAuthorization() = "Token secret"
            })
            .build()
    }
}
```

#### Generer din egen klient

Et bedre alternativ til å bruke den ferdige klienten er å generere din egen. Da
får du bedre kontroll over egen tech-stack og kan i større grad styre hvordan
den genererte koden skal se ut. Da kan du i stedet legge til en avhengighet til
en .zip-fil med selve OpenAPI-specen, og bruke denne som du vil.

```xml
<!-- add this to your pom.xml -->
<dependency>
    <groupId>no.spk.beholdning.ws</groupId>
    <artifactId>beholdning-ws-api</artifactId>
    <version>0.0.1</version>
    <type>zip</type>
    <classifier>openapi</classifier>
</dependency>
```

### beholdning-ws-api-jvm-server

Dersom du skal implementere tjenesten i en Spring-applikasjon, kan du legge til
det ferdige serverbiblioteket som avhengighet i ditt prosjekt. Denne inneholder
et server-side interface for en Spring controller generert fra OpenAPI-specen.

```xml
<!-- add this to your pom.xml -->
<dependency>
    <groupId>no.spk.beholdning.ws</groupId>
    <artifactId>beholdning-ws-api-jvm-server</artifactId>
    <version>0.0.1</version>
</dependency>
```

### beholdning-ws-api-jvm-model

Modellen for API-et, delt av klient og server, generert fra OpenAPI-specen.

```xml
<!-- add this to your pom.xml -->
<dependency>
    <groupId>no.spk.beholdning.ws</groupId>
    <artifactId>beholdning-ws-api-jvm-model</artifactId>
    <version>0.0.1</version>
</dependency>
```

### @spk/beholdning-ws-api-ts-client

Dersom du skal gjøre kall mot tjenesten fra et TypeScript-basert kjøremiljø,
kan du legge til det ferdige klientbiblioteket som en avhengighet i ditt
prosjekt. Denne inneholder en tynn klient generert fra OpenAPI-specen.

```json
{
  "@spk/beholdning-ws-api-ts-client": "^0.0.1"
}
```

#### Node/Express configuration

I din backend for frontend, opprett en funksjon som konfigurerer API-klienten.

```typescript
// apiClients.ts:
export const beholdningWs = (req: AppRequest) => {
    const applicationId = req.properties.applicationId;
    const host = req.properties.beholdningWsUrl;

    const headers: API.HTTPHeaders = {
        'x-application-id': applicationId,
        'x-correlation-id': req.headers['x-correlation-id'] as string,
        'x-request-origin': applicationId,
        Authorization: `Token ${req.cookies.iPlanetDirectoryPro}`,
    };

    const configuration = new Configuration({
        basePath: `${host}/api`,
        middleware: [],
        headers,
    });

    return new API.BeholdningApi(configuration);
};
```

Når dette er gjort, kan du for eksempel knytte denne til en controller for hvert
endepunkt du bruker fra frontend (dersom du ønsker 1-til-1 mot backend).

```typescript
// routes.ts:
export default function (app: Application, config: Config): void {
    const dist = path.join(__dirname, '../dist/');
    app.use('/', serveStatic(dist) as RequestHandler);
    app.post('/api/beholdning', BeholdningController.beregnBeholdning);
    app.use(errorHandlingMiddleware);
    app.use('/api-gateway/*splash', apiGatewayProxy(config.apiGatewayUrl));
}

// BeholdningController.ts:
export const beregnBeholdning = (req: AppRequest, res: Response) => {
    return beholdningWs(req)
        .beregnBeholdning({
            fødselsdato: '1963-09-19',
        })
        .then((result) => {
            res.send(result);
        });
};
```
