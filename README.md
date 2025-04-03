# Spartan Wrath

<h1 align="center">Hola 👋, Somos Spartan's Wrath</h1>
<h3 align="center">Grupo R-2 de Desarrollo Web Seguro</h3> 

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
- Un Usuario puede tener varios productos, puede estar suscrito a varias membresías.
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

**Commits más significativos:**
- [Commit 1](#)
- [Commit 2](#)
- [Commit 3](#)
- [Commit 4](#)
- [Commit 5](#)

**Ficheros principales modificados:**
- `fichero1`
- `fichero2`
- `fichero3`
- `fichero4`
- `fichero5`

### Cesar Martín Baños

**Commits más significativos:**
- [Commit 1](https://github.com/DWS-2025/project-grupo-r-2/commit/31c5fa0db010a4fbf08dbc791ca1f933c18dfa62) Nuevo css, aun con fallos, pero es la base
- [Commit 2](https://github.com/DWS-2025/project-grupo-r-2/commit/687b4ed7a1138c2bc9726a0bd0173fd898734e91) Arreglo errores de compatibilidad del css por completo
- [Commit 3](https://github.com/DWS-2025/project-grupo-r-2/commit/e6e90ec3a57afad03d21e9d250fc1642ab31729f) Ajusto la base de datos y sus relaciones
- [Commit 4](https://github.com/DWS-2025/project-grupo-r-2/commit/19268b72b115b23373e99f7a50e56a14190b691f) Arreglo errores en los controllers, ahora si distinguen entre admin y user
- [Commit 5](#)

**Ficheros principales modificados:**
- `styles.css`
- `MarketController.java`
- `DatabaseInitializer.java`
- `login.html`
- `MembershipController.java`
---
