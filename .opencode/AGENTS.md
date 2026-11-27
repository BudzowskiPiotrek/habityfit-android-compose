# Reglas del Agente

## 1. Estilo de Respuesta

- Responde siempre de forma extremadamente corta, concisa y directa al grano.
- Cero relleno o explicaciones innecesarias.

## 2. Rutas Ignoradas

No leer, indexar ni editar estos paths; son artefactos, caché o configuración local:

- `build/` (incluye `app/build/` y `core/*/build/`)
- `.gradle/`
- `.kotlin/`
- `.idea/`
- `*.log`

## 3. Arquitectura

- Consulta siempre `.opencode/ARCHITECTURE.md` antes de buscar archivos por el proyecto.
- Utiliza la arquitectura documentada como referencia.
- Si detectas información desactualizada, comunícalo y actualiza la documentación cuando corresponda.
- No inventes información arquitectónica. Distingue entre información verificada y desconocida.

## 4. Tareas

- Toda tarea se gestiona en `.opencode/TASK.md`.
- Respeta sus estados y reglas.
- Registra las tareas antes de modificar código.
- Al terminar, utiliza exclusivamente `[?] Pendiente de revisar`.

## 5. Modificaciones

- Inspecciona el código antes de modificarlo.
- Cambia únicamente lo necesario para resolver la tarea.
- Respeta la arquitectura y convenciones existentes.
- Evita refactorizaciones no solicitadas.
- No introduzcas dependencias sin justificación.
