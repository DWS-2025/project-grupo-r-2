# Spartan Wrath

<h1 >Hola 👋, Somos Spartan's Wrath</h1>
<h3 >Grupo R-2 de Desarrollo Web Seguro</h3> 

## 📌 Descripción de la Aplicación Web

**Spartan Wrath** es la página web de un gimnasio dedicado a diferentes artes marciales, tiene una tienda y da la opción de suscribirse a diferentes membresías, crear una cuenta...

## 👥 Integrantes del equipo de desarrollo

| Nombre                  | Usuario GitHub  | Correo universitario               |
|-------------------------|----------------|-----------------------------------|
| Daniel Vallejo Pasamón  | dvallejo99     | [d.vallejo.2021@alumnos.urjc.es](mailto:d.vallejo.2021@alumnos.urjc.es) |
| Cesar Martín Baños      | Cesarmb3       | [c.martinb.2021@alumnos.urjc.es](mailto:c.martinb.2021@alumnos.urjc.es) |

## 📄 Entidades

La aplicación gestiona las siguientes entidades principales:
- **Usuario**: Para diferenciar entre los diferentes clientes y gestionar los roles.
- **Membresía**: Diferentes suscripciones que puede tener el cliente.
- **Clase de combate**: Clases que imparte el gimnasio.
- **Producto**: Diferentes objetos que pueden comprar los clientes.

### Relación entre Entidades
- Un Usuario puede tener varios productos, puede estar suscrito a una membresía.
- Una Membresía solo puede tener asociada una clase de combate.
- Una Clase de combate puede pertenecer a varias membresías.

## 🔑 Permisos de los Usuarios

| Tipo de Usuario | Permisos                                                         |
|-----------------|------------------------------------------------------------------|
| Administrador   | Gestión completa de Usuarios, Suscripciones, Clases y Productos. |
| Usuario         | Compra de productos y suscripcion a membresías                   |

## 🖼️ Imágenes Asociadas

- Los Productos tienen una imagen.

## 📊 Diagrama de Entidades

![Diagrama ER](entidades.png)

## 💪 Desarrollo Colaborativo

### Daniel Vallejo Pasamón
Como partimos de la practica del año pasado me he encargado de añadir los dtos, funcionalidad que habia que implementar de 0.

**Commits más significativos:**
- [Commit 1](https://github.com/DWS-2025/project-grupo-r-2/commit/df3caa3c295601f0b02dfbd8d083db5164f20344) Este es el 4 commit de dtos desde que los he creado y he ido poco a poco añadiendo las funcinalidades e implementando los mappers.
- [Commit 2](https://github.com/DWS-2025/project-grupo-r-2/commit/9704381493e11ba6203c8eb51cde36181445efcc) Aqui ya consegui que compilase y ejecutase los dtos.
- [Commit 3](https://github.com/DWS-2025/project-grupo-r-2/commit/963c144fe769b1f091da8bc1f7a7c19b5815ab4b) Aqui hice que funcionaran las peticiones rest utilizando dtos de users y products, faltaban memberships y combatclass.
- [Commit 4](https://github.com/DWS-2025/project-grupo-r-2/commit/4f189fbbc6a8b9abd9eaf8cf51e63a46dbf98daf) Aqui arregle las peticiones de postman para las dos entidades que faltaban.
- [Commit 5](#) 
**Ficheros principales modificados:**
- `Todos los entidadDTO.java`
- `Todos los entidadmapper.java`
- `Todos los service de cada entidad`
- `Todos los rest controllers de cada entidad`
- `Todos los servicios de cada entidad`

### Cesar Martín Baños
Como partimos de la práctica del año pasado me he encargado de arreglar por completo las funcionalidades que fallaban y hacer el nuevo css, a parte he modificado la base de datos para que muestre bien las relaciones y lo inicialice de manera correcta, también he hecho algunos cambios en los json de prueba para rest. 

**Commits más significativos:**
- [Commit 1](https://github.com/DWS-2025/project-grupo-r-2/commit/31c5fa0db010a4fbf08dbc791ca1f933c18dfa62) Nuevo css, aun con fallos, pero es la base
- [Commit 2](https://github.com/DWS-2025/project-grupo-r-2/commit/687b4ed7a1138c2bc9726a0bd0173fd898734e91) Arreglo errores de compatibilidad del css por completo
- [Commit 3](https://github.com/DWS-2025/project-grupo-r-2/commit/e6e90ec3a57afad03d21e9d250fc1642ab31729f) Ajusto la base de datos y sus relaciones
- [Commit 4](https://github.com/DWS-2025/project-grupo-r-2/commit/19268b72b115b23373e99f7a50e56a14190b691f) Arreglo errores en los controllers, ahora si distinguen entre admin y user
- [Commit 5](https://github.com/DWS-2025/project-grupo-r-2/commit/4bce4a820a79b55c849247e6b4aa772ea01d0c63) Aqui implementé la paginación de memberships aunque faltaban cosas que hice más adelante


**Ficheros principales modificados:**
- `styles.css`
- `MarketController.java`
- `DatabaseInitializer.java`
- `paginacion.js`
- `MembershipController.java`
---
