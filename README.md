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
- [Commit 5](https://github.com/DWS-2025/project-grupo-r-2/commit/8019cfc543694750f7a9a1efc7e83910f8c8b418) Para que guarde la imagen correctamente en el post dentro de la carpeta images del proyecto.
**Ficheros principales modificados:**
- `Todos los entidadDTO.java`
- `Todos los entidadmapper.java`
- `Todos los service de cada entidad`
- `Todos los rest controllers de cada entidad`
- `Todos los servicios de cada entidad`

### Cesar Martín Baños
En esta fase, al tenerlo casi todo del año pasado y al ir actualizando esta fase conforme avanzabamos en las anteriores, hemos revisado posibles vulnerabilidades, pero hemos hecho pocos cambios.

**Commits más significativos:**
- [Commit 1](https://github.com/DWS-2025/project-grupo-r-2/commit/49a3170211ded55107200c9d642112161a67411f) Descargar imagen de disco
- [Commit 2](https://github.com/DWS-2025/project-grupo-r-2/commit/4bcf9b034a6047faf52fd0bd92be3aebace6b774) Actualizo nueva carpeta Security
- [Commit 3](https://github.com/DWS-2025/project-grupo-r-2/commit/d609c5c1a3f53d801b329d4e1e5e41c5514a9d95) Quito librerias no utilizadas y configuro el control de acceso
- [Commit 4](https://github.com/DWS-2025/project-grupo-r-2/commit/bdde66ec8b9950b43be93edc1047809112a10a84) Añado los iconos para obtenerlos del disco y no hacer conexiones a otras páginas web
- [Commit 5](https://github.com/DWS-2025/project-grupo-r-2/commit/5f5c8da6535590fe890cb61c842ea3309b785aed) Añado mas configuración que tapa pequeñas vulnerabilidades


**Ficheros principales modificados:**
- `SecurityConfig.java`
- `MarketController.java`
- `DatabaseInitializer.java`
- `/bootstrap-icons-1.11.3`
- `application.properties`
---
