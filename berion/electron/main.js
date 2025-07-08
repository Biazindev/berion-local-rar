const { app, BrowserWindow } = require('electron');
const { spawn } = require('child_process');
const path = require('path');

let javaProcess;

function createWindow() {
  const win = new BrowserWindow({
    width: 1200,
    height: 800,
    webPreferences: {
      nodeIntegration: false,
    },
  });

  win.loadFile(path.join(__dirname, '..', 'build', 'index.html'));
}

app.whenReady().then(() => {
  // Inicia o back-end Java
  const jarPath = 'C:\\Users\\Tiago\\Desktop\\front-local\\simplifica-contabil\\target\\simplifica-contabil-0.0.1-SNAPSHOT.jar.original';
  javaProcess = spawn('java', ['-jar', jarPath]);

  javaProcess.stdout.on('data', data => console.log(`Back-end: ${data}`));
  javaProcess.stderr.on('data', data => console.error(`Erro: ${data}`));

  createWindow();

  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) createWindow();
  });
});

app.on('window-all-closed', () => {
  if (javaProcess) javaProcess.kill();
  if (process.platform !== 'darwin') app.quit();
});
