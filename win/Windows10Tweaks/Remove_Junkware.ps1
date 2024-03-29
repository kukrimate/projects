param (
[Parameter(Mandatory=$true)][string]$ImagePath
)

# Comment out any additional Apps you want to keep
$PackageList = @(
"Microsoft.BingWeather"
#"Microsoft.DesktopAppInstaller"
"Microsoft.GetHelp"
"Microsoft.Getstarted"
"Microsoft.Messaging"
"Microsoft.Microsoft3DViewer"
"Microsoft.MicrosoftOfficeHub"
"Microsoft.MicrosoftSolitaireCollection"
"Microsoft.MicrosoftStickyNotes"
"Microsoft.MSPaint"
"Microsoft.Office.OneNote"
"Microsoft.OneConnect"
"Microsoft.People"
"Microsoft.Print3D"
"Microsoft.SkypeApp"
#"Microsoft.StorePurchaseApp"
"Microsoft.Wallet"
"Microsoft.Windows.Photos"
"Microsoft.WindowsAlarms"
#"Microsoft.WindowsCalculator"
"Microsoft.WindowsCamera"
"microsoft.windowscommunicationsapps"
"Microsoft.WindowsFeedbackHub"
"Microsoft.WindowsMaps"
"Microsoft.WindowsSoundRecorder"
#"Microsoft.WindowsStore"
#"Microsoft.Xbox.TCUI"
"Microsoft.XboxApp"
"Microsoft.XboxGameOverlay"
#"Microsoft.XboxIdentityProvider"
"Microsoft.XboxSpeechToTextOverlay"
"Microsoft.ZuneMusic"
"Microsoft.ZuneVideo"
)

foreach ($Package in $PackageList)
{
    Get-AppxProvisionedPackage -Path $ImagePath | where DisplayName -EQ $Package | Remove-AppxProvisionedPackage
}