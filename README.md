# Los Bloques (v0.1, un jugador)
Mod de Minecraft Java (Fabric 1.21.1) que construye una ciudad de bloques con estética de Los Santos.
Uso: crea un mundo **plano** en creativo, ejecuta `/ciudad` (o `/ciudad 6`) y se genera alrededor de ti.
GTA San Andreas es solo inspiración visual: NO es juego requerido.

## Estado: SIN COMPILAR NI PROBAR
- Las versiones de Fabric/Loom/yarn son las que conozco, sin verificar. Compruebalas en https://fabricmc.net/develop/
- Falta el gradle wrapper: genera una plantilla en https://fabricmc.net/develop/template/ (1.21.1) y copia `gradle/` y `gradlew*`, o ejecuta `gradle wrapper`.
- Para compilar: `python tools/gen.py` y luego `./gradlew build` (JDK 21). El jar sale en `build/libs/`.

## Diseño
Las hojas de `design/*.json` son la fuente de verdad: cambia la hoja, ejecuta `tools/gen.py` (corre antes el preflight) y se regenera `CityData.java`.
