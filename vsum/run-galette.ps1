
# Configuration - Update these paths for your environment
$javaHome = $env:INSTRUMENTED_JAVA_HOME
if (-not $javaHome) {
    Write-Host "Please set INSTRUMENTED_JAVA_HOME environment variable to your Galette-instrumented JDK path"
    Write-Host "Example: setx INSTRUMENTED_JAVA_HOME 'C:\path\to\instrumented-jdk-17'"
    exit 1
}

$projectRoot = Split-Path $PSScriptRoot -Parent
$agentJar = "$projectRoot\..\galette-concolic-model-transformation\galette-agent\target\galette-agent-1.0.0-SNAPSHOT.jar"
$classes = "$PSScriptRoot\target\classes"
$cpFile = "$PSScriptRoot\target\classpath.txt"


$deps = Get-Content -Raw $cpFile


$cp = "$classes;$deps"


if (!(Test-Path $agentJar)) {
  Write-Host "agent jar not found: $agentJar"
  exit 1
}

& "$javaHome\bin\java.exe" `
  -cp "$cp" `
  -Xbootclasspath/a:"$agentJar" `
  -javaagent:"$agentJar" `
  -Djava.compiler=NONE `
  tools.vitruv.methodologisttemplate.vsum.VSUMRunner






