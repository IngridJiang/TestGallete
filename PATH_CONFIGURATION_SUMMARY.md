# Path Configuration Update Summary

## Problem Identified
The project contained hardcoded Windows paths (`C:/Users/10239/Amathea-acset`) in runtime-generated files that prevented the application from running correctly on different systems.

## Solutions Implemented

### 1. Configuration System
Created a flexible configuration system to replace all hardcoded paths:

**Files Created:**
- `vsum/galette-config.properties` - Main configuration file
- `vsum/src/main/resources/galette-config.properties` - Configuration on classpath
- `vsum/src/main/java/tools/vitruv/methodologisttemplate/vsum/GaletteConfig.java` - Configuration utility class

**Configuration Properties:**
```properties
# Working directory for VSUM output
vsum.working.dir=galette-output-0

# Base project path (uses current directory by default)
project.base.path=${user.dir}

# Model file patterns
model.file.pattern=example.model
model2.file.pattern=example.model2

# Output configuration
output.dir=galette-test-output
output.file=vsum-output.xmi
```

### 2. Updated Application Code
**Modified:**
- `vsum/src/main/java/tools/vitruv/methodologisttemplate/vsum/VSUMRunner.java`
  - Now uses `GaletteConfig` to load working directory
  - Displays configuration values on startup
  - No more hardcoded `"galette-output-0"` path

### 3. Enhanced Run Scripts
**Updated PowerShell script:** `vsum/run-galette.ps1`
- Uses environment variable `INSTRUMENTED_JAVA_HOME` instead of hardcoded paths
- Calculates relative paths from script location
- Better error messages and configuration guidance

**Created bash script:** `vsum/run-galette.sh`
- Linux/Unix equivalent of PowerShell script
- Uses environment variables for configuration
- Relative path resolution
- Comprehensive error checking

### 4. Data Cleanup Utilities
**Created:** `vsum/clean-galette-data.sh`
- Removes runtime files with hardcoded Windows paths
- Allows VSUM to regenerate metadata with correct paths
- Can be run anytime to reset VSUM state

## Hardcoded Paths Eliminated

### Before:
- **Code:** `Path workDir = Paths.get("galette-output-0");`
- **PowerShell:** Hardcoded Windows paths to user directories
- **Data files:** `file:/C:/Users/10239/Amathea-acset/vsum/galette-output-0/example.model2`

### After:
- **Code:** `Path workDir = config.getWorkingPath();` (configurable)
- **Scripts:** Use environment variables and relative paths
- **Data files:** Will be regenerated with correct paths automatically

## Usage Instructions

### Environment Setup
```bash
# Set the path to your Galette-instrumented JDK
export INSTRUMENTED_JAVA_HOME=/path/to/galette/instrumented-jdk-17
```

### Running the Application
```bash
# Option 1: Use the new bash script (recommended)
cd vsum
./run-galette.sh

# Option 2: Manual execution with environment variables
export INSTRUMENTED_JAVA_HOME=/path/to/instrumented/jdk
CLASSPATH_FILE="target/classpath.txt"
FULL_CLASSPATH="target/classes:$(cat $CLASSPATH_FILE)"
$INSTRUMENTED_JAVA_HOME/bin/java -cp "$FULL_CLASSPATH" \
  -Xbootclasspath/a:"../galette-concolic-model-transformation/galette-agent/target/galette-agent-1.0.0-SNAPSHOT.jar" \
  -javaagent:"../galette-concolic-model-transformation/galette-agent/target/galette-agent-1.0.0-SNAPSHOT.jar" \
  -Djava.compiler=NONE \
  tools.vitruv.methodologisttemplate.vsum.VSUMRunner
```

### Configuration Customization
Edit `vsum/galette-config.properties` to customize:
- Working directory location
- Project base path
- Model file patterns
- Output directory and file names

## Benefits
1. **Portability:** No more hardcoded Windows paths
2. **Flexibility:** Easy to configure for different environments
3. **Maintainability:** Central configuration management
4. **Error Prevention:** Clear error messages when paths are incorrect
5. **Team Collaboration:** Each developer can set their own paths

## Status
✅ All hardcoded paths identified and replaced
✅ Configuration system implemented and tested
✅ Run scripts updated with environment variable support
✅ Project builds successfully with new configuration
✅ Data cleanup utilities provided
✅ Documentation complete

The application now uses configurable paths and should work correctly on any system after setting the appropriate environment variables.