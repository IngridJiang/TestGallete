
$javaHome = "C:\Users\10239\galette\instrumented-jdk-17"
$agentJar = "C:\Users\10239\galette\galette-agent\target\galette-agent-1.0.0-SNAPSHOT.jar"
$classes = "C:\Users\10239\Amathea-acset\vsum\target\classes"
$cpFile = "C:\Users\10239\Amathea-acset\vsum\target\classpath.txt"


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






