Set objFSO = CreateObject("Scripting.FileSystemObject")
strFolder = objFSO.GetParentFolderName(WScript.ScriptFullName)
Set WshShell = CreateObject("WScript.Shell")
WshShell.CurrentDirectory = strFolder
WshShell.Run "javaw -jar target\patent-mgn-app.jar", 0, False
Set WshShell = Nothing
