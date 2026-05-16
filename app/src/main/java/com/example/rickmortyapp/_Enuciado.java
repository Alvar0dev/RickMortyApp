package com.example.rickmortyapp;


//public @interface _Enuciado {📝 EXAMEN PRÁCTICO PMDM: "Multiverse Explorer App"
//    Duración: 3 horas
//    Objetivo: Desarrollar una aplicación nativa en Android que consuma una API REST real, gestione una base de datos local, maneje intenciones implícitas para contenido multimedia y utilice los sensores del hardware.
//
//            🎯 PARTE 1: Requisitos Mínimos (Obligatorio para el 5)
//    Si no se completan estos puntos de forma funcional, el examen se considera suspenso.
//
//1. Consumo de API REST (1.5 puntos) - [Ref: PMDM05 - Gestión de la información]
//
//    Conecta la app con la API pública de Rick and Morty: [https://rickandmortyapi.com/api/character](https://rickandmortyapi.com/api/character)
//
//    Utiliza la librería Retrofit junto con un conversor GSON para realizar la solicitud HTTP y mapear la respuesta JSON a tus objetos modelo.
//
//    Debes procesar al menos la imagen, el nombre y el estado (vivo/muerto) del personaje.
//
//            2. Interfaz y Listado (1.5 puntos)
//
//    Crea un RecyclerView en la Activity principal que muestre la lista de personajes obtenidos.
//
//    Usa una librería como Glide, Picasso o Coil para descargar y mostrar las imágenes de forma asíncrona.
//
//    Cada tarjeta de la lista debe tener un botón o icono de "Guardar" (por ejemplo, una estrella o un portal).
//
//            3. Persistencia de Datos con SQLite (1 punto) - [Ref: PMDM05]
//
//    Al pulsar el botón de guardar, el personaje debe registrarse en una base de datos local creando una subclase de SQLiteOpenHelper.
//
//    Debes sobrescribir obligatoriamente los metods onCreate() (para crear la tabla mediante execSQL()) y onUpgrade().
//
//    Utiliza el metod insert() pasando un objeto ContentValues para evitar inyecciones SQL.
//
//            4. Navegación a Pantalla de Detalle (1 punto) - [Ref: PMDM08 - Intenciones]
//
//    Al pulsar sobre la tarjeta de un personaje, lanza un Intent explícito hacia una DetailActivity.
//
//    Utiliza un objeto Bundle con el metod putExtras() para enviar los datos detallados del personaje (nombre, imagen, especie) a la nueva actividad.
//
//            🚀 PARTE 2: Subir Nota (Hasta 8 puntos)
//    Estas tareas demuestran tu dominio sobre la experiencia de usuario y componentes avanzados.
//
//5. Actividad de Favoritos (0.5 puntos)
//
//     Añade un menú en la ActionBar o un botón flotante en la pantalla principal que abra una nueva Activity .
//
//    En esta Activity, utiliza el méto do query() o rawQuery() de SQLiteDatabase para recuperar todos los personajes guardados utilizando un Cursor, y muéstralos en un nuevo RecyclerView.
//
//            6. Soporte Landscape / Pantalla Horizontal (0.5 puntos)
//
//    Implementa un diseño alternativo creando el calificador de orientación correspondiente (layout-land).
//
//    En modo horizontal, la lista debe visualizarse en formato de cuadrícula (Grid).
//
//            7. Cambio de Idioma Dinámico (0.5 puntos)
//
//    Centraliza todos los literales de la app en el archivo res/values/strings.xml.
//
//    Crea un recurso alternativo para el idioma inglés (res/values-en/strings.xml). El sistema seleccionará automáticamente el texto adecuado según el idioma del dispositivo.
//
//            8. Reproducción Multimedia (1 punto) - [Ref: PMDM08]
//
//    En la pantalla de detalle del personaje, añade un botón para "Escuchar Banda Sonora".
//
//    Al pulsarlo, lanza un Intent implícito utilizando Intent.ACTION_VIEW. Utilizaremos siempre Spotify para los proveedores de contenido multimedia, así que la URI del Intent debe buscar directamente la banda sonora en esta plataforma (ej. [https://open.spotify.com/search/rick%20and%20morty](https://open.spotify.com/search/rick%20and%20morty)).
//
//            9. Exportar a JSON (0.5 puntos) - [Ref: PMDM05]
//
//    Añade un botón en tu vista de Favoritos que permita exportar los datos.
//
//    Utiliza el almacenamiento interno de la app (mediante openFileOutput()) para escribir un archivo personajes.json en modo MODE_PRIVATE.
//
//            🏆 PARTE 3: El Reto del Profesor (2 puntos extra para el 10)
//    Demuestra tus conocimientos avanzados sobre el hardware del dispositivo.
//
//10. El Agitado Dimensional (2 puntos) - [Ref: PMDM07 - Sensores]
//
//    Contexto: Vamos a usar los sensores del dispositivo para interactuar con la lista de favoritos de forma física.
//
//    Requisito 1: Utiliza el SensorManager para acceder al sensor de aceleración (TYPE_ACCELEROMETER), el cual mide las fuerzas aplicadas al dispositivo en los tres ejes (X, Y, Z).
//
//    Requisito 2: Registra tu Activity como SensorEventListener y sobrescribe el metod onSensorChanged().
//
//    Requisito 3: Implementa la lógica (aplicando si es necesario un filtro de paso bajo/alto) para detectar un movimiento fuerte de sacudida o "shake".
//
//    Requisito 4: Cuando el usuario agite físicamente el teléfono estando en la pantalla de Favoritos, debes ejecutar un metod delete() en tu base de datos SQLite para vaciar la lista, y actualizar automáticamente el RecyclerView.
//}
