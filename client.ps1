$hostName = "localhost"
$port = 8089
$utf8NoBom = New-Object System.Text.UTF8Encoding($false)
$tcp = New-Object System.Net.Sockets.TcpClient($hostName, $port)
$stream = $tcp.GetStream()
$reader = New-Object System.IO.StreamReader($stream, $utf8NoBom)
$writer = New-Object System.IO.StreamWriter($stream, $utf8NoBom)
$writer.AutoFlush = $true
Write-Host "Подключение к $($hostName):$($port)..." -ForegroundColor Cyan
$welcome = $reader.ReadLine()
Write-Host $welcome -ForegroundColor Green
while ($true) {
    $input = Read-Host "Вы"
    if ($input -eq "/exit") { break }
    $writer.WriteLine($input)
    $response = $reader.ReadLine()
    if ($response) { Write-Host $response -ForegroundColor Yellow }
}
$tcp.Close()
Write-Host "Отключено." -ForegroundColor Red
