
$javaHome = "C:\Users\Anne Koziolek\galette\instrumented-jdk-17"
$agentJar = "C:\Users\Anne Koziolek\Documents\code-GitHub\galette-concolic-model-transformation\galette-agent\target\galette-agent-1.0.0-SNAPSHOT.jar"
$classes = "C:\Users\Anne Koziolek\Documents\code-GitHub\TestGallete\vsum\target\classes"
$cpFile = "C:\Users\Anne Koziolek\Documents\code-GitHub\TestGallete\vsum\target\classpath.txt"


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






