# Sistema Distribuido con RAFT - Entrenamiento y Consulta de Modelos IA

Este proyecto implementa un sistema distribuido para el entrenamiento y consulta de modelos de IA utilizando comunicación por sockets TCP, replicación entre nodos y una interfaz gráfica para el cliente. Además, se permite el uso de Docker para facilitar pruebas y despliegue.

---

## 🧩 Componentes principales

### 🔹 Worker Líder (Java)

* Recibe datos de entrenamiento desde el cliente (por socket TCP)
* Entrena un modelo dummy (regresión lineal con 2 puntos)
* Asigna un ID único al modelo
* Replica el modelo al Worker Follower

### 🔹 Worker Follower (Python)

* Recibe modelos desde el líder
* Los guarda localmente en `models/`
* Ofrece monitoreo por HTTP (puerto 8080)

### 🔹 Cliente (Java Swing GUI)

* GUI para entrenamiento: campos x1, x2, y1, y2
* GUI para consulta: campo de ID del modelo
* Visualiza respuestas en tiempo real

### 🔹 Docker (opcional)

* Permite levantar el sistema en contenedores
* Simplifica pruebas y entrega

---

## 🚀 Instrucciones de ejecución

### 🔹 Modo clásico (sin Docker)

#### 1. Ejecutar Worker Líder (Java):

```bash
cd java/worker_leader
mvn compile
mvn exec:java -Dexec.mainClass="com.distribuitedai.Main"
```

#### 2. Ejecutar Worker Follower (Python):

```bash
cd python/worker_follower
python3 worker_tcp_server.py
python3 monitor_http.py  # En otra terminal (opcional)
```

#### 3. Ejecutar Cliente (GUI Java):

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.distribuitedai.client.ClienteGUI"
```

---

## 🐳 Modo Docker (en progreso)

### Estructura esperada:

```
SistemaDistribuidoRAFT/
├── java/worker_leader/Dockerfile
├── python/worker_follower/Dockerfile
├── docker-compose.yml
```

### Comando para levantar:

```bash
docker-compose up --build
```

---

## 📦 Formato de mensajes TCP

* **Entrenamiento:**

  ```
  ENTRENAMIENTO:x1,x2;y1,y2
  ```

  (Ejemplo: `ENTRENAMIENTO:1.0,2.0;3.0,5.0`)

* **Consulta:**

  ```
  CONSULTA:<id>
  ```

  (Ejemplo: `CONSULTA:abc123-uuid`)

---

## 📂 Estructura de carpetas

```
SistemaDistribuidoRAFT/
├── java/
│   └── worker_leader/
│       ├── src/main/java/com/distribuitedai/
│       └── pom.xml
├── python/
│   └── worker_follower/
│       ├── worker_tcp_server.py
│       ├── monitor_http.py
├── models/                # Modelos replicados
├── logs/                  # Logs opcionales
├── scripts/               # Script de carga masiva (1000 registros)
├── informe_presentacion/  # PDFs a entregar
```

---

## 📌 Diagrama de Arquitectura (describir visualmente):

```
[Cliente GUI] <--TCP--> [Worker Líder Java] <--TCP--> [Worker Follower Python]
                                        \--HTTP--> Navegador (monitoreo)
```

---

## 📄 Informe y Entrega

* Incluir capturas de:

  * Entrenamiento desde GUI
  * Consulta exitosa
  * Monitoreo HTTP
* Graficar:

  * Arquitectura distribuida
  * Flujo de entrenamiento y replicación
* Explicar:

  * Algoritmo RAFT (implementación básica o simulada)
  * Uso de hilos y concurrencia

---

## Extras

* Script `carga_masiva.py` para evaluar rendimiento con 1000 registros
* Uso de UUID para asignación de modelos
* GUI robusta con validación y mensajes de error claros

---

>Proyecto realizado para la Práctica 04 - CC4P1 Programación Concurrente y Distribuida (2025-I)
