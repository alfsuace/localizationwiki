# LocalizationWiki

LocalizationWiki es una aplicación que permite consultar y visualizar artículos localizados desde una API remota. Aunque su funcionalidad principal no requería caché, se decidió implementar un sistema de almacenamiento en caché como experimento para practicar la gestión de recursos (API y batería).

## Características

- Consulta de artículos desde una API remota (WikiPedia).
- Caché local de los artículos para reducir llamadas a la API y consumo de batería.
- Arquitectura modular con separación clara de capas.
- Pruebas unitarias y de ViewModel.

## Arquitectura

El proyecto está diseñado bajo la arquitectura **MVVM** (Modelo–Vista–ViewModel):

### Modelo (Data Layer)

- Repositorio que maneja datos remotos y locales.
- Data Sources:
  - Fuente remota: API HTTP.
  - Fuente local (caché): almacenamiento en base local, usando **Room**.

### ViewModel

- Gestiona la lógica de presentación y coordina las peticiones al repositorio.
- Exposición de **StateFlows** para que la UI las observe.

### Vista (UI)

- Actividades o fragments que observan los cambios del ViewModel.
- Se actualizan cuando los datos llegan ya sea desde la caché o la red.

## Gestión de Caché

Aunque la caché no era estrictamente necesaria para la funcionalidad básica solicitada, se implementó voluntariamente como una prueba de gestión de recursos con dos objetivos:

- **Reducir el uso de la API:** al guardar las respuestas localmente, se minimizan las peticiones repetidas, lo que puede ahorrar cuota de API o coste si la API es limitada.
- **Ahorro de batería:** al evitar llamadas de red innecesarias, se reduce la actividad de red, lo que puede traducirse en un menor consumo de batería para los usuarios.

Este sistema de caché incluye lógica para invalidar datos obsoletos (según tiempo) y para comprobar cuándo debe usarse la versión en caché frente a una nueva descarga desde la API.

## Librerías

Algunas de las librerías utilizadas:

- **Retrofit / OkHttp:** para las llamadas HTTP a la API remota.
- **Gson:** para la (de)serialización de JSON.
- **Room:** para el almacenamiento de la caché local.
- **Kotlin Coroutines** (o ExecutorService / hilos): para operaciones asíncronas (red, base de datos).
- **StateFlow:** para exponer los datos desde el ViewModel.
- **JUnit / MockK:** para las pruebas unitarias y de ViewModel.
- **Koin:** Inyector de dependencias.

## Tests

Se han escrito tests para el ViewModel, pero hay dos tests comentados que fallan actualmente. El motivo es que:

- Los tests terminan antes de que el hilo en el ViewModel o las operaciones asíncronas completen su ejecución.
- Como consecuencia, las aserciones no tienen los datos esperados, ya que el ViewModel no ha terminado de cargar los datos ni emitir los estados correspondientes.


## CI / Github pipelines
- Aún no implementadas
