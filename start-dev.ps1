docker-compose -f compose.yml -f compose.dev.yml up --build --watch
if ($LASTEXITCODE -ne 0) {
    Read-Host -Prompt "Press Enter to exit"
}
