
#define MyAppName "BymetooBotones"
#define MyAppVersion "1.0"
#define MyAppPublisher "Bymetoo"

[Setup]
AppId={{GENERAR-GUID-PARA-ESTA-APLICACION}}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppPublisher={#MyAppPublisher}
DefaultDirName={autopf}\BymetooBotones
DefaultGroupName=BymetooBotones
PrivilegesRequired=admin
OutputDir=.
OutputBaseFilename=Instalador-BymetooBotones-v1.0
Compression=lzma
SolidCompression=yes
WizardStyle=modern
Uninstallable=yes

[Files]
Source: "..\dist\BymetooBotones.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "..\README.md"; DestDir: "{app}"; Flags: ignoreversion
Source: "..\docs\Manual-tecnico.md"; DestDir: "{app}\docs"; Flags: ignoreversion

[Messages]
WelcomeLabel2=Este asistente instalará la biblioteca de componentes Java Swing BymetooBotones en su equipo.
