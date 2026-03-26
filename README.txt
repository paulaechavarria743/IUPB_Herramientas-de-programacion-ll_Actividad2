=== SISTEMA DE GESTIÓN DE ENVÍOS - Logística ===
Examen 2 · Herramientas de Programación II · Marzo 2026

ARCHIVOS DEL PROYECTO (todos en src/):
  Envio.java      → Clase abstracta base (padre)
  Terrestre.java  → Hereda de Envio, tarifa: $1500/km + $2000/kg
  Aereo.java      → Hereda de Envio, tarifa: $5000/km + $4000/kg
  Maritimo.java   → Hereda de Envio, tarifa:  $800/km + $1000/kg
  Logistica.java  → GUI Swing + main()

CÓMO EJECUTAR EN NETBEANS / INTELLIJ / ECLIPSE:
  1. Crear nuevo proyecto Java
  2. Copiar los 5 archivos .java a la carpeta src/
  3. Ejecutar Logistica.java (tiene el método main)

COMPILACIÓN Y EJECUCIÓN DESDE TERMINAL:
  javac src/*.java -d bin
  java -cp bin Logistica

FUNCIONALIDADES:
  ✔ Agregar envío (seleccionar tipo, ingresar código, cliente, peso, distancia)
  ✔ Retirar envío (seleccionar fila en la tabla → clic "Retirar")
  ✔ Listar todos los envíos con su tarifa calculada
  ✔ Validación de campos vacíos y códigos duplicados
  ✔ Interfaz gráfica con tabla y formulario

FÓRMULA DE TARIFA:
  Costo = (distancia × tarifa_base_km) + (peso × recargo_kg)
