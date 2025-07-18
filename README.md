 # Amalthea–Acset Example Project

This repository demonstrates how to use **Amalthea** and **Acset** with the Vitruv methodology template. It contains

*  **Ecore metamodel** and the corresponding **generated model code**,
* **consistency reactions** (CPR) that integrate external user input, and
* a **VSUM example model** that creates a task.

---

## Project Structure

| Directory | Purpose |
|-----------|---------|
| `model` | The Amalthea/Acset Ecore metamodel and generated model code. |
| `consistency` | Consistency reactions (CPR) that react to external user input. |
| `viewtype` | View‑type definitions (not required for getting started). |
| `vsum` | Example VSUM model used by the Vitruv methodology template. |

---

## Prerequisites

* **Java 17** – **instrumented with Galette**¹
* **Maven 3.8+**
* A locally built **Galette agent JAR** (`galette-agent‑1.0.0‑SNAPSHOT.jar`)

> ¹ Follow the Galette documentation to instrument your JDK 17, then set the `JAVA_HOME` environment variable accordingly.

---

## Getting Started

### 1 · Generate Java Code

Run Maven in the repository root:

```bash
mvn clean verify
```

Generated sources and classes appear in each module’s `target` folder.

### 2 · Run the VSUM Runner

Replace the placeholder paths in the command below and execute it **with your instrumented JDK 17**:
How to instrument: https://github.com/neu-se/galette

```powershell
<INSTRUMENTED_JDK_17>\bin\java.exe ^
  -cp "<PROJECT_ROOT>\consistency\target\classes" ^
  -Xbootclasspath/a:"<GALETTE_ROOT>\galette-agent\target\galette-agent-1.0.0-SNAPSHOT.jar" ^
  -javaagent:"<GALETTE_ROOT>\galette-agent\target\galette-agent-1.0.0-SNAPSHOT.jar" ^
  tools.vitruv.methodologisttemplate.vsum.VSUMRunner
```

#### Placeholder explanation

| Placeholder | Description |
|-------------|-------------|
| `<INSTRUMENTED_JDK_17>` | Path to the JDK 17 that you instrumented with Galette. |
| `<PROJECT_ROOT>` | Root directory of this repository (e.g. `C:\Users\<user>\Amalthea-acset`). |
| `<GALETTE_ROOT>` | Path to your local Galette checkout that contains the `galette-agent` module. |






