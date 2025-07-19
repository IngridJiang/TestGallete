# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an Amalthea–Acset example project demonstrating the Vitruv methodology template. It implements consistency management between Amalthea and Acset metamodels using the Vitruv framework, with Galette instrumentation for dynamic analysis.

## Build Commands

**Primary build command:**
```bash
mvn clean verify
```

**Run with Galette instrumentation (Windows PowerShell):**
```powershell
cd vsum
.\run-galette.ps1
```

**Manual execution (replace placeholders):**
```powershell
<INSTRUMENTED_JDK_17>\bin\java.exe ^
  -cp "<PROJECT_ROOT>\consistency\target\classes" ^
  -Xbootclasspath/a:"<GALETTE_ROOT>\galette-agent\target\galette-agent-1.0.0-SNAPSHOT.jar" ^
  -javaagent:"<GALETTE_ROOT>\galette-agent\target\galette-agent-1.0.0-SNAPSHOT.jar" ^
  tools.vitruv.methodologisttemplate.vsum.VSUMRunner
```

**Test execution:**
```bash
mvn test
```

## Architecture

### Module Structure
- **model/**: Ecore metamodels (model.ecore, model2.ecore) and generated Java code
- **consistency/**: Consistency reactions (.reactions files) defining transformation rules
- **viewtype/**: View-type definitions extending IdentityMappingViewType with change filtering
- **vsum/**: VSUM (Virtual Single Underlying Model) implementation and test examples

### Key Components

**Consistency Reactions (consistency/templateReactions.reactions)**:
- Defines bidirectional transformations between Amalthea and Acset models
- Handles ComponentContainer ↔ AscetModule mapping
- Interactive Task creation with user selection dialogs
- Supports InterruptTask, PeriodicTask, SoftwareTask, TimeTableTask

**VSUM Framework**:
- `VSUMRunner.java`: Entry point for standalone execution
- `Test.java`: Core VSUM operations and model manipulation
- `VSUMExampleTest.java`: JUnit tests demonstrating VSUM usage patterns

**ViewType System**:
- `ChangeTransformingViewType`: Abstract base for filtered change propagation
- Filter registration/unregistration for EChange transformation

### Dependencies
- Vitruv Framework 3.1.2 (change propagation, views, reactions)
- Eclipse EMF (Ecore models and XMI serialization)
- JUnit 5 for testing
- Galette agent for instrumentation

## Special Requirements

- **Java 17** instrumented with Galette
- Galette agent JAR at runtime
- Generated model code must be built before execution
- PowerShell script requires absolute paths configuration